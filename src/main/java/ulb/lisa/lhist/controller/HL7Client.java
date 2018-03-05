/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.lhist.controller;

import ulb.lisa.lhist.model.Patient;
import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.message.ADT_A01;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.model.v23.segment.PID;
import ca.uhn.hl7v2.model.v23.segment.PV1;
import ca.uhn.hl7v2.parser.Parser;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ulb.lisa.lhist.InfoH400_TP;

/**
 *
 * @author Adrien Foucart
 */
public class HL7Client {
    
    private static HapiContext context = new DefaultHapiContext();
    
    public static void send_ADT_A01(Patient pat){
        try {
            Parser parser = context.getPipeParser();
            
            ADT_A01 adt = new ADT_A01();
            adt.initQuickstart("ADT", "A01", "P");
        
            MSH mshSegment = adt.getMSH();
            mshSegment.getSendingApplication().getNamespaceID().setValue("LHIST");
            mshSegment.getSendingFacility().getNamespaceID().setValue("LISA");
            mshSegment.getReceivingApplication().getNamespaceID().setValue("TestPanel");
            mshSegment.getReceivingFacility().getNamespaceID().setValue("LISA");
            
            PID pid = adt.getPID();
            pid.getPatientName(0).getFamilyName().setValue(pat.getPerson().getLastName());
            pid.getPatientName(0).getGivenName().setValue(pat.getPerson().getFirstName());
            pid.getPatientIDExternalID().getID().setValue(String.valueOf(pat.getIdpatient()));
            pid.getDateOfBirth().getTimeOfAnEvent().setValue(pat.getPerson().getDateOfBirthAsDate());
            
            PV1 pv1 = adt.getPV1();
            pv1.getPatientClass().setValue("E");
            pv1.getAdmissionType().setValue("A");
                        
            String encodedMessage = parser.encode(adt);
            System.out.println("Printing ER7 Encoded Message:");
            System.out.println(encodedMessage);
            
            Connection connection = context.newClient("192.168.1.110", 52931, false);
            
            Initiator initiator = connection.getInitiator();
            Message response = initiator.sendAndReceive(adt);
            
            String responseString = parser.encode(response);
            System.out.println("Received response:\n" + responseString);
            
            connection.close();
        } catch (HL7Exception | IOException | LLPException ex) {
            Logger.getLogger(InfoH400_TP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(HL7Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
