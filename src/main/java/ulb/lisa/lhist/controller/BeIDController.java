/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.lhist.controller;

import be.belgium.eid.eidlib.BeID;
import be.belgium.eid.exceptions.CardNotFoundException;
import be.belgium.eid.exceptions.EIDException;
import be.belgium.eid.exceptions.InvalidSWException;
import be.belgium.eid.exceptions.WrongPINException;
import java.awt.Image;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.CardException;
import ulb.lisa.lhist.model.Patient;

/**
 *
 * @author Adrien Foucart
 */
public class BeIDController {
    
    public static Image photo = null;
    
    public static Patient readID(){
        
        try {
            BeID eID = new BeID(true);
            
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            
            String firstName = eID.getIDData().get1stFirstname();
            String lastName = eID.getIDData().getName();
            String dob = df.format(eID.getIDData().getBirthDate());
            String socialSecurity = eID.getIDData().getNationalNumber();
            String gender = String.valueOf(eID.getIDData().getSex());
            
            photo = eID.getIDPhoto().getImage();
            
            return new Patient(socialSecurity, firstName, lastName, gender, dob);
        } catch (EIDException ex) {
            Logger.getLogger(BeIDController.class.getName()).log(Level.SEVERE, null, ex);
            
            return null;
        }
    }

    public static String authenticate(String pinCode) {
        try {
            BeID eID = new BeID(true);

            eID.connect();
            
            eID.verifyPIN(pinCode);
            
            return eID.getIDData().get1stFirstname() + " " + eID.getIDData().getName();
        } catch ( WrongPINException ex ){
            System.out.println("Wrong PIN !");
            System.out.println(ex);
        } catch (CardNotFoundException | CardException | InvalidSWException ex) {
            System.out.println("Card problem.");
            System.out.println(ex);
        } catch (EIDException ex) {
            System.out.println("Couldn't get data from card or couldn't connect to card.");
            System.out.println(ex);
        }
        
        return null;
    }
    
}
