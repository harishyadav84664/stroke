package com.nidus.dashboard.web;

import com.nidus.dashboard.service.StudyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final StudyService studies;

    public DashboardController(StudyService studies) {
        this.studies = studies;
    }

    @GetMapping("/")
    public String board(Model model) {
        model.addAttribute("board", studies.board());
        model.addAttribute("active", "board");
        return "index";
    }
}
