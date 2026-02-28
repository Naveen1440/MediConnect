// File: src/main/java/com/edutech/progressive/service/impl/ClinicServiceImplJpa.java
package com.edutech.progressive.service.impl;

import com.edutech.progressive.entity.Clinic;
import com.edutech.progressive.service.ClinicService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service("clinicServiceImplJpa")
public class ClinicServiceImplJpa implements ClinicService {

    @Override
    public List<Clinic> getAllClinics() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public Clinic getClinicById(int clinicId) throws Exception {
        return null;
    }

    @Override
    public Integer addClinic(Clinic clinic) throws Exception {
        return -1;
    }

    @Override
    public void updateClinic(Clinic clinic) throws Exception {
        // no-op for Day 6
    }

    @Override
    public void deleteClinic(int clinicId) throws Exception {
        // no-op for Day 6
    }
}
