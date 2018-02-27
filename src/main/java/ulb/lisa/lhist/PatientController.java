/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.lhist;

import java.util.ArrayList;

/**
 *
 * @author Adrien Foucart
 */
public class PatientController {
    
    private static ArrayList<Patient> patientList;
    private static PatientDatabase db = new PatientDatabase();
    
    public static ArrayList<Patient> getAllPatients(){        
        patientList = db.getAllPatients();
        return patientList;
    }
    
    public static void addPatient(String socialSecurity, String firstName, String lastName, String gender, String dateOfBirth){
        Patient p = new Patient(Patient.last_insert_id+1, socialSecurity, new Person(Person.last_insert_id+1, firstName, lastName, gender, dateOfBirth));
        patientList.add(p);
        db.addPatient(p);
    }

    public static void editPatient(Patient p, String socialSecurity, String firstName, String lastName, String gender, String dateOfBirth) {
        p.setSocialSecurity(socialSecurity);
        p.getPerson().setFirstName(firstName);
        p.getPerson().setLastName(lastName);
        p.getPerson().setGender(gender);
        p.getPerson().setDateOfBirth(dateOfBirth);
        db.editPatient(p);
    }

    static void deletePatient(Patient p) {
        patientList.remove(p);
        db.deletePatient(p);
    }
}
