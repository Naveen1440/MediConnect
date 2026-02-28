// File: src/main/java/com/edutech/progressive/controller/DoctorController.java
package com.edutech.progressive.controller;

import com.edutech.progressive.entity.Doctor;
import com.edutech.progressive.service.DoctorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/doctor", produces = MediaType.APPLICATION_JSON_VALUE)
public class DoctorController {

    private final DoctorService doctorServiceJpa;

    public DoctorController(@Qualifier("doctorServiceImplJpa") DoctorService doctorServiceJpa) {
        this.doctorServiceJpa = doctorServiceJpa;
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        try {
            return ResponseEntity.ok(doctorServiceJpa.getAllDoctors());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable int doctorId) {
        try {
            Doctor doctor = doctorServiceJpa.getDoctorById(doctorId);
            return ResponseEntity.ok(doctor);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> addDoctor(@RequestBody Doctor doctor) {
        try {
            Integer id = doctorServiceJpa.addDoctor(doctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{doctorId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Doctor> updateDoctor(@PathVariable int doctorId, @RequestBody Doctor doctor) {
        try {
            doctor.setDoctorId(doctorId);
            doctorServiceJpa.updateDoctor(doctor);
            Doctor updated = doctorServiceJpa.getDoctorById(doctorId);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Fix for DaySevenTest: return 204 No Content on successful delete
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable int doctorId) {
        try {
            doctorServiceJpa.deleteDoctor(doctorId);
            return ResponseEntity.noContent().build(); // 204
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/experience")
    public ResponseEntity<List<Doctor>> getDoctorSortedByExperience() {
        try {
            return ResponseEntity.ok(doctorServiceJpa.getDoctorSortedByExperience());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


// package com.edutech.progressive.controller;

// import com.edutech.progressive.entity.Doctor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import java.util.List;
// @RestController
// @RequestMapping("/doctor")

// public class DoctorController {

//     public ResponseEntity<List<Doctor>> getAllDoctors() {
//         return null;
//     }

//     public ResponseEntity<Doctor> getDoctorById(int doctorId) {
//         return null;
//     }

//     public ResponseEntity<Integer> addDoctor(Doctor doctor) {
//         return null;
//     }

//     public ResponseEntity<Void> updateDoctor(int doctorId, Doctor doctor) {
//         return null;
//     }

//     public ResponseEntity<Void> deleteDoctor(int doctorId) {
//         return null;
//     }

//     public ResponseEntity<List<Doctor>> getDoctorSortedByExperience() {
//         return null;
//     }
// }
