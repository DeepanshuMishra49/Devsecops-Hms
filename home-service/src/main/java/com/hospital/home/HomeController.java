package com.hospital.home;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class HomeController {

    @Value("${patient.service.url:http://localhost:8081}")
    private String patientServiceUrl;

    @Value("${doctor.service.url:http://localhost:8082}")
    private String doctorServiceUrl;

    @Value("${appointment.service.url:http://localhost:8083}")
    private String appointmentServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("patientServiceUrl", patientServiceUrl);
        model.addAttribute("doctorServiceUrl", doctorServiceUrl);
        model.addAttribute("appointmentServiceUrl", appointmentServiceUrl);

        // Check service health status
        model.addAttribute("patientServiceUp", isServiceUp(patientServiceUrl));
        model.addAttribute("doctorServiceUp", isServiceUp(doctorServiceUrl));
        model.addAttribute("appointmentServiceUp", isServiceUp(appointmentServiceUrl));

        return "index";
    }

    private boolean isServiceUp(String serviceUrl) {
        try {
            restTemplate.getForObject(serviceUrl + "/actuator/health", String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
