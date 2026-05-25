package com.hospital.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${patient.service.url:http://43.204.251.193:8081}")
    private String patientServiceUrl;

    @Value("${doctor.service.url:http://43.204.251.193:8082}")
    private String doctorServiceUrl;

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        for (Appointment appointment : appointments) {
            appointment.setPatientName(getPatientName(appointment.getPatientId()));
            appointment.setDoctorName(getDoctorName(appointment.getDoctorId()));
        }
        return appointments;
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        optionalAppointment.ifPresent(appointment -> {
            appointment.setPatientName(getPatientName(appointment.getPatientId()));
            appointment.setDoctorName(getDoctorName(appointment.getDoctorId()));
        });
        return optionalAppointment;
    }

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, Appointment appointment) {
        appointment.setId(id);
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public String getPatientName(Long patientId) {
        try {
            String url = patientServiceUrl + "/api/patients/" + patientId;
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("name")) {
                return (String) response.get("name");
            }
            return "Unknown Patient";
        } catch (Exception e) {
            return "Unknown Patient";
        }
    }

    public String getDoctorName(Long doctorId) {
        try {
            String url = doctorServiceUrl + "/api/doctors/" + doctorId;
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("name")) {
                return (String) response.get("name");
            }
            return "Unknown Doctor";
        } catch (Exception e) {
            return "Unknown Doctor";
        }
    }

    public List<Map<String, Object>> getAllPatients() {
        try {
            String url = patientServiceUrl + "/api/patients";
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getAllDoctors() {
        try {
            String url = doctorServiceUrl + "/api/doctors";
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
