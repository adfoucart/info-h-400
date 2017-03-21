/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infoh400_tp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrateur
 */
public class PatientController {
    
    public static ArrayList<Patient> getAllPatients(Database db){
        Connection conn = db.getConnection();
        ArrayList<Patient> allPatients = new ArrayList();
        
        try {
            Statement s = conn.createStatement();
            s.executeQuery("SELECT idpatient, socialSecurity, idperson FROM patient");
            ResultSet rs = s.getResultSet();
            while(rs.next()){
                int idpatient = rs.getInt("idpatient");
                String socialSecurity = rs.getString("socialSecurity");
                int idperson = rs.getInt("idperson");
                
                Statement s2 = conn.createStatement();
                s2.executeQuery("SELECT idperson, firstName, lastName, gender, dateOfBirth FROM person WHERE idperson = " + idperson);
                ResultSet rs2 = s2.getResultSet();
                while(rs2.next()){
                    String firstName = rs2.getString("firstName");
                    String lastName = rs2.getString("lastName");
                    String gender = rs2.getString("gender");
                    String dateOfBirth = rs2.getString("dateOfBirth");
                    
                    Patient p = new Patient(idpatient, socialSecurity, firstName, lastName, gender, dateOfBirth);
                    allPatients.add(p);
                }
                
                rs2.close();
                s2.close();
            }
            rs.close();
            s.close();
            return allPatients;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList();
    }
    
    public static void addPatient(Database db, Patient p){
        try {
            Connection conn = db.getConnection();
            
            String queryPerson = "INSERT INTO person(firstName,LastName,dateOfBirth,gender) VALUES(?,?,?,?)";
            
            PreparedStatement sPerson = conn.prepareStatement(queryPerson, Statement.RETURN_GENERATED_KEYS);
            sPerson.setString(1, p.getPerson().getFirstName());
            sPerson.setString(2, p.getPerson().getLastName());
            sPerson.setString(3, p.getPerson().getDateOfBirth());
            sPerson.setString(4, p.getPerson().getGender());
            
            sPerson.executeUpdate();
            ResultSet generatedKeys = sPerson.getGeneratedKeys();
            
            String queryPatient = "INSERT INTO patient(socialSecurity, idperson) VALUES(?, ?)";
            PreparedStatement sPatient = conn.prepareStatement(queryPatient);
            if( generatedKeys.next() ){
                sPatient.setString(1, p.getSocialSecurity());
                sPatient.setInt(2, generatedKeys.getInt(1));
                sPatient.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void editPatient(Database db, Patient p){
        try {
            Connection conn = db.getConnection();
            
            /*String getQuery = "SELECT idperson FROM patient WHERE idpatient = ?";
            
            PreparedStatement sGet = conn.prepareStatement(getQuery);
            sGet.setInt(1, p.getIdpatient());
            
            sGet.executeQuery();
            ResultSet rs = sGet.getResultSet();
            if( rs.next() ){*/
            //int idperson = rs.getInt("idperson");

            String updatePatientQuery = "UPDATE patient SET socialSecurity = ? WHERE idpatient = ?";
            PreparedStatement sUpdatePat = conn.prepareStatement(updatePatientQuery);
            sUpdatePat.setString(1, p.getSocialSecurity());
            sUpdatePat.setInt(2, p.getIdpatient());

            sUpdatePat.executeUpdate();

            String updatePersonQuery = "UPDATE person SET firstName = ?, lastName = ?, dateOfBirth = ?, gender = ? WHERE idperson = ?";
            PreparedStatement sUpdatePer = conn.prepareStatement(updatePersonQuery);
            sUpdatePer.setString(1, p.getPerson().getFirstName());
            sUpdatePer.setString(2, p.getPerson().getLastName());
            sUpdatePer.setString(3, p.getPerson().getDateOfBirth());
            sUpdatePer.setString(4, p.getPerson().getGender());
            sUpdatePer.setInt(5, p.getPerson().getIdperson());

            sUpdatePer.executeUpdate();
            //}
        } catch (SQLException ex) {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void deletePatient(Database db, Patient p){
        try {
            Connection conn = db.getConnection();
            
            String query = "DELETE FROM patient WHERE idpatient = ?";
            
            PreparedStatement s = conn.prepareStatement(query);
            s.setInt(1, p.getIdpatient());
            
            s.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
