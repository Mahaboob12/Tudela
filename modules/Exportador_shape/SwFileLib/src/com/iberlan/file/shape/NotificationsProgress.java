/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file.shape;

/**
 *
 * @author Maria Jesús Santaolaya
 */
import com.iberlan.file.AcptRow;
import com.iberlan.file.shape.Globals.*;
import java.io.IOException;
import java.util.Date;

public class NotificationsProgress {
    
    
    private Date InitialTime;
    private Date EndTime;
    
    private int TotalRead = 0;
    private int OkRead=0;
    private int ErrorRead=0;
     
    private int TotalTransformed = 0;
    private int OkTranformed = 0;    
    private int ErrorTranformed = 0;
    
    private static NotificationsProgress notifier;
    private static Loggers loggers=null;
    private static Progress  progress=null;
    private static Phase currentPhase;
    private String ExportObjName="";
    
    private NotificationsProgress()  throws IOException{
        /*crear instancias de logger y progress */
            progress= Progress.getInstance();
            loggers = Loggers.getInstance();
    }
    
    public static NotificationsProgress getInstance() throws IOException    {
        if (notifier == null) {
            notifier = new NotificationsProgress();}
        return notifier;
    }
    
    public void Start() {
        /* Asiginar valores iniciales del proceso si se dispone de ellos ejecutando la PHASE_INIT*/
        PhaseInit();
    }
    
    public void End()throws IOException{
        /* Finalizar proceso, se elimina el log como manejador*/
        loggers.RemoveFileHandler();
    }
    
    public void PrintEnd(){
        /* Informacion sobre la finalizacion del proceso*/
        Boolean withErrors = ((progress.EndWithErrors()) ||(TotalRead>OkTranformed));
        PrintEndWithErrors(withErrors);
    }
    
    public void PrintEnd(Boolean withErrors){
        /* Informacion sobre la finalizacion del proceso
           indicando si se produjo algun error en funcion del valor WITHERRORS*/
        PrintEndWithErrors(withErrors);        
    }
    private void PrintEndWithErrors(Boolean withErrors){ 
        /* Imprimir informacion de finalizacion de proceso indicando
           si se produjo algun error durante el proceso, fecha de finalizacion y 
           numero de registros procesados correctamente*/
        loggers.PrintEnd(withErrors,OkTranformed,TotalRead,EndTime);
    }
    
    public void NextPhase() {
        int nextPhaseNum = currentPhase.GetPhaseNum() + 1;   
        
        switch(nextPhaseNum)
        {
            case 1:PhaseReadFileInfo();
                break;
            case 2:PhaseReadModel(); 
                break;
            case 3:PhaseReadRecords();
                break;
            case 4:PhaseTransform();
                break;
            case 5:PhaseExport();
        }
    }
       
    public void IncrementProcessed(){
        /* Incrementa numero de registros procesados */
        
        Boolean notificar = progress.IncrementProcess();
        if(notificar){
            loggers.PrintInLogAndErr(progress.GetMessageProgress());}        
    }
    public void SetNumRecords(int numRegs){
        /* Asignar numero de registros totales que se han de procesar */            
        progress.StartProcessCount(numRegs);
    }
    
    public void InitLog(String objName) throws IOException {
        /* Crear un log e imprime una cabecera con informacion del inicio
           nombre del objeto a procesar OBJNAME, fecha 
        */
        ExportObjName = objName;
        loggers.SetNewFileHandler(ExportObjName);
        loggers.PrintHead(ExportObjName,currentPhase.GetInitialTime()); 
    }
   
    private void PhaseInit(){
        /* Guardar fecha de inicio de la exportacion */
        currentPhase = new Phase(0);        
        progress.ResetProgress();
    }
    
    private void PhaseReadFileInfo() {
        /* Leer informacion del fichero, Guardar datos de la fase anterior */
        InitialTime = currentPhase.GetInitialTime();          
        currentPhase = new Phase(1);
        progress.ResetProgress();
    }
    
    private void PhaseReadModel()    {
        /* Leer informacion del modelo */
        currentPhase = new Phase(2);
        progress.ResetProgress();
    }
    
    private void PhaseReadRecords() {
        /* Leer informacion del fichero */
        
        // Guardar datos de la fase anterior
        // duracion
        currentPhase = new Phase(3); 
        progress.ResetProgress();
    }
    
