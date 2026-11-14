package com.nidus.dashboard.web;

import com.nidus.dashboard.model.Prediction;
import com.nidus.dashboard.model.ReviewStatus;
import com.nidus.dashboard.model.Study;
import com.nidus.dashboard.service.StudyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class StudyApiController {

    public record FindingsUpdate(String findings) {}

    private final StudyService studies;

    public StudyApiController(StudyService studies) {
        this.studies = studies;
    }

    @GetMapping("/stats")
    public StudyService.Board getStats() {
        return studies.board();
    }

    @GetMapping("/studies")
    public List<Study> listStudies(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Prediction prediction,
            @RequestParam(required = false) ReviewStatus status
    ) {
        return studies.list(q, prediction, status);
    }

    @GetMapping("/studies/{id}")
    public Study getStudy(@PathVariable Long id) {
        return studies.get(id);
    }

    @PostMapping("/studies")
    public ResponseEntity<Study> createStudy(@Valid @RequestBody StudyForm form) {
        if (form.getAccession() != null && studies.accessionTaken(form.getAccession())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Accession already exists");
        }
        Study created = studies.register(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/studies/{id}/score")
    public Study scoreStudy(@PathVariable Long id) {
        return studies.score(id);
    }

    @PostMapping("/studies/{id}/review")
    public Study reviewStudy(@PathVariable Long id) {
        return studies.review(id);
    }

    @PostMapping("/studies/{id}/reopen")
    public Study reopenStudy(@PathVariable Long id) {
        return studies.reopen(id);
    }

    @PutMapping("/studies/{id}/findings")
    public Study updateFindings(@PathVariable Long id, @RequestBody FindingsUpdate body) {
        return studies.updateFindings(id, body != null ? body.findings() : null);
    }

    @DeleteMapping("/studies/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudy(@PathVariable Long id) {
        studies.delete(id);
    }
}
