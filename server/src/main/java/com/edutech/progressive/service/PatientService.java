package com.edutech.progressive.service;
 
import java.util.List;
 
import com.edutech.progressive.entity.Patient;
 
public interface PatientService {
 
    List<Patient> getAllPatients() throws Exception;
 
    Integer addPatient(Patient patient) throws Exception;
 
    List<Patient> getAllPatientSortedByName() throws Exception;
 
    void updatePatient(Patient patient) throws Exception;
 
    void deletePatient(int patientId) throws Exception;
 
    Patient getPatientById(int patientId) throws Exception;
}