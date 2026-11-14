package com.nidus.dashboard.web;

import com.nidus.dashboard.model.Modality;
import com.nidus.dashboard.model.Sex;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StudyForm {

    @NotBlank
    @Size(max = 16)
    private String accession;

    @NotBlank
    @Size(max = 16)
    private String patientCode;

    @NotNull
    @Min(18)
    @Max(110)
    private Integer age;

    @NotNull
    private Sex sex;

    @NotNull
    private Modality modality;

    @NotBlank
    @Size(max = 24)
    private String sequenceName;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("72.0")
    private Double onsetHours;

    @NotNull
    @Min(0)
    @Max(42)
    private Integer nihss;

    @Size(max = 80)
    private String territory;

    @Min(0)
    @Max(10)
    private Integer aspects;

    @Size(max = 1000)
    private String findings;

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

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }
}
