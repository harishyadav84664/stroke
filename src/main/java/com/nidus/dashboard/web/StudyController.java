package com.nidus.dashboard.web;

import com.nidus.dashboard.model.Modality;
import com.nidus.dashboard.model.Prediction;
import com.nidus.dashboard.model.ReviewStatus;
import com.nidus.dashboard.model.Sex;
import com.nidus.dashboard.service.StudyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudyController {

    private final StudyService studies;

    public StudyController(StudyService studies) {
        this.studies = studies;
    }

    @GetMapping("/studies")
    public String list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String prediction,
            @RequestParam(required = false) String status,
            Model model
    ) {
        Prediction call = parse(prediction, Prediction.class);
        ReviewStatus review = parse(status, ReviewStatus.class);
        model.addAttribute("studies", studies.list(q, call, review));
        model.addAttribute("q", q);
        model.addAttribute("prediction", call);
        model.addAttribute("status", review);
        model.addAttribute("predictions", Prediction.values());
        model.addAttribute("statuses", ReviewStatus.values());
        model.addAttribute("active", "studies");
        return "studies";
    }

    @GetMapping("/studies/new")
    public String form(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new StudyForm());
        }
        addChoices(model);
        model.addAttribute("active", "register");
        return "study-form";
    }

    @PostMapping("/studies")
    public String create(
            @Valid @ModelAttribute("form") StudyForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirect
    ) {
        if (!result.hasErrors() && form.getAccession() != null && studies.accessionTaken(form.getAccession())) {
            result.rejectValue("accession", "taken", "That accession is already on the worklist");
        }
        if (result.hasErrors()) {
            addChoices(model);
            model.addAttribute("active", "register");
            return "study-form";
        }
        var saved = studies.register(form);
        redirect.addFlashAttribute("notice", "Study " + saved.getAccession() + " is on the worklist.");
        return "redirect:/studies/" + saved.getId();
    }

    @GetMapping("/studies/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("study", studies.get(id));
        model.addAttribute("active", "studies");
        return "study";
    }

    @PostMapping("/studies/{id}/score")
    public String score(@PathVariable Long id, RedirectAttributes redirect) {
        studies.score(id);
        redirect.addFlashAttribute("notice", "Diagnostic model score computed.");
        return "redirect:/studies/" + id;
    }

    @PostMapping("/studies/{id}/review")
    public String review(@PathVariable Long id, RedirectAttributes redirect) {
        studies.review(id);
        redirect.addFlashAttribute("notice", "Marked as reviewed.");
        return "redirect:/studies/" + id;
    }

    @PostMapping("/studies/{id}/reopen")
    public String reopen(@PathVariable Long id, RedirectAttributes redirect) {
        studies.reopen(id);
        redirect.addFlashAttribute("notice", "Reopened for clinical review.");
        return "redirect:/studies/" + id;
    }

    @PostMapping("/studies/{id}/findings")
    public String updateFindings(
            @PathVariable Long id,
            @RequestParam(required = false) String findings,
            RedirectAttributes redirect
    ) {
        studies.updateFindings(id, findings);
        redirect.addFlashAttribute("notice", "Clinical findings note updated.");
        return "redirect:/studies/" + id;
    }

    @PostMapping("/studies/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        var study = studies.get(id);
        String accession = study.getAccession();
        studies.delete(id);
        redirect.addFlashAttribute("notice", "Study " + accession + " has been removed.");
        return "redirect:/studies";
    }

    private static void addChoices(Model model) {
        model.addAttribute("sexes", Sex.values());
        model.addAttribute("modalities", Modality.values());
    }

    private static <E extends Enum<E>> E parse(String raw, Class<E> type) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(type, raw);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