    private void PhaseTransform()    {
        // Guardar datos de la fase anterior
        // duracion
        
        // Registros totales recibidos
        // Registros ok
        TotalRead = progress.GetTotalRwos();
        OkRead=progress.GetNumOKProcessed();
        ErrorRead=progress.GetNumErrorProcessed();
        
        // notificar finalizacion y resultados de la fase anterior
        currentPhase = new Phase(4);
        progress.ResetProgress();
    }
    
    private void PhaseExport()    {
        // Registros totales transformados
        // Registros ok
        TotalTransformed = progress.GetTotalRwos();
        OkTranformed = progress.GetNumOKProcessed();    
        ErrorTranformed = progress.GetNumErrorProcessed();
        currentPhase = new Phase(5);
        progress.ResetProgress();
    }
    
    public void NotifyStartPhase() {
        /* Informa hora del inicio de la fase y nombre */
        String msgInit = currentPhase.GetPhaseInfo().getMessageIni();
        String msg = "";
        if(progress.GetTotalRwos()==null){
            msg = String.format(Globals.NewLine+"\t-->"+
                                   msgInit);        }
        else{            
            msg = String.format(Globals.NewLine+"\t-->"+
                                   msgInit, progress.GetTotalRwos());}
       
        loggers.PrintInLogAndErr(Globals.NewLine+msg);        
    }
     
    public void NotifyEndPhase(){        
        /* Informa hora de finalizacion de la fase, el nombre
           y si se produjo algun error */        
        NotifyEndPhaseWithErrors(progress.EndWithErrors());
    }
    
    public void NotifyEndPhase(Boolean withErrors){       
        /* Informa hora de finalizacion de la fase, el nombre
           indicando si se produjo algun error en funcion del valor WITHERRORS*/
        NotifyEndPhaseWithErrors(withErrors);
    }
    
    private void NotifyEndPhaseWithErrors(Boolean withErrors){     
        /* Informa sobre la finalizacion de la fase, el nombre
           indicando si se produjo algun error en funcion del valor WITHERRORS*/
        String msgInit;
        String msg;
        PhasesInfoEnum cPhInfo = currentPhase.GetPhaseInfo();

        if (withErrors){ msgInit = cPhInfo.getMessageEndError();
        } else {msgInit = cPhInfo.getMessageEndOK();}
        
        msg = Globals.NewLine+"\t"+"   " +
              msgInit + Globals.NewLine;  
        
        loggers.PrintInLogAndErr(msg);
    }
     
    public void NotifyError(String msjerror) {
        /* Mostrar error en el log e incrementar el contador de errores*/
        progress.IncrementErrors();
        msjerror=msjerror.replaceAll("\n",Globals.NewLine);
        loggers.PrintInLogAndErr(msjerror);
    }
    
    public void NotifyCriticalException(Exception ex) {
        /* Mostrar excepcion critica en el log, ( esta excepcion interrumpira el proceso)*/
       loggers.PrintException(ex);
    }
    
    public void NotifyException(Exception ex) {
        /* Mostrar excepcion en el log e incrementar el contador de errores*/
        progress.IncrementErrors();
        loggers.PrintException(ex);
    }     
    
    public void NotifyException(Exception ex,AcptRow aRwo) {
        /* Mostrar excepcion en el log incluyendo el id del registro ARWO que
        provoco el error e incrementar el contador de errores*/
        progress.IncrementErrors();
        loggers.PrintExceptionWithRwoId(ex,aRwo);  
    }    
           
    public void NotifyResults(){        
        /* Mostrar en el log informacion sobre los resultados de la fase
           Registros procesados correctamente y nuumero de errores.*/
        String msgInit = currentPhase.GetPhaseInfo().getMessageResults();
        String msgResult = "\t   "+
                String.format(msgInit, progress.GetNumOKProcessed(),
                progress.GetNumErrorProcessed()) + Globals.NewLine;
        
        loggers.PrintInLogAndErr(msgResult);
    }     
    
    public static String getHeadErrorFileAccess(AcptRow a_rwo) {
        /* Devolver cabecera del error de acceso a fichero */        
        return loggers.getHeadErrorFileAccess(a_rwo);
    }
            
}
