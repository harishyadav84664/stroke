package com.nidus.dashboard.repo;

import com.nidus.dashboard.model.Prediction;
import com.nidus.dashboard.model.ReviewStatus;
import com.nidus.dashboard.model.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {

    boolean existsByAccession(String accession);

    List<Study> findAllByOrderByAcquiredAtDesc();

    long countByPrediction(Prediction prediction);

    long countByStatus(ReviewStatus status);

    long countByPredictionIsNotNull();
}
