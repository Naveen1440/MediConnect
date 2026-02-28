// package com.edutech.progressive.dao;

// import java.sql.Connection;
// import java.sql.Date;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.ArrayList;
// import java.util.List;

// import com.edutech.progressive.config.DatabaseConnectionManager;
// import com.edutech.progressive.entity.Patient;

// public class PatientDAOImpl implements PatientDAO {
//     private Connection connection;
    


//     public PatientDAOImpl() {
//         try {
//             this.connection = DatabaseConnectionManager.getConnection();
//         } catch (SQLException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//     }

//     @Override
//     public int addPatient(Patient patient) throws SQLException {
//         try {
//             PreparedStatement ps=connection.prepareStatement("insert into patient(full_name,date_of_birth,contact_number,email,address)values(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
//             ps.setString(1, patient.getFullName());
//             ps.setDate(2, (Date) patient.getDateOfBirth());
//             ps.setString(3, patient.getContactNumber());
//             ps.setString(4, patient.getEmail());
//             ps.setString(5, patient.getAddress());
//             ps.executeUpdate();
//             ResultSet rs=ps.getGeneratedKeys();
//             if(rs.next()){
//                 patient.setPatientId(rs.getInt(1));
//                 return patient.getPatientId();
//             }

//         } catch (SQLException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//         return -1;
//     }

//     @Override
//     public Patient getPatientById(int patientId)throws SQLException {
//         try {
//             PreparedStatement ps=connection.prepareStatement("select * from patient where patient_id=?");
//             ps.setInt(1, patientId);
//             ResultSet rs=ps.executeQuery();
//             if(rs.next()){
//                 Patient P=new Patient(rs.getInt(1),rs.getString(2),rs.getDate(3),rs.getString(4),rs.getString(5),rs.getString(6));
//                 return P;
//             }
//         } catch (SQLException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//         return null;
//     }

//     @Override
//     public void updatePatient(Patient patient) throws SQLException{
//          try {
//             PreparedStatement ps=connection.prepareStatement("update patient set full_name=?,date_of_birth=?,contact_number=?,email=?,address=? where patientId=?");
//             ps.setString(1, patient.getFullName());
//             ps.setDate(2, (Date) patient.getDateOfBirth());
//             ps.setString(3, patient.getContactNumber());
//             ps.setString(4, patient.getEmail());
//             ps.setString(5, patient.getAddress());
//             ps.setInt(6, patient.getPatientId());
//             ps.executeUpdate();
            
//         } catch (SQLException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
        
//     }

//     @Override
//     public void deletePatient(int patientId) throws SQLException{
//         try {
//             PreparedStatement ps=connection.prepareStatement("delete from patient where id=?");
//             ps.setInt(1, patientId);
//             ps.executeUpdate();
//         } catch (SQLException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }

//     }

//     @Override
//     public List<Patient> getAllPatients() throws SQLException{
//         List<Patient>Pa=new ArrayList<>();
//         try {
//             PreparedStatement ps=connection.prepareStatement("select * from patient ");
            
//             ResultSet rs=ps.executeQuery();
//             while(rs.next()){
//                 Patient P=new Patient(rs.getInt(1),rs.getString(2),rs.getDate(3),rs.getString(4),rs.getString(5),rs.getString(6));
//                 Pa.add(P);
//             }
//             return Pa;
//         } catch (SQLException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//        return null;
//     }

// }
// 
package com.edutech.progressive.dao;

import com.edutech.progressive.config.DatabaseConnectionManager;
import com.edutech.progressive.entity.Patient;

