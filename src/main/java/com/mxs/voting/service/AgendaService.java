package com.mxs.voting.service;

import com.mxs.voting.exception.InternalErrorException;
import com.mxs.voting.exception.NotFoundException;
import com.mxs.voting.model.AgendaModel;
import com.mxs.voting.repository.AgendaRepository;
import com.mxs.voting.request.RegisterAgendaRequest;
import com.mxs.voting.request.StartAgendaRequest;
import com.mxs.voting.response.RegisterAgendaResponse;
import com.mxs.voting.response.StartAgendaResponse;
import com.mxs.voting.type.AgendaStatusType;
import com.mxs.voting.type.DomainType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.mxs.voting.constant.MessageConstant.AGENDA_NOT_FOUND;
import static com.mxs.voting.constant.MessageConstant.ERROR_VOTING_FLOW;
import static java.lang.Thread.sleep;

/**
 * Service class used by the Agenda entity.
 */
@Service
public class AgendaService {
    private static final Logger logger = LoggerFactory.getLogger(AgendaService.class);
    private static final long SIXTY_THOUSAND = 60000;
    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Method responsible for registering an Agenda.
     *
     * @param registerAgendaRequest - Agenda entry fields
     * @return Agenda code
     */
    public RegisterAgendaResponse registerAgenda(RegisterAgendaRequest registerAgendaRequest) {
        AgendaModel agendaModel =
                AgendaModel.builder()
                        .title(registerAgendaRequest.getTitle())
                        .description(registerAgendaRequest.getDescription())
                        .duration(registerAgendaRequest.getDuration())
                        .agendaStatusType(AgendaStatusType.CREATED)
                        .build();
        logger.info("AgendaController.registerAgenda -> agendaModel: {}", agendaModel);
        AgendaModel agendaModelCreated = agendaRepository.save(agendaModel);
        logger.info("AgendaController.registerAgenda -> agendaModelCreated: {}", agendaModelCreated);
        saveAgendaInCache(agendaModel);
        return RegisterAgendaResponse.builder().code(agendaModelCreated.getCode()).build();
    }

    /**
     * Method responsible for starting the voting period.
     * Search an Agenda, set its status to "V". It then persists the entity in the relational database and in the cache.
     * Finally, the voting period begins.
     *
     * @param startAgendaRequest - Agenda code
     * @return Voting period completion status
     */
    public StartAgendaResponse startAgenda(StartAgendaRequest startAgendaRequest) {
        AgendaModel agendaModel = findAgenda(startAgendaRequest);
        agendaModel.setAgendaStatusType(AgendaStatusType.VOTING);
        agendaRepository.save(agendaModel);
        saveAgendaInCache(agendaModel);
        return startTimer(agendaModel);
    }

    /**
     * Method responsible for fetching a Agenda.
     * Checks first in cache, if not found, checks in relational database.
     * If in none of the cases it finds any record, it throws a NotFoundException with the message "Agenda not found".
     *
     * @param startAgendaRequest - Agenda code
     * @return Agenda entity
     * @exception NotFoundException - Agenda not found
     */
    private AgendaModel findAgenda(StartAgendaRequest startAgendaRequest) {
        AgendaModel agendaModel = null;
        try {
            agendaModel = (AgendaModel) redisTemplate.opsForHash().get(DomainType.AGENDA, startAgendaRequest.getAgendaCode());
            if (agendaModel != null) {
                logger.info("AgendaController.findAgenda -> Agenda found in cache. agendaModel: {}", agendaModel);
                return agendaModel;
            }
        } catch (Exception e) {
            logger.info("An error occurred while trying to get cached information. Cause: {}, Message: {}", e.getCause(), e.getMessage());
        }

        agendaModel = agendaRepository.findByCodeAndStatusAgendaEquals(startAgendaRequest.getAgendaCode(), AgendaStatusType.CREATED.getCode())
                .orElseThrow(() -> new NotFoundException(AGENDA_NOT_FOUND));
        logger.info("AgendaController.findAgenda -> Agenda found in the database. agendaModel: {}", agendaModel);

        return agendaModel;
    }

    /**
     * Method responsible for starting the voting period.
     * An asynchronous call is made where the thread will wait for the time specified in the "duration" field.
     * In the end, the Agenda entity will be persisted in the relational database and cached.
     *
     * @param agendaModel - Agenda entity
     * @return Voting period completion status
     * @exception InternalErrorException - An error occurred during the voting period
     */
    private StartAgendaResponse startTimer(AgendaModel agendaModel) {
        try {
            final CompletableFuture<StartAgendaResponse> future = CompletableFuture.supplyAsync(() -> {

                long milliseconds = agendaModel.getDuration() * SIXTY_THOUSAND;

                Instant start = Instant.now();
                logger.info("AgendaController.startTimer -> time started: {}", start);

                try {
                    sleep(milliseconds);
                } catch (InterruptedException e) {
                    logger.error("AgendaController.startTimer -> An error occurred while executing the sleep method. " +
                            "Message: {}, Cause: {}", e.getMessage(), e.getCause());
                }

                Instant end = Instant.now();
                logger.info("AgendaController.startTimer -> time ended: {}", end);

                agendaModel.setStarted(LocalDateTime.ofInstant(start, ZoneOffset.UTC));
                agendaModel.setEnded(LocalDateTime.ofInstant(end, ZoneOffset.UTC));
                agendaModel.setAgendaStatusType(AgendaStatusType.FINISHED);

                agendaRepository.save(agendaModel);
                saveAgendaInCache(agendaModel);

                return StartAgendaResponse
                        .builder()
                        .code(agendaModel.getCode())
                        .started(agendaModel.getStarted())
                        .ended(agendaModel.getEnded())
                        .status(agendaModel.getAgendaStatusType().name())
                        .build();
            });
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("AgendaController.startTimer -> An error occurred during the voting period. " +
                    "Message: {}, Cause: {}", e.getMessage(), e.getCause());
            throw new InternalErrorException(ERROR_VOTING_FLOW);
        }
    }

    /**
     * Method responsible for saving an Agenda in Redis asynchronously.
     * If there is any impediment to execution, the method will not throw an exception and will continue with the flow.
     *
     * @param agendaModel - Agenda entity
     */
    private void saveAgendaInCache(AgendaModel agendaModel) {
        CompletableFuture.runAsync(() -> {
            try {
                redisTemplate.opsForHash().put(DomainType.AGENDA, agendaModel.getCode(), agendaModel);
                logger.info("AgendaController.saveAgendaInCache -> Agenda included in cache. Key: {}, HashKey:{}, Value: {}",
                        DomainType.AGENDA, agendaModel.getCode(), agendaModel);
            } catch (Exception e) {
                logger.info("An error occurred while trying to get cached information. Cause: {}, Message: {}", e.getCause(), e.getMessage());
            }
        });
    }
}
