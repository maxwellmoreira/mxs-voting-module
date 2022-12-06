package com.mxs.voting.consumer;

import com.mxs.voting.dto.VoteDto;
import com.mxs.voting.model.AgendaModel;
import com.mxs.voting.model.VoteModel;
import com.mxs.voting.repository.AgendaRepository;
import com.mxs.voting.repository.VoteRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.mxs.voting.constant.QueueConstant.VOTE;

@Component
public class VoteConsumer {
    @Autowired
    public AgendaRepository agendaRepository;
    @Autowired
    public VoteRepository voteRepository;

    @RabbitListener(queues = VOTE)
    public void consume(VoteDto voteDto) {
        Optional<AgendaModel> agendaModelOptional = agendaRepository.findByCodeEquals(voteDto.getAgendaCode());
        agendaModelOptional.ifPresent(
                agendaModel -> {
                    VoteModel voteModel = new VoteModel();
                    voteModel.setCode(voteDto.getVoteCode());
                    voteModel.setVoteType(voteDto.getVoteType());
                    voteModel.setAgendaModel(agendaModel);
                    voteRepository.save(voteModel);
        });
    }
}
