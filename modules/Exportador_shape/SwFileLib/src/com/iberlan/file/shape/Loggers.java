/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file.shape;

import com.iberlan.file.AcptRow;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.Arrays;


/**
 *
 * @author Maria Jesús Santaolaya
 */
public class Loggers {
    
    private static Loggers loggers;
    
    // Logs
    private static final Logger LOGGER_RWO = Logger.getLogger(WriterShapeFile.class.getName());
    public static FileHandler currentFileHandler;    
    private String rwoName;        
    
    private Loggers(){}
    
    public static Loggers getInstance() throws IOException    {
        /*Devolver instancia de la clase*/
        if (loggers == null) {
            loggers = new Loggers();}
        return loggers;
    }
    
    public Logger getCurrentLogger() throws IOException    {
        /* Devolver Log actual si aun no existe se crea*/
        VerifyFileHandler(Boolean.FALSE);       
        return LOGGER_RWO;
    }
    
    public Logger getCurrentLogger(String rwo_name) throws IOException    {
        /* Devolver Log actual para el nombre de elemento RWO_NAME
           si aun no existe se crea*/
        SetNewFileHandler(rwo_name);     
        return LOGGER_RWO;
    }
    
    public void SetNewFileHandler(String rwo_name) throws IOException    {
        /*Crear Log para el nombre de elemento RWO_NAME e incluirlo como manejador
          si aun no existe.*/
        Boolean forceChangeHandler;
        
        //Verificar nombre de RWO
        if(rwo_name != null) {rwo_name = rwo_name.trim();}
        forceChangeHandler=(rwo_name!=rwoName);
        if(forceChangeHandler){SetRwoName(rwo_name);}
        
        VerifyFileHandler(forceChangeHandler);
    }
    
   
      
    private void VerifyFileHandler(Boolean forceChangeHandler) throws IOException    {   
        /*Verificar si el manejador ya existe o si hay que forzar que se asocie uno nuevo.
          Incluir lo si no existe*/
        if ((currentFileHandler == null) || forceChangeHandler)
        {
            ChangeFileHandler();}  
    }
    
    private void ChangeFileHandler() throws IOException    {
        /*Eliminar FileHandler Existente e incluir otro*/
        RemoveFileHandler();
        AddNewFileHandler();
    }
    
    private void SetRwoName(String rwo_name)     {
        /*Asignar nombre de elemento para el que se va a crear el log*/
        rwoName=rwo_name;
    }
        
