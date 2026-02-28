package com.edutech.progressive.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.edutech.progressive.config.DatabaseConnectionManager;
import com.edutech.progressive.entity.Doctor;

public class DoctorDAOImpl implements DoctorDAO {
    private Connection connection;
    


    

    public DoctorDAOImpl() {
        try {
            this.connection = DatabaseConnectionManager.getConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public int addDoctor(Doctor doctor) throws SQLException{
        try {
            PreparedStatement ps=connection.prepareStatement("insert into doctor(full_name,specialty,contact_number,email,years_of_experience)values (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, doctor.getFullName());
            ps.setString(2,doctor.getSpecialty());
            ps.setString(3,doctor.getContactNumber());
            ps.setString(4,doctor.getEmail());
            ps.setInt(5, doctor.getYearsOfExperience());
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()){
                doctor.setDoctorId(rs.getInt(1));
                return doctor.getDoctorId();
            }
            

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return -1;
    }

    @Override
    public Doctor getDoctorById(int doctorId)throws SQLException {
         try {
            PreparedStatement ps=connection.prepareStatement("select * from doctor where doctor_id=?");
            ps.setInt(1, doctorId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                Doctor d=new Doctor(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6));
                return d;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


       return null;
    }

    @Override
    public void updateDoctor(Doctor doctor)throws SQLException {
         try {
            PreparedStatement ps=connection.prepareStatement("update doctor set full_name=?,specialty=?,contact_number=?,email=?,years_of_experience=? where doctor_id=?");
            ps.setString(1, doctor.getFullName());
            ps.setString(2,doctor.getSpecialty());
            ps.setString(3,doctor.getContactNumber());
            ps.setString(4,doctor.getEmail());
            ps.setInt(5, doctor.getYearsOfExperience());
            ps.setInt(6, doctor.getDoctorId());
            ps.executeUpdate();
            
            
            

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    @Override
    public void deleteDoctor(int doctorId)throws SQLException {
        try {
            PreparedStatement ps=connection.prepareStatement("delete from doctor where doctor_id=?");
            ps.setInt(1, doctorId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    @Override
    public List<Doctor> getAllDoctors()throws SQLException {
        List<Doctor>Do=new ArrayList<>();
        try {
            PreparedStatement ps=connection.prepareStatement("select * from doctor");
            
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                Doctor d=new Doctor(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6));
                Do.add(d);
            }
            return Do;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }



}
