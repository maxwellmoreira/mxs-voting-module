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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.mxs.voting.constant.MessageConstant.AGENDA_NOT_FOUND;
import static com.mxs.voting.constant.MessageConstant.ERROR_VOTING_FLOW;
import static java.lang.Thread.sleep;

@Service
public class AgendaService {
    private static final Logger logger = LoggerFactory.getLogger(AgendaService.class);
    private static final long SIXTY_THOUSAND = 60000;

    @Autowired
    private AgendaRepository agendaRepository;

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
        return RegisterAgendaResponse.builder().code(agendaModelCreated.getCode()).build();
    }

    public StartAgendaResponse startAgenda(StartAgendaRequest startAgendaRequest) {
        Optional<AgendaModel> agendaModelOptional = agendaRepository.findByCodeAndStatusAgendaEquals(startAgendaRequest.getAgendaCode(), AgendaStatusType.CREATED.getCode());
        logger.info("AgendaController.startAgenda -> agendaModelOptional: {}", agendaModelOptional);
        return agendaModelOptional.map(
                agenda -> {
                    agenda.setAgendaStatusType(AgendaStatusType.VOTING);
                    agendaRepository.save(agenda);
                    return startTimer(agenda);
                }).orElseThrow(() -> new NotFoundException(AGENDA_NOT_FOUND));
    }

    private StartAgendaResponse startTimer(AgendaModel agendaModel) {
        try {
            final CompletableFuture<StartAgendaResponse> future = CompletableFuture.supplyAsync(() -> {

                long milliseconds = agendaModel.getDuration() * SIXTY_THOUSAND;

                Instant start = Instant.now();
                logger.info("AgendaController.startTimer -> time started: {}", start);

                try {
                    sleep(milliseconds);
                } catch (InterruptedException e) {
                    logger.error("AgendaController.startTimer -> An error occurred during the voting period. " +
                            "Message: {}, Cause: {}", e.getMessage(), e.getCause());
                    throw new InternalErrorException(ERROR_VOTING_FLOW);
                }

                Instant end = Instant.now();
                logger.info("AgendaController.startTimer -> time ended: {}", end);

                agendaModel.setStarted(LocalDateTime.ofInstant(start, ZoneOffset.UTC));
                agendaModel.setEnded(LocalDateTime.ofInstant(end, ZoneOffset.UTC));
                agendaModel.setAgendaStatusType(AgendaStatusType.FINISHED);

                agendaRepository.save(agendaModel);

                return StartAgendaResponse
                        .builder()
                        .code(agendaModel.getCode())
                        .started(agendaModel.getStarted())
                        .ended(agendaModel.getEnded())
                        .status(agendaModel.getAgendaStatusType().name())
                        .build();
            });
            future.isDone();
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("AgendaController.startTimer -> An error occurred during the voting period. " +
                    "Message: {}, Cause: {}", e.getMessage(), e.getCause());
            throw new InternalErrorException(ERROR_VOTING_FLOW);
        }
    }
}
