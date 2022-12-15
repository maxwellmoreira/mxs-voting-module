package com.mxs.voting.controller;

import com.mxs.voting.request.VoteCountingRequest;
import com.mxs.voting.request.VoteRequest;
import com.mxs.voting.response.VoteCountingResponse;
import com.mxs.voting.response.VoteResponse;
import com.mxs.voting.service.VoteService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.mxs.voting.constant.UriConstant.VOTE;

@Controller
@RequestMapping(value = VOTE)
@Tag(name = "Vote Controller", description = "Controller responsible for operations related to voting")
public class VoteController {
    private static final Logger logger = LoggerFactory.getLogger(VoteController.class);
    @Autowired
    private VoteService voteService;

    @Operation(summary = "Vote for a specific agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vote registered successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VoteResponse.class))})})
    @PostMapping
    public ResponseEntity<VoteResponse> vote(@Valid @RequestBody VoteRequest voteRequest) {
        logger.info("VoteController.vote -> voteRequest: {}", voteRequest);
        VoteResponse voteResponse = voteService.vote(voteRequest);
        logger.info("VoteController.vote -> voteResponse: {}", voteResponse);
        return new ResponseEntity<>(voteResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Vote count and agenda result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote count and agenda result returned successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VoteResponse.class))})})
    @PutMapping("/vote-counting")
    public ResponseEntity<VoteCountingResponse> countVotes(@Valid @RequestBody VoteCountingRequest voteCountingRequest) {
        logger.info("VoteController.countVotes -> voteCountingRequest: {}", voteCountingRequest);
        VoteCountingResponse voteCountingResponse = voteService.countVotes(voteCountingRequest);
        logger.info("VoteController.countVotes -> voteCountingResponse: {}", voteCountingResponse);
        return new ResponseEntity<>(voteCountingResponse, HttpStatus.OK);
    }
}
