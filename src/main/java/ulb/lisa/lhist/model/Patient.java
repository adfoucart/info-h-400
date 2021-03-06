/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.lhist.model;

/**
 *
 * @author Adrien Foucart
 */
public class Patient {
    
    private int idpatient;
    private String socialSecurity;
    private Person person;
    public static int last_insert_id = 0;
    
    public Patient(int idpatient, String socialSecurity, Person person) {
        this.idpatient = idpatient;
        this.socialSecurity = socialSecurity;
        this.person = person;
        if( idpatient > this.last_insert_id )
            this.last_insert_id = idpatient;
    }
    
    public Patient(String socialSecurity, Person person) {
        this.socialSecurity = socialSecurity;
        this.person = person;
    }
    
    public Patient(int idpatient, String socialSecurity, String firstName, String lastName, String gender, String dateOfBirth) {
        this.idpatient = idpatient;
        this.socialSecurity = socialSecurity;
        this.person = new Person(firstName, lastName, gender, dateOfBirth);
    }
    
    public Patient(String socialSecurity, String firstName, String lastName, String gender, String dateOfBirth) {
        this.socialSecurity = socialSecurity;
        this.person = new Person(firstName, lastName, gender, dateOfBirth);
    }
    
    @Override
    public String toString(){
        return "[ID_PAT=" + this.idpatient + "] " + person.toString() + " - " + socialSecurity;
    }

    public String getSocialSecurity() {
        return socialSecurity;
    }

    public int getIdpatient() {
        return idpatient;
    }

    public Person getPerson() {
        return person;
    }

    public void setSocialSecurity(String socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
    public void setIdpatient(int idpatient) {
        this.idpatient = idpatient;
    }
}
