package com.mxs.voting.service;

import com.mxs.voting.dto.VoteCountingDto;
import com.mxs.voting.dto.VoteDto;
import com.mxs.voting.model.AgendaModel;
import com.mxs.voting.model.VotingOptionModel;
import com.mxs.voting.repository.AgendaRepository;
import com.mxs.voting.repository.VoteRepository;
import com.mxs.voting.repository.VotingOptionRepository;
import com.mxs.voting.request.VoteCountingRequest;
import com.mxs.voting.request.VoteRequest;
import com.mxs.voting.response.VoteCountingResponse;
import com.mxs.voting.response.VoteResponse;
import com.mxs.voting.type.AgendaStatusType;
import com.mxs.voting.type.StatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.mxs.voting.constant.QueueConstant.VOTE;

@Service
public class VoteService {
    private static final Logger logger = LoggerFactory.getLogger(VoteService.class);
    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private VotingOptionRepository votingOptionRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public VoteResponse vote(VoteRequest voteRequest) {
        UUID uuid = UUID.randomUUID();
        logger.info("VoteService.vote -> uuid: {}", uuid);
        VoteDto voteDto =
                VoteDto.builder()
                        .voteCode(uuid.toString())
                        .agendaCode(voteRequest.getAgendaCode())
                        .votingOptionCode(voteRequest.getVotingOptionCode())
                        .build();
        rabbitTemplate.convertAndSend(VOTE, voteDto);
        logger.info("VoteService.vote -> message sent successfully! queue: {}, voteDto: {}", VOTE, voteDto);
        return VoteResponse.builder().code(voteDto.getVoteCode()).build();
    }

    public VoteCountingResponse countVotes(VoteCountingRequest voteCountingRequest) {
        AgendaModel agendaModel = getAgendaByCode(voteCountingRequest);
        logger.info("VoteService.countVotes -> agendaModel: {}", agendaModel);
        List<VotingOptionModel> votingOptionModelList = getVotingOptionByAgenda(agendaModel);
        logger.info("VoteService.countVotes -> votingOptionModelList: {}", votingOptionModelList);
        List<VoteCountingDto> voteCountingDtoList = getVoteCountingDtoList(agendaModel, votingOptionModelList);
        logger.info("VoteService.countVotes -> voteCountingDtoList: {}", voteCountingDtoList);
        int totalAmountVotes = voteCountingDtoList.stream().map(VoteCountingDto::getAmountVotes).reduce(0, Integer::sum);
        logger.info("VoteService.countVotes -> totalAmountVotes: {}", totalAmountVotes);
        updateVoteCountingDtoList(voteCountingDtoList, totalAmountVotes);
        updateVotingOptionModelList(voteCountingDtoList, votingOptionModelList);
        updateAgenda(agendaModel, votingOptionModelList, totalAmountVotes);
        return VoteCountingResponse
                .builder()
                .agendaCode(agendaModel.getCode())
                .title(agendaModel.getTitle())
                .started(agendaModel.getStarted())
                .ended(agendaModel.getEnded())
                .totalAmountVotes(totalAmountVotes)
                .winnerCode(voteCountingDtoList.stream()
                        .filter(voteCountingDto -> voteCountingDto.getHasWon().equals(Boolean.TRUE))
                        .map(VoteCountingDto::getCode)
                        .findFirst()
                        .orElse(null))
                .winner(voteCountingDtoList.stream()
                        .filter(voteCountingDto -> voteCountingDto.getHasWon().equals(Boolean.TRUE))
                        .map(VoteCountingDto::getDescription)
                        .findFirst()
                        .orElse(null))
                .voteCountingDtoList(voteCountingDtoList)
                .build();
    }

    private AgendaModel getAgendaByCode(VoteCountingRequest voteCountingRequest) {
        return agendaRepository.findByCodeAndStatusAgendaEquals(
                voteCountingRequest.getAgendaCode(), AgendaStatusType.FINISHED.getCode()).get();
    }

    private List<VotingOptionModel> getVotingOptionByAgenda(AgendaModel agendaModel) {
        return votingOptionRepository.findByAgendaEquals(agendaModel.getCode(), StatusType.ACTIVE.getCode());
    }

    private List<VoteCountingDto> getVoteCountingDtoList(AgendaModel agendaModel, List<VotingOptionModel> votingOptionModelList) {
        //TODO perform only 1 query to the database
        return votingOptionModelList.stream().map(
                votingOptionModel -> {
                    var votes = countVotes(agendaModel, votingOptionModel);
                    logger.info("VoteService.getVoteCountingDtoList -> votingOptionModel: {}, votes: {}", votingOptionModel, votes);
                    return VoteCountingDto
                            .builder()
                            .code(votingOptionModel.getCode())
                            .description(votingOptionModel.getDescription())
                            .amountVotes(votes)
                            .build();
                }).sorted(Comparator.comparingInt(VoteCountingDto::getAmountVotes).reversed())
                .collect(Collectors.toList());
    }

    private int countVotes(AgendaModel agendaModel, VotingOptionModel votingOptionModel) {
        return voteRepository.countVotesByAgenda(agendaModel.getCode(), votingOptionModel.getCode(), StatusType.ACTIVE.getCode());
    }

    private void updateVoteCountingDtoList(List<VoteCountingDto> voteCountingDtoList, int totalAmountVotes) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        voteCountingDtoList.forEach(
                voteCountingDto -> {
                    voteCountingDto.setPosition(atomicInteger.getAndIncrement());
                    if (voteCountingDto.getPosition() == 1) {
                        voteCountingDto.setHasWon(Boolean.TRUE);
                    } else {
                        voteCountingDto.setHasWon(Boolean.FALSE);
                    }
                    var votesPercentage = (voteCountingDto.getAmountVotes() * 100) / totalAmountVotes;
                    voteCountingDto.setVotesPercentage(votesPercentage);
        });
    }

    private void updateVotingOptionModelList(List<VoteCountingDto> voteCountingDtoList, List<VotingOptionModel> votingOptionModelList) {
        voteCountingDtoList.forEach(voteCountingDto -> {
            VotingOptionModel votingOptionModel = votingOptionModelList.stream()
                    .filter(votingOption -> votingOption.getCode().equalsIgnoreCase(voteCountingDto.getCode()))
                    .findFirst()
                    .orElse(null);
            votingOptionModel.setAmountVotes(voteCountingDto.getAmountVotes());
            votingOptionModel.setPercentageVotes(voteCountingDto.getVotesPercentage());
            votingOptionModel.setPosition(voteCountingDto.getPosition());
            votingOptionModel.setHasWon(voteCountingDto.getHasWon());
        });
        votingOptionRepository.saveAll(votingOptionModelList);
    }

    private void updateAgenda(AgendaModel agendaModel, List<VotingOptionModel> votingOptionModelList, int totalAmountVotes) {
        VotingOptionModel votingOptionModel = votingOptionModelList.stream()
                .filter(votingOption -> votingOption.getHasWon().equals(Boolean.TRUE)).findFirst().orElseThrow(IllegalArgumentException::new);
        agendaModel.setTotalAmountVotes(totalAmountVotes);
        agendaModel.setWinner(votingOptionModel.getCode());
        agendaRepository.save(agendaModel);
    }
}
