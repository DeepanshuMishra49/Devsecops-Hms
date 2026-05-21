package com.hospital.patient;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class PatientViewController {

    private final PatientService patientService;

    public PatientViewController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/")
    public String redirectToPatients() {
        return "redirect:/patients";
    }

    @GetMapping("/patients")
    public String listPatients(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "list";
    }

    @GetMapping("/patients/new")
    public String showAddForm(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("isEdit", false);
        return "form";
    }

    @PostMapping("/patients")
    public String savePatient(@Valid Patient patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "form";
        }
        patientService.savePatient(patient);
        return "redirect:/patients";
    }

    @GetMapping("/patients/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        model.addAttribute("patient", patient);
        model.addAttribute("isEdit", true);
        return "form";
    }

    @PostMapping("/patients/update/{id}")
    public String updatePatient(@PathVariable Long id, @Valid Patient patient,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            patient.setId(id);
            model.addAttribute("isEdit", true);
            return "form";
        }
        patientService.updatePatient(id, patient);
        return "redirect:/patients";
    }

    @GetMapping("/patients/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }

    @GetMapping("/patients/view/{id}")
    public String viewPatient(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        model.addAttribute("patient", patient);
        return "view";
    }
}
