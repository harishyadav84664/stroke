package com.nidus.dashboard.service;

import com.nidus.dashboard.model.Prediction;
import com.nidus.dashboard.model.ReviewStatus;
import com.nidus.dashboard.model.Study;
import com.nidus.dashboard.repo.StudyRepository;
import com.nidus.dashboard.scoring.DemoScorer;
import com.nidus.dashboard.web.StudyForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudyService {

    public record Board(
            long total,
            long suspected,
            long queued,
            long scored,
            int meanConfidence,
            int ischemicPct,
            int hemorrhagicPct,
            int clearPct,
            List<Study> recent
    ) {
    }

    private final StudyRepository studies;

    public StudyService(StudyRepository studies) {
        this.studies = studies;
    }

    @Transactional(readOnly = true)
    public Board board() {
        long total = studies.count();
        long ischemic = studies.countByPrediction(Prediction.ISCHEMIC);
        long hemorrhagic = studies.countByPrediction(Prediction.HEMORRHAGIC);
        long clear = studies.countByPrediction(Prediction.NO_STROKE);
        long scored = studies.countByPredictionIsNotNull();
        int mean = 0;
        if (scored > 0) {
            double sum = studies.findAll().stream()
                    .filter(study -> study.getConfidence() != null)
                    .mapToDouble(Study::getConfidence)
                    .sum();
            mean = (int) Math.round((sum / scored) * 100);
        }
        List<Study> recent = studies.findAllByOrderByAcquiredAtDesc().stream().limit(6).toList();
        return new Board(
                total,
                ischemic + hemorrhagic,
                studies.countByStatus(ReviewStatus.QUEUED),
                scored,
                mean,
                percent(ischemic, total),
                percent(hemorrhagic, total),
                percent(clear, total),
                recent
        );
    }

    @Transactional(readOnly = true)
    public List<Study> list(Prediction prediction, ReviewStatus status) {
        return list(null, prediction, status);
    }

    @Transactional(readOnly = true)
    public List<Study> list(String query, Prediction prediction, ReviewStatus status) {
        String cleanQuery = (query != null && !query.isBlank()) ? query.trim().toUpperCase() : null;
        return studies.findAllByOrderByAcquiredAtDesc().stream()
                .filter(study -> cleanQuery == null
                        || (study.getAccession() != null && study.getAccession().toUpperCase().contains(cleanQuery))
                        || (study.getPatientCode() != null && study.getPatientCode().toUpperCase().contains(cleanQuery))
                        || (study.getTerritory() != null && study.getTerritory().toUpperCase().contains(cleanQuery)))
                .filter(study -> prediction == null || study.getPrediction() == prediction)
                .filter(study -> status == null || study.getStatus() == status)
                .toList();
    }

    @Transactional(readOnly = true)
    public Study get(Long id) {
        return studies.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not on the worklist"));
    }

    @Transactional(readOnly = true)
    public boolean accessionTaken(String accession) {
        return studies.existsByAccession(accession.trim());
    }

    @Transactional
    public Study register(StudyForm form) {
        Study study = new Study();
        study.setAccession(form.getAccession().trim().toUpperCase());
        study.setPatientCode(form.getPatientCode().trim().toUpperCase());
        study.setAge(form.getAge());
        study.setSex(form.getSex());
        study.setModality(form.getModality());
        study.setSequenceName(form.getSequenceName().trim().toUpperCase());
        study.setAcquiredAt(LocalDateTime.now());
        study.setOnsetHours(form.getOnsetHours());
        study.setNihss(form.getNihss());
        study.setTerritory(blankToNull(form.getTerritory()));
        study.setAspects(form.getAspects());
        study.setFindings(blankToNull(form.getFindings()));
        study.setStatus(ReviewStatus.QUEUED);
        return studies.save(study);
    }

    @Transactional
    public Study score(Long id) {
        Study study = get(id);
        DemoScorer.Score score = DemoScorer.score(study.getNihss(), study.getModality());
        study.setPrediction(score.prediction());
        study.setConfidence(score.confidence());
        study.setTerritory(score.territory());
        study.setAspects(score.aspects());
        study.setScoredBy("NIDUS-v1-Scorer");
        return study;
    }

    @Transactional
    public Study review(Long id) {
        Study study = get(id);
        study.setStatus(ReviewStatus.REVIEWED);
        return study;
    }

    @Transactional
    public Study reopen(Long id) {
        Study study = get(id);
        study.setStatus(ReviewStatus.QUEUED);
        return study;
    }

    @Transactional
    public Study updateFindings(Long id, String findings) {
        Study study = get(id);
        study.setFindings(blankToNull(findings));
        return study;
    }

    @Transactional
    public void delete(Long id) {
        Study study = get(id);
        studies.delete(study);
    }

    private static int percent(long part, long total) {
        if (total == 0) {
            return 0;
        }
        return (int) Math.round(part * 100.0 / total);
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
