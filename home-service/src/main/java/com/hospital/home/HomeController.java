package com.hospital.home;

import com.hospital.home.model.User;
import com.hospital.home.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class HomeController {

    @Value("${patient.service.url:http://43.204.251.193:8081}")
    private String patientServiceUrl;

    @Value("${doctor.service.url:http://43.204.251.193:8082}")
    private String doctorServiceUrl;

    @Value("${appointment.service.url:http://43.204.251.193:8083}")
    private String appointmentServiceUrl;


    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("patientServiceUrl", patientServiceUrl);
        model.addAttribute("doctorServiceUrl", doctorServiceUrl);
        model.addAttribute("appointmentServiceUrl", appointmentServiceUrl);

        // Check service health status
        model.addAttribute("patientServiceUp", isServiceUp(patientServiceUrl));
        model.addAttribute("doctorServiceUp", isServiceUp(doctorServiceUrl));
        model.addAttribute("appointmentServiceUp", isServiceUp(appointmentServiceUrl));

        // Get currently authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUsername = authentication.getName();
            userRepository.findByUsername(currentUsername)
                    .or(() -> userRepository.findByEmail(currentUsername))
                    .ifPresent(user -> model.addAttribute("currentUser", user));
        }

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
