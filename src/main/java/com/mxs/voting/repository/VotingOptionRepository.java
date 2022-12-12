package com.mxs.voting.repository;

import com.mxs.voting.model.VotingOptionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotingOptionRepository extends JpaRepository<VotingOptionModel, Long> {
    Optional<VotingOptionModel> findByCodeEquals(String code);

    @Query("SELECT vom FROM VotingOptionModel vom WHERE vom.agendaModel.code = ?1 and vom.status = ?2")
    List<VotingOptionModel> findByAgendaEquals(String code, String status);
}
