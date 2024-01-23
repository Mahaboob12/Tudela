/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file.shape;

import com.gesmallworld.core.acpt.AcptException;
import com.gesmallworld.core.geometry.gis.Chain;
import com.gesmallworld.core.geometry.gis.Point;
import com.gesmallworld.core.geometry.gis.SimplePoint;
import com.gesmallworld.core.geometry.gis.SimpleChain;
import com.gesmallworld.core.geometry.gis.SimpleArea;
import com.gesmallworld.core.geometry.gis.Area;
import com.gesmallworld.core.geometry.gis.Text;
import com.iberlan.file.AcptRow;
import com.iberlan.file.AcptShapeFile;
import com.iberlan.file.FieldDescription;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.net.URL;
import org.geotools.data.Transaction;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.FileDataStoreFactorySpi;

import org.geotools.referencing.CRS;
import org.geotools.data.FeatureStore;
import org.opengis.feature.type.AttributeDescriptor;

import com.gesmallworld.core.acpt.AcptType;
import com.iberlan.file.TransformGeoms;
import static com.iberlan.file.shape.Globals.debug;
import com.iberlan.geometry.PseudoAreaExport;
import java.lang.management.MemoryMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

/**
 *
 * @author Fernando
 *         Maria Jesús Santaolaya
 */

public class WriterShapeFile {
 
    // Logs
    public static int CURRENT_IO_ERRORS = 0;    
    public static NotificationsProgress notifier = null;
     
    public static void main(String[] args) {
        
        try {
             debug.appendToFile("Exportar");
            //Iniciar logger y progress
            InitNotifier();
            
            AcptShapeFile shFile = new AcptShapeFile(true);                
            // Leer modelo y registros
            shFile.read();            
            // Exportar registros en Shape
            writeFile(shFile);
            
        } catch (IOException ex) {
            NotifyCriticalException(ex);
        } catch (AcptException ex) {
            NotifyCriticalException(ex);
        } catch (Exception ex) {
            NotifyCriticalException(ex);
        }
        finally
        {
            EndProcess();
        }
    }
    
