package com.mxs.voting.service;

import com.mxs.voting.model.AgendaModel;
import com.mxs.voting.model.VotingOptionModel;
import com.mxs.voting.repository.AgendaRepository;
import com.mxs.voting.repository.VotingOptionRepository;
import com.mxs.voting.request.CreateVotingOptionRequest;
import com.mxs.voting.response.CreateVotingOptionResponse;
import com.mxs.voting.type.AgendaStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotingOptionService {
    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private VotingOptionRepository votingOptionRepository;

    public CreateVotingOptionResponse createVotingOption(CreateVotingOptionRequest createVotingOptionRequest) {
        AgendaModel agendaModel =
                agendaRepository.findByCodeAndStatusAgendaEquals(createVotingOptionRequest.getAgendaCode(),
                        AgendaStatusType.CREATED.getCode()).orElseThrow(IllegalArgumentException::new);
        VotingOptionModel votingOptionModel =
                VotingOptionModel.builder().description(createVotingOptionRequest.getDescription()).agendaModel(agendaModel).build();
        VotingOptionModel response = votingOptionRepository.save(votingOptionModel);
        return CreateVotingOptionResponse.builder().code(response.getCode()).build();
    }
}
