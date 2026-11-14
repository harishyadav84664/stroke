package com.nidus.dashboard.web;

import com.nidus.dashboard.model.Modality;
import com.nidus.dashboard.model.Prediction;
import com.nidus.dashboard.model.ReviewStatus;
import com.nidus.dashboard.model.Sex;
import com.nidus.dashboard.model.Study;
import com.nidus.dashboard.service.StudyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest({DashboardController.class, StudyController.class, ModelController.class})
class StudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudyService studyService;

    @Test
    void dashboardPageLoads() throws Exception {
        StudyService.Board board = new StudyService.Board(10, 6, 4, 8, 85, 50, 20, 30, List.of());
        when(studyService.board()).thenReturn(board);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("board"));
    }

    @Test
    void studiesListPageLoads() throws Exception {
        when(studyService.list(any(), any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/studies"))
                .andExpect(status().isOk())
                .andExpect(view().name("studies"))
                .andExpect(model().attributeExists("studies"));
    }

    @Test
    void studyDetailPageLoads() throws Exception {
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
        s.setPrediction(Prediction.ISCHEMIC);

        when(studyService.get(1L)).thenReturn(s);

        mockMvc.perform(get("/studies/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("study"))
                .andExpect(model().attributeExists("study"));
    }

    @Test
    void newStudyFormPageLoads() throws Exception {
        mockMvc.perform(get("/studies/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("study-form"))
                .andExpect(model().attributeExists("form"));
    }

    @Test
    void modelExplanationPageLoads() throws Exception {
        mockMvc.perform(get("/model"))
                .andExpect(status().isOk())
                .andExpect(view().name("model"))
                .andExpect(model().attributeExists("metrics", "steps"));
    }

    @Test
    void scoreStudyRedirects() throws Exception {
        mockMvc.perform(post("/studies/1/score"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/studies/1"));
    }
}
