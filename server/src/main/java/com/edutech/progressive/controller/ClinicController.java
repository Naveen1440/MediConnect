package com.edutech.progressive.controller;

import com.edutech.progressive.entity.Clinic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/clinic")

public class ClinicController {
    public ResponseEntity<List<Clinic>> getAllClinics() {
        return null;
    }

    public ResponseEntity<Clinic> getClinicById(int clinicId) {
        return null;
    }

    public ResponseEntity<Integer> addClinic(Clinic clinic) {
        return null;
    }

    public ResponseEntity<Void> updateClinic(int clinicId, Clinic clinic) {
        return null;
    }

    public ResponseEntity<Void> deleteClinic(int clinicId) {
        return null;
    }

    public ResponseEntity<List<Clinic>> getAllClinicByLocation(String location) {
        return null;
    }

    public ResponseEntity<List<Clinic>> getAllClinicByDoctorId(int doctorId) {
        return null;
    }
}
