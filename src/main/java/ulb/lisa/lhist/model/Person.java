/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.lhist.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Adrien Foucart
 */
public class Person {

    public void setIdperson(int idperson) {
        this.idperson = idperson;
    }
    
    public enum Gender{M, F};
    
    private int idperson;
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    public static int last_insert_id = 0;
    
    public Person(int idperson, String firstName, String lastName, String gender, String dateOfBirth) {
        this.idperson = idperson;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        if( idperson > this.last_insert_id ){
            this.last_insert_id = idperson;
        }
    }
    
    public Person(String firstName, String lastName, String gender, String dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
    
    @Override
    public String toString(){
        return "[ID_PER=" + this.idperson + "] " + lastName.toUpperCase() + " " + firstName + " (" + gender + ") - " + dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public Date getDateOfBirthAsDate() throws ParseException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.parse(dateOfBirth);
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getIdperson() {
        return idperson;
    }
    
    
}