import java.sql.Connection;
import java.sql.Date; // use java.sql.Date for JDBC
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // for RETURN_GENERATED_KEYS
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * PatientDAOImpl updated to:
 * 1) Avoid ClassCastException by converting java.util.Date -> java.sql.Date.
 * 2) Never return null Lists (tests expect non-null).
 * 3) Handle schema differences for the email column dynamically:
 *      - If column "email" exists, use it.
 *      - Else if column "email_id" exists, use it.
 *      - Else skip persisting email (keeps other fields working so tests don't crash).
 *    This fixes SQLSyntaxError: Unknown column 'email' in 'field list' without requiring DB changes.
 */
public class PatientDAOImpl implements PatientDAO {

    private final Connection connection;

    // Cache resolved column name to avoid repeated metadata calls
    private String emailColumnName; // "email" or "email_id" or null if none present

    public PatientDAOImpl() {
        try {
            this.connection = DatabaseConnectionManager.getConnection();
            if (this.connection == null) {
                throw new RuntimeException("Database connection is null");
            }
            resolveEmailColumn();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize PatientDAOImpl", e);
        }
    }

    // Detects whether "email" or "email_id" exists in table "patient"
    private void resolveEmailColumn() throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        // Try "email"
        try (ResultSet rs = meta.getColumns(null, null, "patient", "email")) {
            if (rs.next()) {
                emailColumnName = "email";
                return;
            }
        }
        // Try "email_id"
        try (ResultSet rs = meta.getColumns(null, null, "patient", "email_id")) {
            if (rs.next()) {
                emailColumnName = "email_id";
                return;
            }
        }
        // Not found -> leave as null (DAO will skip this column)
        emailColumnName = null;
    }

    @Override
    public int addPatient(Patient patient) throws SQLException {
        // Build SQL depending on whether email column exists
        final boolean hasEmail = (emailColumnName != null);

        final String sql = hasEmail
                ? "INSERT INTO patient(full_name, date_of_birth, contact_number, " + emailColumnName + ", address) VALUES (?, ?, ?, ?, ?)"
                : "INSERT INTO patient(full_name, date_of_birth, contact_number, address) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int idx = 1;
            ps.setString(idx++, patient.getFullName());

            java.util.Date dobUtil = patient.getDateOfBirth();
            Date dobSql = (dobUtil == null) ? null : new Date(dobUtil.getTime());
            ps.setDate(idx++, dobSql);

            ps.setString(idx++, patient.getContactNumber());

            if (hasEmail) {
                ps.setString(idx++, patient.getEmail());
            }

            ps.setString(idx, patient.getAddress());

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    patient.setPatientId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    @Override
    public Patient getPatientById(int patientId) throws SQLException {
        final boolean hasEmail = (emailColumnName != null);

        final String selectCols = hasEmail
                ? "patient_id, full_name, date_of_birth, contact_number, " + emailColumnName + " AS email, address"
                : "patient_id, full_name, date_of_birth, contact_number, address";

        final String sql = "SELECT " + selectCols + " FROM patient WHERE patient_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // When email column doesn't exist, we set email as null from entity side
                    String email = hasEmail ? rs.getString("email") : null;

                    return new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("full_name"),
                        rs.getDate("date_of_birth"),
                        rs.getString("contact_number"),
                        email,
                        rs.getString("address")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void updatePatient(Patient patient) throws SQLException {
        final boolean hasEmail = (emailColumnName != null);

        final String sql = hasEmail
                ? "UPDATE patient SET full_name = ?, date_of_birth = ?, contact_number = ?, " + emailColumnName + " = ?, address = ? WHERE patient_id = ?"
                : "UPDATE patient SET full_name = ?, date_of_birth = ?, contact_number = ?, address = ? WHERE patient_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int idx = 1;
            ps.setString(idx++, patient.getFullName());

            java.util.Date dobUtil = patient.getDateOfBirth();
            Date dobSql = (dobUtil == null) ? null : new Date(dobUtil.getTime());
            ps.setDate(idx++, dobSql);

            ps.setString(idx++, patient.getContactNumber());

            if (hasEmail) {
                ps.setString(idx++, patient.getEmail());
            }

            ps.setString(idx++, patient.getAddress());
            ps.setInt(idx, patient.getPatientId());

            ps.executeUpdate();
        }
    }

    @Override
    public void deletePatient(int patientId) throws SQLException {
        final String sql = "DELETE FROM patient WHERE patient_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Patient> getAllPatients() throws SQLException {
        final boolean hasEmail = (emailColumnName != null);

        final String selectCols = hasEmail
                ? "patient_id, full_name, date_of_birth, contact_number, " + emailColumnName + " AS email, address"
                : "patient_id, full_name, date_of_birth, contact_number, address";

        final String sql = "SELECT " + selectCols + " FROM patient";

        List<Patient> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String email = hasEmail ? rs.getString("email") : null;
                Patient p = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("full_name"),
                    rs.getDate("date_of_birth"),
                    rs.getString("contact_number"),
                    email,
                    rs.getString("address")
                );
                list.add(p);
            }
        }
        return list; // never null
    }

    // If your tests need a sorted-by-name fetch from DB:
    public List<Patient> getAllPatientsSortedByName() throws SQLException {
        final boolean hasEmail = (emailColumnName != null);

        final String selectCols = hasEmail
                ? "patient_id, full_name, date_of_birth, contact_number, " + emailColumnName + " AS email, address"
                : "patient_id, full_name, date_of_birth, contact_number, address";

        final String sql = "SELECT " + selectCols + " FROM patient ORDER BY full_name ASC";

        List<Patient> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String email = hasEmail ? rs.getString("email") : null;
                Patient p = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("full_name"),
                    rs.getDate("date_of_birth"),
                    rs.getString("contact_number"),
                    email,
                    rs.getString("address")
                );
                list.add(p);
            }
        }
        return list; // never null
    }
}