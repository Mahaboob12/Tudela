/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file.shape;


import com.iberlan.utilities.DebugTools;

/**
 *
 * @author Maria Jesús Santaolaya
 */
public final class Globals {
    
      
    public static DebugTools debug= DebugTools.getInstance();
    public static final String MSG_PROGRESS = "Procesados %s de %s.";
    public static final String NewLine = System.getProperty("line.separator");
    
    // Campo que tienen todas las geometrias textuales 
    final static String TextFieldName = "text";
    public static final String SufixStringType = ":java.lang.String";
    public static final String TextFieldType =TextFieldName + SufixStringType;
    
    //Prefijo sistema de coordenadas valido para Shape
    public static final String PREFIX_COORD_SYS = "EPSG:";
    
    // Tamaño maximo del nombre de los atributos en SHAPE
    public static final Short MAX_SIZE_ATTRIBUTE_NAME = 10;
    
    // Mensajes de texto
    public static final String ERROR_MESSAGE = "Se ha producido un ERROR durante el proceso de exportacion. ";
    
    public static final String LOG_FILE_NAME = "%hLog_";
    public static final String LOG_FILE_EXT = ".log";
    public static final String ERROR_FILE_ACCESS ="EXPORTACION INTERRUMPIDA POR PROBLEMAS DE ACCESO A FICHEROS";
    
    public static final String MSG_INIT_EXPORT = "INICIAR EXPORTACION ";
    public static final String MSG_END_EXPORT = "EXPORTACION %s FINALIZADA CON ";
    public static final String MSG_ID = "*** ID: ";  
    public static final String MSG_NUM_EXPORTED = "Exportados %s registros de un total de %s leidos.";
     
    public static final String MSG_END_ERROR = "ERRORES";
    public static final String MSG_END_OK = "EXITO";
    
    // En los errores de acceso a ficheros.
    // Limite de reintentos y total de errores permitidos antes de interrumpir la ejecucion
    public static final int MAX_IO_ERRORS = 0;
    public static final int MAX_ACCESS_FILES_ATTEMPTS = 3;
    
    // Nombres de las geometrias Shape
    public enum ShapeGeomNames {
        MULTILINE("MultiLineString"),
        LINE("LineString"),
        POINT("Point"),
        MULTIPOLIGON("MultiPolygon");  
        private String geomName;
        private ShapeGeomNames(String s)
        {
           geomName = s;
        }
        public String Value(){return geomName;}
    }
    
    // Phases de exportacion
    public enum PhasesInfoEnum {
        INIT(0, "","","",""),        
        READ_FILEINFO(1, "", "","",""),
        READ_MODEL(2,Constants.MSG_READ_MODEL,Constants.MSG_ERR_MODEL_READ,Constants.MSG_OK_MODEL_READ,""),
        READ_RECORDS(3,Constants.MSG_READ_RECORDS,Constants.MSG_OK_RECORDS_READ,
                       Constants.MSG_ERR_RECORDS_READ, Constants.MSG_RESULTS_READ_RECORDS),
        TRANSFORM(4,Constants.MSG_TRANSFORM,Constants.MSG_OK_TRANSFORM,
                       Constants.MSG_ERR_TRANSFORM, Constants.MSG_RESULTS_TRANSFORM),
        EXPORT(5,Constants.MSG_EXPORT,Constants.MSG_OK_EXPORT,
                       Constants.MSG_ERR_EXPORT, "");
        
        private int phaseNum;
        private String msgInitial="";
        private String msgEndOK="";
        private String msgEndErr="";        
        private String msgResults="";
        
        private PhasesInfoEnum(int ph_Num,String msg_Ini,String msg_EndOK,
                              String msg_EndErr, String msg_Results)
        {
           phaseNum = ph_Num;
           msgInitial = msg_Ini;
           msgEndOK = msg_EndOK;
           msgEndErr = msg_EndErr;
           msgResults = msg_Results;
        }
        
        public String getMessageIni(){return msgInitial;}        
        public String getMessageEndOK(){return msgEndOK;}        
        public String getMessageEndError(){return msgEndErr;}        
        public String getMessageResults(){return msgResults;}
        public int Value(){return phaseNum;}
        
        private static class Constants {
            
            public static final String MSG_READ_MODEL ="Leyendo Modelo, Num. Campos: %s";    
            public static final String MSG_ERR_MODEL_READ = "ERROR en definición de tipo.";    
            public static final String MSG_OK_MODEL_READ = "RECIBIDA definición de tipo.";
    
            public static final String MSG_READ_RECORDS = "Leyendo Registros SW, Num. Regs: %s";
            public static final String MSG_ERR_RECORDS_READ = "ERROR en la lectura de registros.";    
            public static final String MSG_OK_RECORDS_READ = "Registros leidos correctamente.";
            public static final String MSG_RESULTS_READ_RECORDS = "Leidos %s ; Se ignoraron %s ";
            
            public static final String MSG_TRANSFORM = "Transformar %s Geometrias";
            public static final String MSG_ERR_TRANSFORM = "ERROR en la Transformacion de geometrias.";    
            public static final String MSG_OK_TRANSFORM = "Geometrias transformadas correctamente.";
            public static final String MSG_RESULTS_TRANSFORM = "Transformadas %s ; Se ignoraron %s ";
            
            public static final String MSG_EXPORT = "Exportar a Shape";
            public static final String MSG_ERR_EXPORT = "Se produjo un error durante el proceso de exportacion.";    
            public static final String MSG_OK_EXPORT = "Registros exportados correctamente.";
        }
}
    
    
    
}
