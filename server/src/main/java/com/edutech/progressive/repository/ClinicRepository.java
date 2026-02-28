package com.edutech.progressive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.progressive.entity.Clinic;
@Repository
public interface ClinicRepository extends JpaRepository<Clinic,Integer> {
}
