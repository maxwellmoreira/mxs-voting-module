package com.mxs.voting.service;

import com.mxs.voting.model.AgendaModel;
import com.mxs.voting.repository.AgendaRepository;
import com.mxs.voting.request.RegisterAgendaRequest;
import com.mxs.voting.request.StartAgendaRequest;
import com.mxs.voting.response.RegisterAgendaResponse;
import com.mxs.voting.response.StartAgendaResponse;
import com.mxs.voting.type.StatusType;
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

import static java.lang.Thread.sleep;

@Service
public class AgendaService {
    private static final Logger logger = LoggerFactory.getLogger(AgendaService.class);
    private static final long DEFAULT_DURATION = 1;
    private static final long THOUSAND = 1000;
    private static final long SIXTY = 60;
    @Autowired
    private AgendaRepository agendaRepository;

    public RegisterAgendaResponse registerAgenda(RegisterAgendaRequest registerAgendaRequest) {
        var duration = registerAgendaRequest.getDuration();

        if (registerAgendaRequest.getDuration() <= 0) {
            duration = DEFAULT_DURATION;
            logger.info("AgendaController.registerAgenda -> default duration: {}", DEFAULT_DURATION);
        }

        AgendaModel agendaModel =
                AgendaModel.builder()
                        .title(registerAgendaRequest.getTitle()).description(registerAgendaRequest.getDescription())
                        .duration(duration).statusType(StatusType.CREATED)
                        .build();
        AgendaModel agendaModelCreated = agendaRepository.save(agendaModel);

        logger.info("AgendaController.registerAgenda -> agendaModelCreated: {}", agendaModelCreated);

        return RegisterAgendaResponse.builder().code(agendaModelCreated.getCode()).build();
    }

    public StartAgendaResponse startAgenda(StartAgendaRequest startAgendaRequest) {
        Optional<AgendaModel> agendaModelOptional = agendaRepository.findByCodeEquals(startAgendaRequest.getCode());
        logger.info("AgendaController.startAgenda -> agendaModelOptional: {}", agendaModelOptional);
        return agendaModelOptional.map(
                agenda -> {
                    agenda.setStatusType(StatusType.VOTING);
                    agendaRepository.save(agenda);
                    return startTimer(agenda);
                }).get();
    }

    private StartAgendaResponse startTimer(AgendaModel agendaModel) {
        try {
            final CompletableFuture<StartAgendaResponse> future = CompletableFuture.supplyAsync(() -> {

                long milliseconds = (agendaModel.getDuration() * THOUSAND) * SIXTY;

                Instant start = Instant.now();

                logger.info("AgendaController.startTimer -> time started: {}", start);

                try {
                    sleep(milliseconds);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Instant end = Instant.now();

                logger.info("AgendaController.startTimer -> time ended: {}", end);

                agendaModel.setStarted(LocalDateTime.ofInstant(start, ZoneOffset.UTC));
                agendaModel.setEnded(LocalDateTime.ofInstant(end, ZoneOffset.UTC));
                agendaModel.setStatusType(StatusType.FINISHED);
                agendaRepository.save(agendaModel);

                return StartAgendaResponse
                        .builder()
                        .code(agendaModel.getCode())
                        .started(agendaModel.getStarted())
                        .ended(agendaModel.getEnded())
                        .status(agendaModel.getStatusType().name())
                        .build();
            });
            future.isDone();
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
