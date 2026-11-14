package com.nidus.dashboard.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ShellAdvice {

    @ModelAttribute("appName")
    public String appName() {
        return "NIDUS";
    }
}
