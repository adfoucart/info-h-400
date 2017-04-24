/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infoh400_tp;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomDirectory;
import com.pixelmed.dicom.DicomDirectoryRecord;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.DicomInputStream;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.display.SourceImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTree;

/**
 *
 * @author Administrateur
 */
public class DicomController {
    
    public static String dicomDirPath = "";
    private static DicomDirectoryRecord ddr; 
    
    public static DicomDirectory getDICOMDIR(String path){
        DicomInputStream dis = null;
        try {
            DicomController.dicomDirPath = path;
            File f = new File(path);
            dis = new DicomInputStream(f);
            AttributeList al = new AttributeList();
            al.read(dis);
            return new DicomDirectory(al);
        } catch (IOException | DicomException ex) {
            Logger.getLogger(DicomController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dis.close();
            } catch (IOException ex) {
                Logger.getLogger(DicomController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }
    
    public static void selectDirectoryRecord(JTree tree){
        DicomController.ddr = (DicomDirectoryRecord) tree.getLastSelectedPathComponent();
    }

    public static BufferedImage getImageOrNull() {
        AttributeList al = ddr.getAttributeList();
        if( al == null ) return null;
        
        if( al.get(TagFromName.DirectoryRecordType).getSingleStringValueOrEmptyString().equalsIgnoreCase("IMAGE") ){
            String imageFileName = al.get(TagFromName.ReferencedFileID).getDelimitedStringValuesOrNull();
            imageFileName = dicomDirPath.replace("dicomdir","")+imageFileName;
            
            File f = new File(imageFileName);
            if( f.exists() && f.canRead() ){
                try {
                    SourceImage sImg = new SourceImage(imageFileName);
                    BufferedImage img = sImg.getBufferedImage();
                    return img;
                } catch (IOException | DicomException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return null;
    }

    public static String getAttributesAsString() {
        AttributeList al = ddr.getAttributeList();
        if( al == null ) return null;
        
        String s = "";
        Set<AttributeTag> keySet = al.keySet();
        for(AttributeTag key : keySet ){
            s += key.toString() + " : " + al.get(key).getDelimitedStringValuesOrEmptyString() + "\n";
        }
        
        return s;
    }
    
}
