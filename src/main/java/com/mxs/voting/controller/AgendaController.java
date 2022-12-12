package com.mxs.voting.controller;

import com.mxs.voting.request.RegisterAgendaRequest;
import com.mxs.voting.request.StartAgendaRequest;
import com.mxs.voting.response.RegisterAgendaResponse;
import com.mxs.voting.response.StartAgendaResponse;
import com.mxs.voting.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static com.mxs.voting.constant.UriConstant.AGENDA;

@Controller
@RequestMapping(value = AGENDA)
@Tag(name = "Agenda Controller", description = "Controller responsible for operations related to the agenda")
public class AgendaController {

    private static final Logger logger = LoggerFactory.getLogger(AgendaController.class);

    @Autowired
    private AgendaService agendaService;

    @Operation(summary = "Create an agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agenda registered successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterAgendaResponse.class))})})
    @PostMapping
    public ResponseEntity<RegisterAgendaResponse> registerAgenda(@Valid @RequestBody RegisterAgendaRequest registerAgendaRequest) {
        logger.info("AgendaController.registerAgenda -> registerAgendaRequest: {}", registerAgendaRequest);
        RegisterAgendaResponse registerAgendaResponse = agendaService.registerAgenda(registerAgendaRequest);
        logger.info("AgendaController.registerAgenda -> registerAgendaResponse: {}", registerAgendaResponse);
        return new ResponseEntity<>(registerAgendaResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Start an agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agenda started and ended successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StartAgendaResponse.class))})})
    @PutMapping
    public ResponseEntity<StartAgendaResponse> startAgenda(@Valid @RequestBody StartAgendaRequest startAgendaRequest) {
        logger.info("AgendaController.startAgenda -> startAgendaRequest: {}", startAgendaRequest);
        StartAgendaResponse startAgendaResponse = agendaService.startAgenda(startAgendaRequest);
        logger.info("AgendaController.startAgenda -> startAgendaResponse: {}", startAgendaResponse);
        return new ResponseEntity<>(startAgendaResponse, HttpStatus.OK);
    }
}