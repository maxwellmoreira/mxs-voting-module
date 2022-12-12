package com.mxs.voting.consumer;

import com.mxs.voting.dto.VoteDto;
import com.mxs.voting.model.AgendaModel;
import com.mxs.voting.model.VoteModel;
import com.mxs.voting.model.VotingOptionModel;
import com.mxs.voting.repository.AgendaRepository;
import com.mxs.voting.repository.VoteRepository;
import com.mxs.voting.repository.VotingOptionRepository;
import com.mxs.voting.type.AgendaStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.mxs.voting.constant.QueueConstant.VOTE;

@Component
public class VoteConsumer {
    private static final Logger logger = LoggerFactory.getLogger(VoteConsumer.class);
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private VotingOptionRepository votingOptionRepository;

    @RabbitListener(queues = VOTE)
    public void consume(VoteDto voteDto) {
        //TODO org.hibernate.LazyInitializationException: could not initialize proxy - no Session

        AgendaModel agendaModel = getAgendaModel(voteDto);
        VotingOptionModel votingOptionModel = getVotingOptionModel(voteDto);

        VoteModel voteModel = new VoteModel();
        voteModel.setCode(voteDto.getVoteCode());
        voteModel.setAgendaModel(agendaModel);
        voteModel.setVotingOptionModel(votingOptionModel);

        voteRepository.save(voteModel);
        logger.info("VoteConsumer.consume -> Message consumed successfully! queue: {}", VOTE);
    }

    private AgendaModel getAgendaModel(VoteDto voteDto) {
        return agendaRepository.findByCodeAndStatusAgendaEquals(voteDto.getAgendaCode(), AgendaStatusType.VOTING.getCode())
                .orElseThrow(IllegalArgumentException::new);
    }

    private VotingOptionModel getVotingOptionModel(VoteDto voteDto) {
        return votingOptionRepository.findByCodeEquals(voteDto.getVotingOptionCode())
                .orElseThrow(IllegalArgumentException::new);
    }
}
