package com.nidus.dashboard.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nidus.dashboard.model.Modality;
import com.nidus.dashboard.model.Prediction;
import com.nidus.dashboard.model.ReviewStatus;
import com.nidus.dashboard.model.Sex;
import com.nidus.dashboard.model.Study;
import com.nidus.dashboard.service.StudyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudyApiController.class)
class StudyApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudyService studyService;

    @Test
    void getStatsReturnsOk() throws Exception {
        StudyService.Board board = new StudyService.Board(10, 6, 4, 8, 85, 50, 20, 30, List.of());
        when(studyService.board()).thenReturn(board);

        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(10))
                .andExpect(jsonPath("$.meanConfidence").value(85));
    }

    @Test
    void listStudiesReturnsArray() throws Exception {
        Study s = new Study();
        s.setAccession("NID-24011");
        s.setPatientCode("P-1842");
        s.setAge(67);
        s.setSex(Sex.MALE);
        s.setModality(Modality.MRI);
        s.setSequenceName("DWI");
        s.setAcquiredAt(LocalDateTime.now());
        s.setOnsetHours(2.5);
        s.setNihss(14);
        s.setStatus(ReviewStatus.QUEUED);

        when(studyService.list(eq("NID"), eq(null), eq(null))).thenReturn(List.of(s));

        mockMvc.perform(get("/api/studies").param("q", "NID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accession").value("NID-24011"))
                .andExpect(jsonPath("$[0].patientCode").value("P-1842"));
    }

    @Test
    void createStudyReturnsCreated() throws Exception {
        StudyForm form = new StudyForm();
        form.setAccession("NID-API01");
        form.setPatientCode("P-API01");
        form.setAge(60);
        form.setSex(Sex.FEMALE);
        form.setModality(Modality.CT);
        form.setSequenceName("NCCT");
        form.setOnsetHours(1.0);
        form.setNihss(8);

        Study saved = new Study();
        saved.setAccession("NID-API01");
        saved.setPatientCode("P-API01");
        saved.setStatus(ReviewStatus.QUEUED);

        when(studyService.accessionTaken("NID-API01")).thenReturn(false);
        when(studyService.register(any(StudyForm.class))).thenReturn(saved);

        mockMvc.perform(post("/api/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accession").value("NID-API01"));
    }

    @Test
    void scoreStudyReturnsUpdatedStudy() throws Exception {
        Study scored = new Study();
        scored.setAccession("NID-API01");
        scored.setPrediction(Prediction.ISCHEMIC);
        scored.setConfidence(0.88);

        when(studyService.score(1L)).thenReturn(scored);

        mockMvc.perform(post("/api/studies/1/score"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prediction").value("ISCHEMIC"));
    }

    @Test
    void deleteStudyReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/studies/1"))
                .andExpect(status().isNoContent());
    }
}
