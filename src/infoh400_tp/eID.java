/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infoh400_tp;

import be.belgium.eid.eidcommon.ByteConverter;
import be.belgium.eid.eidlib.BeID;
import be.belgium.eid.event.CardListener;
import be.belgium.eid.exceptions.EIDException;
import be.belgium.eid.objects.IDData;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPasswordField;

/**
 *
 * @author Administrateur
 */
public class eID {
    
    public Patient p;
    public Image picture;
    private BeID eid;
    private JButton eIDButton;
    
    
    public eID(JButton eIDButton){
        p = null;
        picture = null;
        this.eIDButton = eIDButton;
        
        eid = new BeID(true);
        eid.enableCardListener(new CardListener() {
            @Override
            public void cardInserted() {
                eIDButton.setEnabled(true);
            }

            @Override
            public void cardRemoved() {
                eIDButton.setEnabled(false);                
            }
        });
    }
    
    public void retrievePatientInfo(){
        try {
            IDData data = eid.getIDData();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            p = new Patient(data.getNationalNumber(), data.get1stFirstname(), data.getName(), String.valueOf(data.getSex()), formatter.format(data.getBirthDate()));
            picture = eid.getIDPhoto().getImage();
        } catch (EIDException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean authenticate(JPasswordField passwordField){
        try {
            String text = "anything";
            byte[] signature = eid.generateSignature(text.getBytes(), String.valueOf(passwordField.getPassword()), BeID.SignatureType.AUTHENTICATIONSIG);
            return eid.verifySignature(text.getBytes(), signature, BeID.SignatureType.AUTHENTICATIONSIG);
        } catch (EIDException ex) {
            Logger.getLogger(eID.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
}
