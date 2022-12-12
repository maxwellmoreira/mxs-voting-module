package com.mxs.voting.repository;

import com.mxs.voting.model.VoteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<VoteModel, Long> {
    @Query("SELECT COUNT(vm) FROM VoteModel vm WHERE 1=1 " +
            " AND vm.agendaModel.code = ?1 " +
            " AND vm.votingOptionModel.code = ?2 " +
            " AND vm.status = ?3")
    int countVotesByAgenda(String agendaCode, String votingOptionCode, String voteStatus);
}