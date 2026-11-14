package com.nidus.dashboard.service;

import com.nidus.dashboard.model.Modality;
import com.nidus.dashboard.model.Prediction;
import com.nidus.dashboard.model.ReviewStatus;
import com.nidus.dashboard.model.Sex;
import com.nidus.dashboard.model.Study;
import com.nidus.dashboard.repo.StudyRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StudySeeder implements ApplicationRunner {

    private final StudyRepository studies;

    public StudySeeder(StudyRepository studies) {
        this.studies = studies;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (studies.count() > 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        studies.save(study("NID-24011", "P-1842", 67, Sex.MALE, Modality.MRI, "DWI",
                now.minusHours(6), 2.5, 14, Prediction.ISCHEMIC, 0.91, "Left MCA", 6,
                ReviewStatus.REVIEWED, "seed",
                "Restricted diffusion in the left middle-cerebral territory. Demonstration case."));
        studies.save(study("NID-24018", "P-1904", 54, Sex.FEMALE, Modality.CT, "NCCT",
                now.minusHours(5), 1.5, 11, Prediction.HEMORRHAGIC, 0.88, "Right basal ganglia", 8,
                ReviewStatus.REVIEWED, "seed",
                "Hyperdense focus in the right basal ganglia. Demonstration case."));
        studies.save(study("NID-24022", "P-2011", 72, Sex.FEMALE, Modality.MRI, "FLAIR",
                now.minusHours(4), 8.0, 2, Prediction.NO_STROKE, 0.84, "No acute territory", null,
                ReviewStatus.QUEUED, "seed",
                "No restricted diffusion. Migraine mimic kept on the worklist for review."));
        studies.save(study("NID-24027", "P-1760", 81, Sex.MALE, Modality.CT, "NCCT",
                now.minusHours(3), 4.0, 9, Prediction.ISCHEMIC, 0.79, "Left lenticulostriate", 7,
                ReviewStatus.REVIEWED, "seed",
                "Subtle hypoattenuation in the left lentiform nucleus. Demonstration case."));
        studies.save(study("NID-24031", "P-2108", 46, Sex.MALE, Modality.MRI, "DWI",
                now.minusHours(2).minusMinutes(20), 3.0, 6, Prediction.ISCHEMIC, 0.86, "Right PCA", 9,
                ReviewStatus.QUEUED, "seed",
                "Small occipital restriction on the right. Demonstration case."));
        studies.save(study("NID-24036", "P-1888", 59, Sex.FEMALE, Modality.CTA, "CTA",
                now.minusHours(2), 1.0, 1, Prediction.NO_STROKE, 0.77, "No acute territory", null,
                ReviewStatus.REVIEWED, "seed",
                "Vessels opacify. No large-vessel cutoff on this demonstration study."));
        studies.save(study("NID-24040", "P-2220", 70, Sex.MALE, Modality.CT, "NCCT",
                now.minusMinutes(70), 0.8, 16, Prediction.HEMORRHAGIC, 0.93, "Left thalamus", 5,
                ReviewStatus.QUEUED, "seed",
                "Left thalamic hemorrhage with local mass effect. Demonstration case."));
        studies.save(study("NID-24044", "P-1655", 63, Sex.FEMALE, Modality.MRI, "DWI",
                now.minusMinutes(25), 5.5, 8, Prediction.ISCHEMIC, 0.82, "Right MCA superior division", 7,
                ReviewStatus.QUEUED, "seed",
                "Superior-division restriction. Waiting for a reader. Demonstration case."));
    }

    private static Study study(
            String accession,
            String patientCode,
            int age,
            Sex sex,
            Modality modality,
            String sequence,
            LocalDateTime acquiredAt,
            double onsetHours,
            int nihss,
            Prediction prediction,
            double confidence,
            String territory,
            Integer aspects,
            ReviewStatus status,
            String scoredBy,
            String findings
    ) {
        Study study = new Study();
        study.setAccession(accession);
        study.setPatientCode(patientCode);
        study.setAge(age);
        study.setSex(sex);
        study.setModality(modality);
        study.setSequenceName(sequence);
        study.setAcquiredAt(acquiredAt);
        study.setOnsetHours(onsetHours);
        study.setNihss(nihss);
        study.setPrediction(prediction);
        study.setConfidence(confidence);
        study.setTerritory(territory);
        study.setAspects(aspects);
        study.setStatus(status);
        study.setScoredBy(scoredBy);
        study.setFindings(findings);
        return study;
    }
}
