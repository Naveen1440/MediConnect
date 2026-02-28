// File: src/main/java/com/edutech/progressive/service/impl/DoctorServiceImplJpa.java
package com.edutech.progressive.service.impl;

import com.edutech.progressive.entity.Doctor;
import com.edutech.progressive.repository.DoctorRepository;
import com.edutech.progressive.service.DoctorService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("doctorServiceImplJpa")
public class DoctorServiceImplJpa implements DoctorService  {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImplJpa(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Doctor> getAllDoctors() throws Exception {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(int doctorId) throws Exception {
        return doctorRepository.findByDoctorId(doctorId);
    }

    @Override
    public Integer addDoctor(Doctor doctor) throws Exception {
        Doctor saved = doctorRepository.save(doctor);
        return saved.getDoctorId();
    }

    @Override
    public List<Doctor> getDoctorSortedByExperience() throws Exception {
        // Ascending by years_of_experience
        return doctorRepository.findAll(Sort.by(Sort.Direction.ASC, "yearsOfExperience"));
    }

    @Override
    public void updateDoctor(Doctor doctor) throws Exception {
        // Upsert behavior: if present, merge mutable fields; else create
        Doctor existing = doctorRepository.findByDoctorId(doctor.getDoctorId());
        if (existing == null) {
            doctorRepository.save(doctor);
            return;
        }
        existing.setFullName(doctor.getFullName());
        existing.setSpecialty(doctor.getSpecialty());
        existing.setContactNumber(doctor.getContactNumber());
        existing.setEmail(doctor.getEmail());
        existing.setYearsOfExperience(doctor.getYearsOfExperience());
        doctorRepository.save(existing);
    }

    @Override
    public void deleteDoctor(int doctorId) throws Exception {
        doctorRepository.deleteById(doctorId);
    }
}




// File: src/main/java/com/edutech/progressive/service/impl/DoctorServiceImplJpa.java
// package com.edutech.progressive.service.impl;

// import com.edutech.progressive.entity.Doctor;
// import com.edutech.progressive.service.DoctorService;
// import org.springframework.stereotype.Service;

// import java.util.Collections;
// import java.util.List;

// @Service("doctorServiceImplJpa")
// public class DoctorServiceImplJpa implements DoctorService  {

//     @Override
//     public List<Doctor> getAllDoctors() throws Exception {
//         return Collections.emptyList();
//     }

//     @Override
//     public Integer addDoctor(Doctor doctor) throws Exception {
//         return -1;
//     }

//     @Override
//     public List<Doctor> getDoctorSortedByExperience() throws Exception {
//         return Collections.emptyList();
//     }

//     @Override
//     public void updateDoctor(Doctor doctor) throws Exception {
//         // no-op for Day 6
//     }

//     @Override
//     public void deleteDoctor(int doctorId) throws Exception {
//         // no-op for Day 6
//     }

//     @Override
//     public Doctor getDoctorById(int doctorId) throws Exception {
//         return null;
//     }
// }