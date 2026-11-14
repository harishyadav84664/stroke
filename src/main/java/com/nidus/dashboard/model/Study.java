package com.nidus.dashboard.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "studies")
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String accession;

    @Column(nullable = false, length = 16)
    private String patientCode;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private Modality modality;

    @Column(nullable = false, length = 24)
    private String sequenceName;

    @Column(nullable = false)
    private LocalDateTime acquiredAt;

    @Column(nullable = false)
    private Double onsetHours;

    @Column(nullable = false)
    private Integer nihss;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private Prediction prediction;

    private Double confidence;

    @Column(length = 80)
    private String territory;

    private Integer aspects;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ReviewStatus status;

    @Column(length = 32)
    private String scoredBy;

    @Column(length = 1000)
    private String findings;

    public Long getId() {
        return id;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getPatientCode() {
        return patientCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public LocalDateTime getAcquiredAt() {
        return acquiredAt;
    }

    public void setAcquiredAt(LocalDateTime acquiredAt) {
        this.acquiredAt = acquiredAt;
    }

    public Double getOnsetHours() {
        return onsetHours;
    }

    public void setOnsetHours(Double onsetHours) {
        this.onsetHours = onsetHours;
    }

    public Integer getNihss() {
        return nihss;
    }

    public void setNihss(Integer nihss) {
        this.nihss = nihss;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public void setPrediction(Prediction prediction) {
        this.prediction = prediction;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public Integer getAspects() {
        return aspects;
    }

    public void setAspects(Integer aspects) {
        this.aspects = aspects;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public String getScoredBy() {
        return scoredBy;
    }

    public void setScoredBy(String scoredBy) {
        this.scoredBy = scoredBy;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }
}
