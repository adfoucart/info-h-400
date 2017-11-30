/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infoh400_tp;

import java.util.ArrayList;

/**
 *
 * @author Adrien Foucart
 */
public class PatientController {
    
    private static ArrayList<Patient> patientList;
    
    public static ArrayList<Patient> getAllPatients(){        
        if( patientList == null ){
            // Initialize patientDB
            patientList = new ArrayList<Patient>();
            patientList.add(new Patient(1, "AA214", new Person(3, "Christopher", "Nolan", "M", "30-07-1970")));
            patientList.add(new Patient(2, "OR184", new Person(1, "Jodie", "Foster", "F", "19-11-1962")));
            patientList.add(new Patient(3, "CQ556", new Person(2, "Robert", "Zemeckis", "M", "14-05-1952")));
        }
        return patientList;
    }
    
    public static void addPatient(Patient p){
        
    }
    
    public static void editPatient(Patient p){
        
    }
    
    public static void deletePatient(Patient p){
        
    }
}
