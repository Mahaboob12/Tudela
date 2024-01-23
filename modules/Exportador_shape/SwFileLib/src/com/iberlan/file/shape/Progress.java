/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file.shape;

import java.io.IOException;

/**
 *
 * @author Maria Jesús Santaolaya
 */
public class Progress {
    
    private Integer TotalRwos=null;      // numero de registros totales a procesar
    private Integer ProcessedRwos=null;  // contador de registros procesados hasta el momento
    private int ProcessedErrors=0;       // contador de errores detectados durante el proceso
    
    public static final Integer Milestone=1000; //hito    
    private static Progress progress;    
    
    private Progress(){}
    
    public static Progress getInstance() throws IOException    {
        /*Devolver instancia de la clase*/
        if (progress == null) {
            progress = new Progress();}
        return progress;
    } 
    
    public int GetNumErrorProcessed()    {
        /*Devuelve el numero de errores*/
        return ProcessedErrors;
    }
    
    public Integer GetTotalRwos()    {
        /*Devuelve el numero total de elementos que se procesan*/
        return TotalRwos;
    }
    
    public Integer GetProcessedRwos()    {
        /*Devuelve el numero de elementos procesados hasta el momento*/
        return ProcessedRwos;
    }
    
    public int GetNumOKProcessed()    {
        /*Devuelve el numero de elementos procesados correctamente*/
        int numOk=0;        
        if (ProcessedRwos != null){
            numOk = ProcessedRwos - ProcessedErrors;}
        return numOk;
    }
    
    public void StartProcessCount(Integer numRwos)    {
        /*Iniciar cuenta del proceso*/
        ResetProgress();
        SetTotalRwos(numRwos);
    }
    
    public void ResetProgress()    {
        /*Reiniciar contadores del progreso*/
        TotalRwos=null;
        ProcessedRwos=null;
        ProcessedErrors=0;
    }
    
    private void SetTotalRwos(Integer numRwos)    {
        /*Asignar valor al numero Total de Rwos a procesar e 
          inicia a 0 el numero de registros procesados*/
        TotalRwos=numRwos;
        ProcessedRwos = 0;
    }       
    
    public Boolean IncrementProcess()    {
        /*Incrementa el numero de registros procesados y devuelve un valor indicando
          si hay que notificar la informacion de progreso*/
        Boolean notify = Boolean.FALSE;        
        ProcessedRwos = ProcessedRwos +1 ;
                
        // Se notifica cuando se han procesado un numero de elementos multiplo del hito y al finalizar
        // de procesar todos.
        if((((ProcessedRwos)%Milestone)==0)||
           (ProcessedRwos.equals(TotalRwos))){
                notify = Boolean.TRUE;}         
        return notify;
    }
    
    public void IncrementErrors()    {
        /*Incrementar numero de errores durante el proceso*/
        ProcessedErrors = ProcessedErrors + 1;
    }
    
    public String GetMessageProgress()    {
        /*Devuelve el mensaje de progreso que se muestra al notificar*/
        String message=Globals.NewLine + "\t\t"+
                       String.format(Globals.MSG_PROGRESS,ProcessedRwos,TotalRwos );               
        return message;
    }
    
    public Boolean EndWithErrors()    {
        /*Indica si se detecto algun error durante el proceso*/                  
        return (ProcessedErrors>0);
    }
}
