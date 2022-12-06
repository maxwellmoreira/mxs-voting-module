package com.mxs.voting.repository;

import com.mxs.voting.model.AgendaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<AgendaModel, Long> {
    Optional<AgendaModel> findByCodeEquals(String code);
}
