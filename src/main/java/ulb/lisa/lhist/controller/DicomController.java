/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.lhist.controller;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomDirectory;
import com.pixelmed.dicom.DicomDirectoryRecord;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.DicomInputStream;
import com.pixelmed.dicom.SOPClass;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.display.SourceImage;
import com.pixelmed.network.DicomNetworkException;
import com.pixelmed.network.StorageSOPClassSCU;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrien Foucart
 */
public class DicomController {

    public static DicomDirectory getDICOMDIR(File selectedFile) {
        try {
            AttributeList al = new AttributeList();
            al.read(new DicomInputStream(selectedFile));
            DicomDirectory ddr = new DicomDirectory(al);
            return ddr;
        } catch (IOException | DicomException ex) {
            Logger.getLogger(DicomController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public static String getAttributes(Object lastSelectedPathComponent) {
        DicomDirectoryRecord ddr = (DicomDirectoryRecord) lastSelectedPathComponent;
        AttributeList al = ddr.getAttributeList();
        return al.toString();
    }

    public static Image getImage(Object lastSelectedPathComponent, String dicomDirPath) {
        DicomDirectoryRecord ddr = (DicomDirectoryRecord) lastSelectedPathComponent;
        Image img = null;
        
        System.out.println(ddr.getAttributeList().get(TagFromName.DirectoryRecordType).getSingleStringValueOrNull());

        if( ddr.getAttributeList().get(TagFromName.DirectoryRecordType).getSingleStringValueOrEmptyString().equals("IMAGE") ){
            String path = ddr.getAttributeList().get(TagFromName.ReferencedFileID).getDelimitedStringValuesOrNull();
            if( path != null ){
                String fullImagePath = dicomDirPath.replace("dicomdir","") + path;
                System.out.println(fullImagePath);
                try {
                    SourceImage sImg = new SourceImage(fullImagePath);
                    img = sImg.getBufferedImage();
                } catch (DicomException | IOException ex) {
                    Logger.getLogger(DicomController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return img;
    }

    public static void storeDICOM(Object lastSelectedPathComponent, String dicomDirPath, String host, int port, String aetitle) {
        DicomDirectoryRecord ddr = (DicomDirectoryRecord) lastSelectedPathComponent;
        
        if( ddr.getAttributeList().get(TagFromName.DirectoryRecordType).getSingleStringValueOrEmptyString().equals("IMAGE") ){
            String path = ddr.getAttributeList().get(TagFromName.ReferencedFileID).getDelimitedStringValuesOrNull();
            if( path != null ){
                String fullPath = dicomDirPath.replace("dicomdir","") + path;
                AttributeList al = new AttributeList();
                try {
                    al.read(new DicomInputStream(new File(fullPath)));
                    new StorageSOPClassSCU(host, port, aetitle, "JAVA", fullPath, SOPClass.MRImageStorage, al.get(TagFromName.SOPInstanceUID).getSingleStringValueOrEmptyString(), 0);
                } catch (IOException | DicomException | DicomNetworkException ex) {
                    Logger.getLogger(DicomController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
