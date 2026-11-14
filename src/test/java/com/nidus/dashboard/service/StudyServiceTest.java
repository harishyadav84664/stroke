package com.nidus.dashboard.service;

import com.nidus.dashboard.model.Modality;
import com.nidus.dashboard.model.Prediction;
import com.nidus.dashboard.model.ReviewStatus;
import com.nidus.dashboard.model.Sex;
import com.nidus.dashboard.model.Study;
import com.nidus.dashboard.repo.StudyRepository;
import com.nidus.dashboard.web.StudyForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class StudyServiceTest {

    @Autowired
    private StudyRepository repository;

    private StudyService service;

    @BeforeEach
    void setUp() {
        service = new StudyService(repository);
    }

    @Test
    void registerAndGetStudy() {
        StudyForm form = new StudyForm();
        form.setAccession("NID-TEST01");
        form.setPatientCode("P-TEST99");
        form.setAge(58);
        form.setSex(Sex.MALE);
        form.setModality(Modality.MRI);
        form.setSequenceName("DWI");
        form.setOnsetHours(2.0);
        form.setNihss(10);
        form.setFindings("Initial test note");

        Study saved = service.register(form);
        assertNotNull(saved.getId());
        assertEquals("NID-TEST01", saved.getAccession());
        assertEquals(ReviewStatus.QUEUED, saved.getStatus());

        Study fetched = service.get(saved.getId());
        assertEquals("P-TEST99", fetched.getPatientCode());
        assertTrue(service.accessionTaken("NID-TEST01"));
    }

    @Test
    void scoreAndReviewLifecycle() {
        StudyForm form = new StudyForm();
        form.setAccession("NID-TEST02");
        form.setPatientCode("P-TEST02");
        form.setAge(65);
        form.setSex(Sex.FEMALE);
        form.setModality(Modality.CT);
        form.setSequenceName("NCCT");
        form.setOnsetHours(1.5);
        form.setNihss(14);

        Study saved = service.register(form);
        Study scored = service.score(saved.getId());
        assertEquals(Prediction.HEMORRHAGIC, scored.getPrediction());
        assertNotNull(scored.getConfidence());

        Study reviewed = service.review(saved.getId());
        assertEquals(ReviewStatus.REVIEWED, reviewed.getStatus());

        Study reopened = service.reopen(saved.getId());
        assertEquals(ReviewStatus.QUEUED, reopened.getStatus());
    }

    @Test
    void searchAndFilterStudies() {
        StudyForm f1 = new StudyForm();
        f1.setAccession("NID-ALPHA");
        f1.setPatientCode("P-ALPHA");
        f1.setAge(70);
        f1.setSex(Sex.MALE);
        f1.setModality(Modality.MRI);
        f1.setSequenceName("DWI");
        f1.setOnsetHours(3.0);
        f1.setNihss(2); // will score NO_STROKE
        service.register(f1);

        StudyForm f2 = new StudyForm();
        f2.setAccession("NID-BETA");
        f2.setPatientCode("P-BETA");
        f2.setAge(60);
        f2.setSex(Sex.FEMALE);
        f2.setModality(Modality.MRI);
        f2.setSequenceName("FLAIR");
        f2.setOnsetHours(4.0);
        f2.setNihss(12);
        service.register(f2);

        List<Study> foundByQuery = service.list("alpha", null, null);
        assertEquals(1, foundByQuery.size());
        assertEquals("NID-ALPHA", foundByQuery.get(0).getAccession());

        List<Study> allQueued = service.list(null, null, ReviewStatus.QUEUED);
        assertEquals(2, allQueued.size());
    }

    @Test
    void updateFindingsAndDelete() {
        StudyForm form = new StudyForm();
        form.setAccession("NID-TEST03");
        form.setPatientCode("P-TEST03");
        form.setAge(50);
        form.setSex(Sex.MALE);
        form.setModality(Modality.MRI);
        form.setSequenceName("DWI");
        form.setOnsetHours(1.0);
        form.setNihss(5);

        Study saved = service.register(form);
        service.updateFindings(saved.getId(), "Updated radiologist impressions");

        Study fetched = service.get(saved.getId());
        assertEquals("Updated radiologist impressions", fetched.getFindings());

        service.delete(saved.getId());
        assertFalse(service.accessionTaken("NID-TEST03"));
    }
}