    private static void EndProcess(){
        // Notificar resultados finales
        // Cerrar ficheros log si hay alguno abierto
            if(notifier != null){
                try{
                    notifier.PrintEnd();
                    notifier.End();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
    }
    

    private static void InitNotifier()throws IOException{
        /* Iniciar notificador */
        notifier = NotificationsProgress.getInstance();
        notifier.Start();
    }
    
    private static void NotifyCriticalException(Exception ex){
        /* Notificar excepcion que interrumpe la exportacion */
        if(notifier != null){
           try{
                notifier.NotifyCriticalException(ex);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }
    private static SimpleFeatureType getType(String featureName, 
            FieldDescription[] model, String coordSys) throws SchemaException{
        
        /* Devuelve un SimpleFeatureType indicando la estructura de tipos de datos 
           que se van a exportar a SHAPE segun MODEL*/
            
        int i;
        String fields=null;
        String separatingAttb =",";
        String separatingAttbInfo =":";
        
        for(i=0; i<model.length; i++) {            
            
            FieldDescription field = model[i];
            String name = field.getName();
            AcptType typeF = field.getType();
            String fielddesc=null;
            String anotacion_text = null;
            
             
            // El tamaño máximo del nombre de campo en Shape son 10 caracteres
            if (name.length() > Globals.MAX_SIZE_ATTRIBUTE_NAME ){
                    name = name.substring(0, Globals.MAX_SIZE_ATTRIBUTE_NAME - 1);
            }
                
            if (typeF.isBuiltin()){
                fielddesc = name+separatingAttbInfo+
                            typeF.getTypeClass().getName(); 
            }
            else
            {
      
                String ShField =null;                
                if (typeF.getTypeClass() == Chain.class)
                {
                    ShField = Globals.ShapeGeomNames.MULTILINE.Value();
                }
                else if (typeF.getTypeClass() == SimpleChain.class)
                {
                    ShField = Globals.ShapeGeomNames.LINE.Value();               
                }
                else if ((typeF.getTypeClass() == Point.class)||
                        (typeF.getTypeClass() ==  SimplePoint.class))
                {
                    ShField = Globals.ShapeGeomNames.POINT.Value();
                }
                else if ((typeF.getTypeClass() == Area.class)||
                        (typeF.getTypeClass() ==  SimpleArea.class)||
                        (typeF.getTypeClass() ==  PseudoAreaExport.class))
                {
                    ShField = Globals.ShapeGeomNames.MULTIPOLIGON.Value();
                }
                else if (typeF.getTypeClass() == Text.class)
                {
                    ShField = Globals.ShapeGeomNames.POINT.Value(); 
                    anotacion_text = Globals.TextFieldType; //:java.lang.String
                }
                
                name="the_geom";    
                fielddesc = "*"+name+separatingAttbInfo+
                            ShField+separatingAttbInfo+
                            coordSys;
                
                // Verificar si es una geometia TEXT para añadir el texto como atributo
                if ((anotacion_text != null) && (!anotacion_text.equals(""))){
                    fielddesc = fielddesc +separatingAttb+ anotacion_text;
                }            
            }
            
            if (fields == null){
                fields = fielddesc;
            }else{
                fields = fields +separatingAttb+fielddesc;
            }
        }
       
        SimpleFeatureType type = DataUtilities.createType(featureName, fields);
        
        return type;
    }
    
    private static void writeFile(AcptShapeFile shapeFile)  throws Exception{

        //Recuperar registros y modelo              
        
        AcptRow[] rows = null;
        FieldDescription[] model = null;        
        SimpleFeatureType featureType = null;
        String filename = null;
        String dirname = null;
        String fwithoutext = null;
        File newFile = null;
        
        int numRegs = 0;
        int numOK = 0;
        int cont = 0;
                    
        try{
            notifier.NextPhase();
            rows = shapeFile.getRecords();
            model = shapeFile.getModel();
                                   
            //Crear Fichero Shape    
            filename = shapeFile.getFilename();
            dirname = shapeFile.getDirname();
            fwithoutext = shapeFile.getFileNameWithoutExtension();//recuperar nombre del fichero con extension .shp
            newFile = new File(dirname,filename);
            
            //crear SimpleFeatureType
            featureType = getType(fwithoutext, model,shapeFile.getCoordSysModel());
            
            int i;
            numRegs = rows.length;
        
            ArrayList<SimpleFeature> listfeatures = new ArrayList<SimpleFeature>();
            
            notifier.SetNumRecords(numRegs);
            notifier.NotifyStartPhase();
            
            for(i=0; i<rows.length; i++) {  
                AcptRow currentRow = null;
                try {
                    
                    notifier.IncrementProcessed();
                    currentRow = rows[i];
                    SimpleFeature aFeature = getRwoFeatureCollection(featureType,currentRow,model);
                    listfeatures.add(aFeature);                                  
                } catch (IOException ioex) {
                    if (IsExceededNumberOfIOErrors()){
                    // Se han producido demasiados errores de acceso consecutivos
                    // detener la exportacion 
                        throw new IOException(notifier.getHeadErrorFileAccess(currentRow),ioex);//ioex;
                    }                   
                } catch (Exception ex) {
                   // Se ha producido un error al intentar generar el objeto Shape
                   // notificar e ignorar dicho elemento en la exportacion
                   notifier.NotifyException(ex,currentRow);                      
               }                 
            }  
       
            
            notifier.NotifyEndPhase();
            notifier.NotifyResults();
                    
            rows= null;
            URL URL_File = newFile.toURI().toURL();
            String ShapeCoordinateSystem = shapeFile.getCoordSysShapeFile();
            shapeFile = null;
            
            
            ExportRwos(featureType,ShapeCoordinateSystem,URL_File,listfeatures);
            
        } catch (Exception ex) {
            throw ex;
        }  
        finally {//            
        }           
        
    }
    
    private static void ExportRwos(SimpleFeatureType featureType,
            String ShapeCoordinateSystem,URL URL_File,
            ArrayList<SimpleFeature> listfeatures)  throws Exception{
            /*
                Exportar la lista de features
            */
        FileDataStoreFactorySpi factory; 
        ListFeatureCollection collectionFeatures;
        ShapefileDataStore myData=null;
        Map<String, Serializable> params;
        FeatureStore<SimpleFeatureType, SimpleFeature> featureStore;
        
        try{
            
            notifier.NextPhase();
            notifier.SetNumRecords(listfeatures.size());
            notifier.NotifyStartPhase();
        
            factory = new ShapefileDataStoreFactory(); 
        
            //Crear esquema de fichero 
            params = new HashMap<>();
            params.put("url", URL_File); //location of a resource, used by file reading datasources
            params.put("create spatial index", Boolean.TRUE);
            myData = (ShapefileDataStore)factory.createNewDataStore(params);  
            myData.createSchema(featureType);  //is called to set up the contents. This method will delete 
            //any existing local resources or throw an IOException if the DataStore is remote.
        
            //asignar sistema de coordenadas 
            myData.forceSchemaCRS(CRS.decode(ShapeCoordinateSystem));
            
            //         
            String typeName = myData.getTypeNames()[0];
            featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>)
                            myData.getFeatureSource(typeName);
        
            collectionFeatures = new ListFeatureCollection(featureType,listfeatures); 
            
            // Exportar
            exportRwo(collectionFeatures,featureStore);
            
        } catch (Exception ex) {
            throw ex;
        }  
        finally {
            // is called when the application is shut down 
            if(myData != null){myData.dispose();}                
            notifier.NotifyEndPhase();
        }
        
     }
    private static Boolean IsExceededNumberOfIOErrors()  throws Exception{
        /*
            Verifica si se han producido mas errores de acceso a ficheros de los permitidos
            y lo notifica al log
        */
        Boolean IsExceeded=(CURRENT_IO_ERRORS >=Globals.MAX_IO_ERRORS);       
        return IsExceeded;
       
    } 
    
    private static Boolean IsExceededAttempsAccessFile(int attemp_number)  throws Exception{
        /*
            Verifica si se ha superado el numero maximo permitido de reintentos de acceso 
            al mismo fichero
        */
        Boolean IsExceeded=(attemp_number >=Globals.MAX_ACCESS_FILES_ATTEMPTS);       
        return IsExceeded;
       
    } 
    
    private static SimpleFeature getRwoFeatureCollection(SimpleFeatureType featureType, 
             AcptRow currentRow, FieldDescription[] model)  throws Exception{
 
        SimpleFeature featurenew = SimpleFeatureBuilder.build(featureType, new Object[]{}, null );
        String defaultGeomName = featureType.getGeometryDescriptor().getLocalName();
            
        try {
            int i,j;
            
            // Se lleva otro contador para la descripcion del tipo de campo ya que en el caso
            // del texto se exportan dos valores a partir del campo leido, la geometria y el texto
            j=-1;
            
            for(i=0; i<model.length; i++) {
                
                FieldDescription field = model[i];
                String fld_name = field.getName();
                j = j+ 1;  
                
                //Atributos
                AttributeDescriptor attb = featureType.getDescriptor(j);
                String attb_name = attb.getLocalName();
                
                if (attb_name.equals(Globals.TextFieldName)) continue;
                Object obj = currentRow.get(fld_name);   
                
                if (field.isGeometric()){   
                    if (obj instanceof Chain){
                        //MultiLineString
                        featurenew.setAttribute(defaultGeomName,TransformGeoms.createShapeMultiLine((Chain) obj));
                      } else if (obj instanceof SimpleChain){
                        //LineString
                        featurenew.setAttribute(defaultGeomName,TransformGeoms.createShapeLine((SimpleChain) obj));                          
                      } else if((obj instanceof Point)|| (obj instanceof SimplePoint)){
                        //Point
                        featurenew.setAttribute(defaultGeomName,TransformGeoms.createShapePoint(obj));
                      } else if(obj instanceof Area){
                        //multiPolygon
                        featurenew.setAttribute(defaultGeomName,TransformGeoms.createShapeArea((Area)obj));
                       } else if(obj instanceof PseudoAreaExport){
                        //multiPolygon
                        featurenew.setAttribute(defaultGeomName,TransformGeoms.createShapeArea((PseudoAreaExport)obj));
                      } else if (obj instanceof SimpleArea){
                        //Polygon                           
                        featurenew.setAttribute(defaultGeomName,TransformGeoms.createShapeArea((SimpleArea)obj));
                      } else if(obj instanceof Text){
                        //Text, se exporta la geometria y el texto como atributo
                        Text geom_text = (Text)obj;
                        featurenew.setAttribute(defaultGeomName,TransformGeoms.createShapePoint(geom_text));
                        featurenew.setAttribute(Globals.TextFieldName,geom_text.string);
                        // incrementar contador ya que se vuelcan 2 atributos
                        j = j+1;
                      }        
                } else{
                    //Atributos
                    featurenew.setAttribute(attb_name,currentRow.get(fld_name));
                }  
            } 
        } catch (Exception ex) {
            throw ex;
        } 
        return featurenew;
    }  
     
     
    private static Boolean exportRwo(ListFeatureCollection collectionFeatures,
            FeatureStore<SimpleFeatureType, SimpleFeature> featureStore)  throws Exception{
        
        Boolean ok = Boolean.FALSE;
        String aHandle = "add"; // "create"
        Transaction transaction = null;
        int attempt_number = 0;
        
        
        
        do {
            try {
                // used to populate the contents 
                attempt_number = (attempt_number + 1);
                
                transaction = new DefaultTransaction(aHandle);
                featureStore.setTransaction(transaction);
                featureStore.addFeatures(collectionFeatures);
                
                transaction.commit();
                
                // Errores continuos producidos
                CURRENT_IO_ERRORS = 0;
                ok = Boolean.TRUE;
               
                            
            } catch (IOException problem) {            
       
                ok = Boolean.FALSE;                
                if(IsExceededAttempsAccessFile(attempt_number)){
                    // superado el maximo numero de intentos permitido
                    CURRENT_IO_ERRORS = CURRENT_IO_ERRORS +1;  
                    problem.printStackTrace();
                    transaction.rollback();
                    throw problem;
                }      
                else
                {
                    // esperar e intentar de nuevo
                    transaction.rollback();
                    //transaction.wait();
                    //Thread.sleep(1000); 
                }
                //LOGGER_RWO.log(Level.SEVERE, ERROR_MESSAGE, problem);
            } catch (Exception ex) {
                //System.err.println("ERROR");
                ok = Boolean.FALSE;
                throw ex;
            } finally {
              transaction.close();
              //transaction.getState(ok);           
            }  
            
        
        } while((!IsExceededAttempsAccessFile(attempt_number)) && (!ok));
        
        return ok;
    }
    
    private static void PrintMemoryUse()
    {
        int MEGABYTE = (1024*1024);
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        MemoryUsage memNonHeap=memoryBean.getNonHeapMemoryUsage();
        
        long maxMemory = heapUsage.getMax() / MEGABYTE;
        long usedMemory = heapUsage.getUsed() / MEGABYTE;
        long committedMemory = heapUsage.getCommitted() / MEGABYTE;
        
        long maxMemoryN = memNonHeap.getMax() / MEGABYTE;
        long usedMemoryN = memNonHeap.getUsed() / MEGABYTE;
        long committedMemoryN = memNonHeap.getCommitted() / MEGABYTE;
        
        
        System.err.println("");
        System.err.println("*******************************************************************************");
        System.err.println("Heap Memory Use :" + usedMemory + "M/" + maxMemory + "M");
        System.err.println("Heap Committed Memory :" + committedMemory + "M");
        System.err.println("Non Heap Memory Use :" + usedMemoryN + "M/" + maxMemoryN + "M");
        System.err.println("Non Heap Committed Memory :" + committedMemoryN + "M");
        System.err.println("*******************************************************************************");
        System.err.println("");
    }

}


