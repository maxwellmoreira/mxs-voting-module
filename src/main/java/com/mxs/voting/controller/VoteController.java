package com.mxs.voting.controller;

import com.mxs.voting.request.VoteRequest;
import com.mxs.voting.response.VoteResponse;
import com.mxs.voting.service.VoteService;
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

import static com.mxs.voting.constant.UriConstant.VOTE;

@Controller
@RequestMapping(value = VOTE)
public class VoteController {

    private static final Logger logger = LoggerFactory.getLogger(VoteController.class);

    @Autowired
    private VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteResponse> vote(@Valid @RequestBody VoteRequest voteRequest) {
        logger.info("VoteController.vote -> voteRequest: {}", voteRequest);
        VoteResponse voteResponse = voteService.vote(voteRequest);
        logger.info("VoteController.vote -> voteResponse: {}", voteResponse);
        return new ResponseEntity<>(voteResponse, HttpStatus.CREATED);
    }
}
