/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file;

import com.gesmallworld.core.acpt.AcptException;
import com.iberlan.file.shape.Globals;
import java.io.IOException;

/**
 *
 * @author Fernando
 */
public class AcptShapeFile extends AcptGeomRecord {

    private String filename;
    private String dirname;
    private String coordinate_sys;
    private String filename_without_ext;
            
    public AcptShapeFile() throws AcptException,IOException {
        super();
    }

    public AcptShapeFile(boolean autostart) throws AcptException,IOException {
        super(autostart);
    }

    public void read() throws IOException, AcptException {
        
        notifier.NextPhase();
        
        FileAction fileAction = readAction();
        
        filename = readFilename();      
        dirname = readDirname();        
        coordinate_sys = readCoordinateSystem();
        setFileNameWithoutExtension();
        
        //Notificar al log inicio del proceso
        initLog();
        
        //notifier.SetExportName(filename_without_ext);
                
        if (fileAction == FileAction.WRITE) {
            super.read();
            sendOK();
        } else {

        }

    }

    private void initLog() throws IOException {
        /* Notificar el nombre del objeto a exportar para crear el log de expotacion*/
        notifier.InitLog(filename_without_ext); 
    }
    
    private FileAction readAction() throws IOException {
        int action = (int) getByte();
        return FileAction.get(action);
    }

    private String readFilename() throws IOException {
        String result = "";
        result = getString();
        return result;
    }
    
    private String readCoordinateSystem() throws IOException {
        String result;
        result = getString();
        return result;
    }

    private String readDirname() throws IOException {
        String result = "";
        result = getString();
        return result;
    }
    
    private void setFileNameWithoutExtension() throws IOException {
        filename_without_ext = filename.substring(0, filename.lastIndexOf("."));
    }

    public String getFilename() {
        return filename;
    }

    public String getDirname() {
        return dirname;
    }
    
    public String getFileNameWithoutExtension() {
        return filename_without_ext;
    }
    
    public String getCoordinateSystem() {
        String coordSys = coordinate_sys!=null?coordinate_sys:"";
        return coordSys;
    }
    
    public String getCoordSysModel() {
        
        return "srid="+getCoordinateSystem();
    }
    
    public String getCoordSysShapeFile() {
        return Globals.PREFIX_COORD_SYS+getCoordinateSystem();
    }

}
