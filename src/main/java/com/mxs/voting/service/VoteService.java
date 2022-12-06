package com.mxs.voting.service;

import com.mxs.voting.dto.VoteDto;
import com.mxs.voting.request.VoteRequest;
import com.mxs.voting.response.VoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.mxs.voting.constant.QueueConstant.VOTE;

@Service
public class VoteService {
    private static final Logger logger = LoggerFactory.getLogger(VoteService.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public VoteResponse vote(VoteRequest voteRequest) {
        UUID uuid = UUID.randomUUID();
        logger.info("VoteService.vote -> uuid: {}", uuid);
        VoteDto voteDto =
                VoteDto.builder()
                        .voteCode(uuid.toString())
                        .agendaCode(voteRequest.getAgendaCode())
                        .voteType(voteRequest.getVoteType())
                        .build();
        rabbitTemplate.convertAndSend(VOTE, voteDto);
        logger.info("VoteService.vote -> message sent successfully! queue: {}, message: {}", VOTE, voteDto);
        return VoteResponse.builder().code(voteDto.getVoteCode()).build();
    }
}
