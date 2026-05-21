package com.hospital.doctor;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DoctorViewController {

    private final DoctorService doctorService;

    public DoctorViewController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/")
    public String redirectToDoctors() {
        return "redirect:/doctors";
    }

    @GetMapping("/doctors")
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "list";
    }

    @GetMapping("/doctors/new")
    public String showAddForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("isEdit", false);
        return "form";
    }

    @PostMapping("/doctors")
    public String saveDoctor(@Valid Doctor doctor, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "form";
        }
        doctorService.saveDoctor(doctor);
        return "redirect:/doctors";
    }

    @GetMapping("/doctors/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        model.addAttribute("doctor", doctor);
        model.addAttribute("isEdit", true);
        return "form";
    }

    @PostMapping("/doctors/update/{id}")
    public String updateDoctor(@PathVariable Long id, @Valid Doctor doctor, BindingResult result, Model model) {
        if (result.hasErrors()) {
            doctor.setId(id);
            model.addAttribute("isEdit", true);
            return "form";
        }
        doctorService.updateDoctor(id, doctor);
        return "redirect:/doctors";
    }

    @GetMapping("/doctors/delete/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/doctors";
    }

    @GetMapping("/doctors/view/{id}")
    public String viewDoctor(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        model.addAttribute("doctor", doctor);
        return "view";
    }
}
