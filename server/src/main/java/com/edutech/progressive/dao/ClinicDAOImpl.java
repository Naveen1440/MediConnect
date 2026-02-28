package com.edutech.progressive.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.edutech.progressive.config.DatabaseConnectionManager;
import com.edutech.progressive.entity.Clinic;

public class ClinicDAOImpl implements ClinicDAO{
    private Connection connection;
    

    public ClinicDAOImpl() {
        try {
            this.connection = DatabaseConnectionManager.getConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public int addClinic(Clinic clinic) throws SQLException{
        try {
            PreparedStatement ps=connection.prepareStatement("insert into clinic(clinic_name,location,doctor_Id,contact_number,established_year) values (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, clinic.getClinicName());
            ps.setString(2, clinic.getLocation());
            ps.setInt(3, clinic.getDoctorId());
            ps.setString(4, clinic.getContactNumber());
            ps.setInt(5, clinic.getEstablishedYear());
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()){
                clinic.setClinicId(rs.getInt(1));
                return clinic.getClinicId();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public Clinic getClinicById(int clinicId)throws SQLException {
        try {
            PreparedStatement ps=connection.prepareStatement("select * from clinic where clinic_id=?");
            ps.setInt(1, clinicId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                Clinic c=new Clinic(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getString(5),rs.getInt(6));
                return c;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
       return null;

    }

    @Override
    public void updateClinic(Clinic clinic) throws SQLException{
        
            try {
                PreparedStatement ps=connection.prepareStatement("update clinic set clinic_name=?,location=?,doctor_Id=?,contact_number=?,established_year=? where clinic_id=?");
                ps.setString(1, clinic.getClinicName());
                ps.setString(2, clinic.getLocation());
                ps.setInt(3, clinic.getDoctorId());
                ps.setString(4, clinic.getContactNumber());
                ps.setInt(5, clinic.getEstablishedYear());
                ps.setInt(6, clinic.getClinicId());
                ps.executeUpdate();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           
        
    }

    @Override
    public void deleteClinic(int clinicId)throws SQLException {
        
            PreparedStatement ps=connection.prepareStatement("delete from clinic where clinic_id=?");
            ps.setInt(1, clinicId);
            ps.executeUpdate();
        
        
    }

    @Override
    public List<Clinic> getAllClinics()throws SQLException {
        List<Clinic>cl=new ArrayList<>();
        try {
            PreparedStatement ps=connection.prepareStatement("select * from clinic ");
        
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                Clinic c=new Clinic(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getString(5),rs.getInt(6));
                cl.add(c);
            }
            return cl;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}