    private void AddNewFileHandler()  throws IOException{
        /* Añade un manejador al Logger para volcar los errores en un
           fichero de texto */
        
        String actualRwoName="";
        String separating = "_";
        String formatDate = "yyyyMMdd_HH_m";
        
        if(rwoName != null ){
            actualRwoName = rwoName + separating;
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
        String textoFecha = formatter.format(new Date());
        String fileName = Globals.LOG_FILE_NAME+ actualRwoName + textoFecha + Globals.LOG_FILE_EXT;
       
        FileHandler fileTxt = new FileHandler(fileName);
        fileTxt.setFormatter(new FormatterCustomize());
        LOGGER_RWO.addHandler(fileTxt);
        currentFileHandler = fileTxt;    
    }
    
    public void RemoveFileHandler()  throws IOException{        
        /* Elimina manejador fichero de texto si existe */        
        if (currentFileHandler != null ){
            currentFileHandler.close();
            LOGGER_RWO.removeHandler(currentFileHandler);
            currentFileHandler = null;}    
    }
    
    public static String GetCurrentFormatDate()  {
        /* Imprimir por pantalla y en el Logger
           la fecha actual */
        GregorianCalendar fecha = new GregorianCalendar();
        String formatDate;
        //Obtenemos el valor del año, mes, día,
        //hora, minuto y segundo del sistema
        //usando el método get y el parámetro correspondiente
        int año = fecha.get(GregorianCalendar.YEAR);
        int mes = fecha.get(GregorianCalendar.MONTH);
        int dia = fecha.get(GregorianCalendar.DAY_OF_MONTH);
        int hora = fecha.get(GregorianCalendar.HOUR_OF_DAY);
        int minuto = fecha.get(GregorianCalendar.MINUTE);
        int segundo = fecha.get(GregorianCalendar.SECOND);
        
        formatDate = String.format("%s/%s/%s - %02d:%02d:%02d %n",
                                   dia,(mes+1),año,hora, minuto, segundo);
        
        return formatDate;
        
    }
 
    public void PrintHead(String rwo_name,Date startDate) {
        /*Imprime en el log un mensaje de inicio de proceso indicando
          fecha de inicio STARTDATE, nombre del elemento a procesar RWO_NAME*/
        
        String msgStart;
        
        if(rwo_name == null){rwo_name = "";}
        else{rwo_name=" "+rwo_name;}
        
        msgStart = Globals.MSG_INIT_EXPORT + rwo_name;
        printBlockText(msgStart,startDate);
    }
    
    public void PrintEnd(Boolean WithErr,int numRegOK,int numRegTotal,Date endDate){
        /*Imprime en el log un mensaje de finalizacion de proceso indicando
          fecha de finalizacion ENDDATE, numero de registros procesados correctamente NUMREG
          y si se produjo algun error durante el proceso WITHERR*/
        
        String msgSufix;
        String msgEnd;
        
        if(WithErr){msgSufix = Globals.MSG_END_ERROR;}
        else{msgSufix = Globals.MSG_END_OK;}
        String result = Globals.NewLine+"\t"+
                        String.format(Globals.MSG_NUM_EXPORTED, numRegOK,numRegTotal);
        
        msgEnd = String.format(Globals.MSG_END_EXPORT,rwoName) + msgSufix + result;        
        printBlockText(msgEnd,endDate);
    }
        
    private void printBlockText(String blockText,Date aDate) {
        /* Imprimir cabecera de incicio de exportacion o mensaje de finalizacion */
        
        String texto_bloque="";
        int i;
        int lines =3;
        char separatingChar = '@';
        int numRepeatChar=75;
        
        String formatDate = "dd/MM/yyyy HH:mm:ss";
               
        SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
        if(aDate==null){aDate=new Date();}
        String textoFecha = formatter.format(aDate);
        
        try{            
            for(i=1; i<=lines; i++) { 
                String texto="";
                switch(i){
                    case 1: case 3:
                        texto = Globals.NewLine + repeatChar(separatingChar,numRepeatChar);
                        texto = repeatString(texto,2) + Globals.NewLine;
                        break;
                    case 2:
                        texto = Globals.NewLine+"\t" + blockText +
                                Globals.NewLine+"\t" + textoFecha + Globals.NewLine;                        
                }              
                texto_bloque = texto_bloque + texto;
            }
            //Imprimir
            PrintInLogAndErr(texto_bloque);
        } catch (Exception ex) {
           // No ha sido posible imprimir la informacion en el log
           // Se notifica pero no se interrumpe el proceso de exportacion
            ex.printStackTrace();
        }         
    }
    
    public void PrintException(Exception ex) {
        /* Imprimir en el log la excepcion*/
        PrintExceptionWithRwoId(ex,null); 
    }
    
    public void PrintExceptionWithRwoId(Exception ex,AcptRow aRwo) {          
        /* Imprimir en el log la excepcion EX, incluyendo informacion del ARWO
         que ha provocado el error*/
        String error_message = getErrorMessage(null,aRwo); 
        PrintExceptionInLogAndErr(error_message,Level.SEVERE,ex);
    }
    
    private void PrintExceptionInLogAndErr(String str, Level aLevel,Exception a_ex) {
        try{
            Logger aLogger = getCurrentLogger();
            if(a_ex!=null){aLogger.log(aLevel,str,a_ex);}      
            else{aLogger.log(aLevel,str);            }
        } catch (Exception ex) {
           // No ha sido posible imprimir la informacion en el log
           // Se notifica pero no se interrumpe el proceso de exportacion
           ex.printStackTrace(); 
        } 
    }
    
    public void PrintInLogAndErr(String str) {
        PrintInLogAndErr(str,Level.INFO);
    }
    
    public void PrintInLogAndErr(String str, Level aLevel) {
        PrintExceptionInLogAndErr(str, aLevel,null);
    }       
    
    private static final String repeatChar(char c, int length) {
        /* Devuelve el caracter C repetido LENGTH  veces*/
        char[] data = new char[length];
        Arrays.fill(data, c);
        return new String(data);
    }
    
    private static final String repeatString(String str, int length) {
        /* Devuelve el String STR repetido LENGTH  veces */
        return new String(new char[length]).replace("\0", str);
    }
        
    public static String getHeadErrorFileAccess(AcptRow a_rwo) {
        /* Devolver cabecera del error de acceso a fichero */        
        return getErrorMessage(Globals.ERROR_FILE_ACCESS, a_rwo);
    }
    
    private static String getErrorMessage(String headMsg,AcptRow aRwo) {
        /* Devuelve el mensaje de error con cabecera HEADMSG e identificador
           de registro ARWO*/
        
        String error_message= Globals.NewLine;
        
        //Escribir cabecera
        if ((headMsg != null) &&(!headMsg.isEmpty()))
        {error_message=error_message+headMsg;}
        else
        {error_message= error_message+Globals.ERROR_MESSAGE;}
        
        //Escribir rwo_id
        if ((aRwo!=null) && (!aRwo.isEmpty())){
            error_message = error_message + Globals.NewLine +
                    Globals.MSG_ID + Globals.NewLine + aRwo.fullRwoIds();}
        return error_message;
    }
    
    
}
