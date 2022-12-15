package com.mxs.voting.controller;

import com.mxs.voting.request.CreateVotingOptionRequest;
import com.mxs.voting.response.CreateVotingOptionResponse;
import com.mxs.voting.response.VoteResponse;
import com.mxs.voting.service.VotingOptionService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static com.mxs.voting.constant.UriConstant.VOTING_OPTION;

@Controller
@RequestMapping(value = VOTING_OPTION)
@Tag(name = "Voting Option Controller", description = "Controller responsible for operations related to voting options")
public class VotingOptionController {
    private static final Logger logger = LoggerFactory.getLogger(VotingOptionController.class);
    @Autowired
    private VotingOptionService votingOptionService;

    @Operation(summary = "Create voting option")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voting option created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VoteResponse.class))})})
    @PostMapping
    public ResponseEntity<CreateVotingOptionResponse> createVotingOption(@Valid @RequestBody CreateVotingOptionRequest createVotingOptionRequest) {
        logger.info("VotingOptionController.createVotingOption -> createVotingOptionRequest: {}", createVotingOptionRequest);
        CreateVotingOptionResponse createVotingOptionResponse = votingOptionService.createVotingOption(createVotingOptionRequest);
        logger.info("VotingOptionController.createVotingOption -> createVotingOptionResponse: {}", createVotingOptionResponse);
        return new ResponseEntity<>(createVotingOptionResponse, HttpStatus.CREATED);
    }
}
