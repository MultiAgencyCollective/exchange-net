package controllers;

import java.io.File;
import java.io.IOException;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;

import play.data.validation.Check;
import play.db.jpa.Blob;

public final class MyChecks {

    public final class NameCheck extends Check {
        
        @Override
        public boolean isSatisfied(
            final Object project,
            final Object name
        ) {
            if (name == null) {
                return false;
            }
            if (!(name instanceof String)) {
                return false;
            }
            final String nameString = (String) name;
            if (nameString.length() == 0) {
                return false;
            }
            
            return true;
        }
    }
    
    public final class PhotoCheck extends Check {
        
        @Override
        public boolean isSatisfied(
            final Object project,
            final Object input
        ) {
            setMessage("Please upload a photo.");
            
            if (input == null) {
                return false;
            }
            if (!(input instanceof Blob)) {
                System.out.println("not a blob");
                return false;
            }
            final Blob myBlob = (Blob) input;
            final File blobFile = myBlob.getFile();
            
            // doesn't work: file mime type is application/octet-stream
            /*
            final String mimeType = 
                new MimetypesFileTypeMap().getContentType(blobFile);
            if (!mimeType.contains("image")) {
                System.out.println("not an image: " + mimeType);
                return false;
            }
            */
            
            String mimeType;
            try {
                mimeType = detectMimeType(blobFile);
                if (mimeType == null) {
                    System.out.println("not an image: " + mimeType);
                    return false;
                }
                if (!mimeType.contains("image")) {
                    System.out.println("not an image: " + mimeType);
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }


            // max file size of 15 MB
            final long maxFileBytes = 15728640L;
            if (blobFile.length() > maxFileBytes) {
                System.out.println("too large");
                setMessage("Image must be smaller than 15MB.");
                return false;
            }
            
            return true;
        }
        
        private String detectMimeType(final File file) throws IOException {
            TikaInputStream tikaIS = null;
            try {
                tikaIS = TikaInputStream.get(file);

                /*
                 * You might not want to provide the file's name. 
                 * If you provide an Excel
                 * document with a .xls extension, it will 
                 * get it correct right away; but if you provide an 
                 * Excel document with .doc extension, it will guess 
                 * it to be a Word document
                 */
                final Metadata metadata = new Metadata();
                // metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());

                final Detector detector = new DefaultDetector(
                    MimeTypes.getDefaultMimeTypes()
                );
                if (detector.detect(tikaIS, metadata) == null) {
                    return null;
                }
                
                return detector.detect(tikaIS, metadata).toString();
            } finally {
                if (tikaIS != null) {
                    tikaIS.close();
                }
            }
        }
    }
}
