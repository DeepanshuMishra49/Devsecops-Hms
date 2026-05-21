package com.hospital.appointment;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class AppointmentViewController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/")
    public String home() {
        return "redirect:/appointments";
    }

    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "list";
    }

    @GetMapping("/appointments/new")
    public String showCreateForm(Model model) {
        Appointment appointment = new Appointment();
        appointment.setStatus("SCHEDULED");
        model.addAttribute("appointment", appointment);
        model.addAttribute("isEdit", false);
        model.addAttribute("patients", appointmentService.getAllPatients());
        model.addAttribute("doctors", appointmentService.getAllDoctors());
        return "form";
    }

    @PostMapping("/appointments")
    public String saveAppointment(@Valid Appointment appointment, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            model.addAttribute("patients", appointmentService.getAllPatients());
            model.addAttribute("doctors", appointmentService.getAllDoctors());
            return "form";
        }
        appointmentService.saveAppointment(appointment);
        return "redirect:/appointments";
    }

    @GetMapping("/appointments/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Appointment> optionalAppointment = appointmentService.getAppointmentById(id);
        if (optionalAppointment.isPresent()) {
            model.addAttribute("appointment", optionalAppointment.get());
            model.addAttribute("isEdit", true);
            model.addAttribute("patients", appointmentService.getAllPatients());
            model.addAttribute("doctors", appointmentService.getAllDoctors());
            return "form";
        }
        return "redirect:/appointments";
    }

    @PostMapping("/appointments/update/{id}")
    public String updateAppointment(@PathVariable Long id, @Valid Appointment appointment,
                                     BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            model.addAttribute("patients", appointmentService.getAllPatients());
            model.addAttribute("doctors", appointmentService.getAllDoctors());
            return "form";
        }
        appointmentService.updateAppointment(id, appointment);
        return "redirect:/appointments";
    }

    @GetMapping("/appointments/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }

    @GetMapping("/appointments/view/{id}")
    public String viewAppointment(@PathVariable Long id, Model model) {
        Optional<Appointment> optionalAppointment = appointmentService.getAppointmentById(id);
        if (optionalAppointment.isPresent()) {
            model.addAttribute("appointment", optionalAppointment.get());
            return "view";
        }
        return "redirect:/appointments";
    }
}
