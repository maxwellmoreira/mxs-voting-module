package com.mxs.voting.repository;

import com.mxs.voting.model.VoteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<VoteModel, Long> {
}