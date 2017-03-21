/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infoh400_tp;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.message.ADT_A01;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.PID;
import ca.uhn.hl7v2.parser.Parser;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrateur
 */
class HL7 {
    
    private String host;
    private int port;
    private HapiContext ctxt;
    private boolean useSSL;
    
    public HL7(String host, int port){
        this.host = host;
        this.port = port;
        ctxt = new DefaultHapiContext();
        useSSL = false;
    }

    void send_ADT_A01(Patient pat){
        
        try {
            ADT_A01 adt = new ADT_A01();
            
            MSH msh = adt.getMSH();
            msh.getFieldSeparator().setValue("|");
            msh.getEncodingCharacters().setValue("^~\\&");
            msh.getDateTimeOfMessage().setValue(Date.from(Instant.now()));
            msh.getSendingApplication().getNamespaceID().setValue("LISATP");
            msh.getSequenceNumber().setValue("123");
            msh.getMessageType().getMessageCode().setValue("ADT");
            msh.getMessageType().getTriggerEvent().setValue("A01");
            msh.getMessageType().getMessageStructure().setValue("ADT A01");
            msh.getVersionID().getVersionID().setValue("2.6");
            msh.getMessageControlID().setValue("1");
            
            PID pid = adt.getPID();
            pid.getPatientName(0).getFamilyName().getSurname().setValue(pat.getPerson().getLastName());
            pid.getPatientName(0).getGivenName().setValue(pat.getPerson().getFirstName());
            pid.getDateTimeOfBirth().setValue(pat.getPerson().getDateOfBirth());
            pid.getPatientIdentifierList(0).getIDNumber().setValue(String.valueOf(pat.getIdpatient()));
            
            Parser parser = ctxt.getPipeParser();
            String encoded = parser.encode(adt);
            System.out.println("Encoded : ");
            System.out.println(encoded);
            
            Connection conn = ctxt.newClient(host, port, useSSL);
            
            Initiator init = conn.getInitiator();
            Message response = init.sendAndReceive(adt);
            
            String responseString = parser.encode(response);
            System.out.println("Encoded response : ");
            System.out.println(responseString);
            
            conn.close();
        } catch (DataTypeException ex) {
            Logger.getLogger(HL7.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HL7Exception ex) {
            Logger.getLogger(HL7.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | LLPException ex) {
            Logger.getLogger(HL7.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
    
    
}
