/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file;

import com.gesmallworld.core.acp.AcptAcp;
import com.gesmallworld.core.acpt.AcptException;
import com.gesmallworld.core.acpt.AcptType;
import com.gesmallworld.core.acpt.BuiltinType;
import com.gesmallworld.core.version.PackageVersion;
import com.iberlan.file.shape.NotificationsProgress;
import java.io.IOException;

/**
 *
 * @author Fernando
 */
public abstract class AcptTranslator extends AcptAcp{
    
    public static NotificationsProgress notifier = null;
    
    protected int getMaxProtocol() {
        return 1;
    }

    protected int getMinProtocol() {
        return 1;
    }

    protected String getProgramId() {
        return "Translator";
    }

    public AcptTranslator() throws IOException {
        super();
        InitNotifier();
    }

    public AcptTranslator(boolean autostart) throws IOException {
        super(autostart);
        InitNotifier();
    }

    private void InitNotifier() throws IOException{        
        notifier = NotificationsProgress.getInstance();
    }
           
    protected void init(){
        new com.gesmallworld.core.acp.DeclareVersion();
        new com.gesmallworld.core.acpt.DeclareVersion();
        PackageVersion.printVersionsIfRequested(new String[]{"-v"});
        PackageVersion.requiresVersion("com.gesmallworld.core.acp", 1, 0);
        PackageVersion.requiresVersion("com.gesmallworld.core.acpt", 1, 0);       
        
    }

    public abstract void read() throws IOException, AcptException;
    
    protected boolean checkEnd() throws IOException {
        int end = getUByte();
        return (end == 255);
    }

    protected void sendOK() throws IOException {
        putUByte(255);
        this.flush();
    }
    protected void sendNOK() throws IOException {
        putUByte(254);
        this.flush();
    }

    protected AcptType typeForId(int id) {
        AcptType result = builtinTypes[1];
        if (id <= 64) {
            result = builtinTypes[id];
        }
        return result;
    }
    
    private AcptType[] builtinTypes = {
        null, //0
        BuiltinType.UNSET, //1
        BuiltinType.UINT8, //2
        BuiltinType.UINT16, //3
        BuiltinType.UINT32, //4
        BuiltinType.UINT64, //5
        BuiltinType.INT8, //6
        BuiltinType.INT16, //7
        BuiltinType.INT32, //8
        BuiltinType.INT64, //9
        BuiltinType.FLOAT32, //10
        BuiltinType.FLOAT64, //11
        BuiltinType.CHAR8, //12
        BuiltinType.CHAR16, //13
        BuiltinType.BOOL, //14
        BuiltinType.KLEENE, //15
        BuiltinType.SIMPLE_TIME, //16
        //BuiltinType.CALENDAR_SIMPLE_TIME, //16
        BuiltinType.DATE, //17
        //BuiltinType.CALENDAR,             //17
        BuiltinType.DATE_TIME, //18
        //BuiltinType.CALENDAR_DATE_TIME,   //18
        BuiltinType.DOUBLE_AS_INT, //19
        BuiltinType.SHORT_STRING8, //20
        BuiltinType.SHORT_STRING16, //21
        BuiltinType.LONG_STRING8, //22
        BuiltinType.LONG_STRING16, //23
        BuiltinType.EMPTY_VECTOR, //24
        BuiltinType.CHAR8_TO_16, //25
        BuiltinType.SHORT_STRING8_TO_16, //26
        BuiltinType.LONG_STRING8_TO_16, //27
        null, null, null, null,
        BuiltinType.COORD, //32
        BuiltinType.COORD3D, //33
        BuiltinType.COORD2H, //34
        null,
        BuiltinType.SIMPLE_SECTOR, //36
        BuiltinType.SECTOR_Z, //37
        BuiltinType.SECTOR_H, //38
        BuiltinType.CP_ARC, //39
        BuiltinType.CIRCLE, //40
        BuiltinType.TAN_ARC, //41
        BuiltinType.SECTOR, //42
        BuiltinType.SECTOR_ROPE, //43
        BuiltinType.GIS_ID, //44
        BuiltinType.BIGINT, //45
        null, null, null, null, null,
        null, null, null, null, null,
        null, null, null, null, null,
        null, null, null, null
    };

    public AcptType[] getBuiltinTypes() {
        return builtinTypes;
    }

}
