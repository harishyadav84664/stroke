package com.nidus.dashboard.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ModelController {

    public record Metric(String label, String value, String note) {
    }

    public record Step(String index, String title, String body) {
    }

    @GetMapping("/model")
    public String model(Model model) {
        model.addAttribute("active", "model");
        model.addAttribute("metrics", List.of(
                new Metric("AUC", "0.94", "Placeholder figure for the interface"),
                new Metric("Sensitivity", "0.91", "Ischemic and hemorrhagic cases, pooled"),
                new Metric("Specificity", "0.88", "Studies with no acute stroke"),
                new Metric("Held-out set", "1,240", "Replace with your evaluation count")
        ));
        model.addAttribute("steps", List.of(
                new Step("01", "Intake", "NCCT, DWI, or CTA lands on the worklist with onset time and NIHSS."),
                new Step("02", "Preparation", "A real pipeline would orient, resample, and normalize the volume before inference."),
                new Step("03", "Classification", "The model assigns ischemic stroke, hemorrhagic stroke, or no acute stroke, plus a confidence."),
                new Step("04", "Localization", "A second head marks the likely vascular territory for the reader."),
                new Step("05", "Review", "A person accepts or sets aside the result. The model does not sign the report.")
        ));
        return "model";
    }
}
