/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file;

import com.gesmallworld.core.acpt.AcptException;
import com.gesmallworld.core.acpt.AcptType;
import com.gesmallworld.core.acpt.BuiltinType;
import static com.iberlan.file.shape.Globals.debug;
import java.io.IOException;

import java.util.ArrayList;
/**
 *
 * @author Fernando
 */
public class AcptRecord extends AcptTranslator {

    private AcptRow[] records;
    private FieldDescription[] model;
    
    public AcptRecord() throws IOException{
        super();
    }

    public AcptRecord(boolean autostart) throws IOException{
        super(autostart);
    }

    @Override
    public void read() throws IOException, AcptException {
        
        model = readModel();
        records = readRecords(model);
    }

    protected FieldDescription[] readModel() throws IOException, AcptException {
        notifier.NextPhase();
        
        int nFields = getUByte();        
        
        // Notificar inicio de la lectura del modelo
        notifier.SetNumRecords(nFields);
        notifier.NotifyStartPhase();
        
        FieldDescription[] eds = new FieldDescription[nFields];
        for (int i = 0; i < nFields; i++) {
            int typeno = getUShort();
            boolean geometric = (typeno >= 550);
            AcptType type = (AcptType)typeForId(typeno);
            String name = getString();
            boolean isNullable = getBoolean();
            eds[i] = new FieldDescription(name, type, isNullable, geometric);
           /* this.flush();
            sendOK();*/
        }
        
        // Notificar resultado de la lectura
        notifier.NotifyEndPhase(checkEnd());
        
        return eds;
    }

    protected AcptRow[] readRecords(FieldDescription[] fields) throws IOException, AcptException {
        
        notifier.NextPhase();
        int nRecs = (int) getUInt();
        int contOK = 0;
        
        // Notificar inicio de la lectura de registros
        notifier.SetNumRecords(nRecs);
        notifier.NotifyStartPhase();
        
        AcptRow[] recs = new AcptRow[nRecs];
        ArrayList<AcptRow> listRecords = new ArrayList<AcptRow>();
       
        for (int n = 1; n <= nRecs; n++) {
            try { 
                  notifier.IncrementProcessed();                  
                  boolean isValidData = getBoolean();
                  
                  if(isValidData){            
                    // dato ok
                        AcptRow rec = readRecord(fields);
                        recs[n-1] = rec;
                        listRecords.add(rec);
                        contOK = contOK + 1;}
                  else{                
                    //recoger error y enviar al log
                    String msjerror = getString();
                    notifier.NotifyError(msjerror);
                  }                  
            }catch(Exception ex){
                notifier.NotifyException(ex);
            }
        }

        recs = listRecords.toArray(new AcptRow[listRecords.size()]);
        
        notifier.NotifyEndPhase();
        notifier.NotifyResults();
        
        return recs;
    }

    protected AcptRow readRecord(FieldDescription[] fields) throws IOException, AcptException {
        
        AcptRow rec = new AcptRow();
        for (int i = 0; i < fields.length; i++) {
            FieldDescription field = fields[i];
            String name = field.getName();
            AcptType type = field.getType();
            AcptType recType = peekObject();
            Object obj = null;
            if (field.isNullable() && recType == BuiltinType.UNSET) {
                skipObject();
            } else if (field.isGeometric()) {
                skipObject();
                skipObject();
            } else {
                obj = getObject(type);
            }
            rec.put(name, obj);
        }            
        return rec;
    }
    
    public AcptRow[] getRecords() {
        return records;
    }

    public FieldDescription[] getModel() {
        return model;
    }
        
}
