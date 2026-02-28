package com.edutech.progressive.service;
 
import java.util.List;
 
import com.edutech.progressive.entity.Doctor;
 
public interface DoctorService {
 
    List<Doctor> getAllDoctors() throws Exception;
 
    Integer addDoctor(Doctor doctor) throws Exception;
 
    List<Doctor> getDoctorSortedByExperience() throws Exception;
 
    void updateDoctor(Doctor doctor) throws Exception;
 
    void deleteDoctor(int doctorId) throws Exception;
 
    Doctor getDoctorById(int doctorId) throws Exception;
}