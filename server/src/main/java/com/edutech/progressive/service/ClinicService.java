package com.edutech.progressive.service;

import com.edutech.progressive.entity.Clinic;

import java.sql.SQLException;
import java.util.List;

public interface ClinicService {

    public List<Clinic> getAllClinics() throws Exception;

    public Clinic getClinicById(int clinicId)throws Exception;

    public Integer addClinic(Clinic clinic)throws Exception;

    public void updateClinic(Clinic clinic)throws Exception;

    public void deleteClinic(int clinicId)throws Exception;

    //Do not implement these methods in ClinicServiceImplJdbc.java class
    default public List<Clinic> getAllClinicByLocation(String location) { return null; }

    default public List<Clinic> getAllClinicByDoctorId(int doctorId) { return null; }
}
