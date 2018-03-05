/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.lhist.database;

import ulb.lisa.lhist.model.Person;
import ulb.lisa.lhist.model.Patient;
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
 * @author Adrien Foucart
 */
public class PatientDatabase {
    
    private Connection conn = null;
    
    public PatientDatabase(){
        this.conn = Database.getInstance().getConnection();
    }
    
    public ArrayList<Patient> getAllPatients(){
        ArrayList<Patient> patients = new ArrayList();
        try {
            Statement s = conn.createStatement();
            s.executeQuery("SELECT pat.idpatient, pat.socialSecurity, per.idperson, per.firstName, per.lastName, per.dateOfBirth, per.gender FROM patient pat LEFT JOIN person per ON per.idperson = pat.idperson");
            
            ResultSet rs = s.getResultSet();
            while(rs.next()){
                int idpatient = rs.getInt("idpatient");
                String socialSecurity = rs.getString("socialSecurity");
                int idperson = rs.getInt("idperson");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String dateOfBirth = rs.getString("dateOfBirth");
                String gender = rs.getString("gender");
                
                Patient pat = new Patient(idpatient, socialSecurity, new Person(idperson, firstName, lastName, gender, dateOfBirth));
                patients.add(pat);
            }
            rs.close();
            s.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return patients;
    }
    
    public void addPatient(Patient pat){
        try {
            conn.setAutoCommit(false);
            PreparedStatement s = conn.prepareStatement("INSERT INTO person(firstName, lastName, dateOfBirth, gender) VALUES(?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            
            s.setString(1, pat.getPerson().getFirstName());
            s.setString(2, pat.getPerson().getLastName());
            s.setString(3, pat.getPerson().getDateOfBirth());
            s.setString(4, pat.getPerson().getGender());
            
            s.executeUpdate();
            ResultSet rs = s.getGeneratedKeys();
            
            if( rs.next() ){
                pat.getPerson().setIdperson(rs.getInt(1));
                
                PreparedStatement s2 = conn.prepareStatement("INSERT INTO patient(idperson, socialSecurity) VALUES(?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                
                s2.setInt(1, pat.getPerson().getIdperson());
                s2.setString(2, pat.getSocialSecurity());
                
                s2.executeUpdate();
                ResultSet rs2 = s2.getGeneratedKeys();
                
                if( rs2.next() ){
                    pat.setIdpatient(rs2.getInt(1));
                }
                
                conn.commit();                
                rs2.close();
                s2.close();
            }
            
            rs.close();
            s.close(); 
            conn.setAutoCommit(true);           
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editPatient(Patient pat){
        try {
            PreparedStatement s = conn.prepareStatement("UPDATE person SET firstName = ?, lastName = ?, gender = ?, dateOfBirth = ? WHERE idperson = ?");
            s.setString(1, pat.getPerson().getFirstName());
            s.setString(2, pat.getPerson().getLastName());
            s.setString(3, pat.getPerson().getGender());
            s.setString(4, pat.getPerson().getDateOfBirth());
            s.setInt(5, pat.getPerson().getIdperson());
            s.executeUpdate();
            s.close();
            
            s = conn.prepareStatement("UPDATE patient SET socialSecurity = ? WHERE idpatient = ?");
            s.setString(1, pat.getSocialSecurity());
            s.setInt(2, pat.getIdpatient());
            s.executeUpdate();
            s.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deletePatient(Patient pat){
        try {
            PreparedStatement s = conn.prepareStatement("DELETE FROM patient WHERE idpatient = ?");
            s.setInt(1, pat.getIdpatient());
            s.executeUpdate();
            s.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
