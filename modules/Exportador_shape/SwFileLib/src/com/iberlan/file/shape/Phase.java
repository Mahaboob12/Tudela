/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file.shape;

import java.util.Date;
import com.iberlan.file.shape.Globals.*;

/**
 *
 * @author Maria Jesús Santaolaya
 */
public class Phase {
    
    private Date InitialTime;
    private Date EndTime;
    private Long Duration;
    
      
    public PhasesInfoEnum CurrentPhaseInfo=null;
    
    private Phase(){}
    
    public Phase(int phase_value){
        // Validar si es un valor de fase valido
        
        InitialTime = new Date();
        SetPhaseInfo(phase_value);
    }
    
    public void SetEndTime()
    {
        EndTime = new Date();
    }
    
    public Date GetEndDate()
    {
        if (EndTime == null)
        {
            EndTime = new Date();
        }
        return EndTime;
    }
    
    public int GetPhaseNum()
    {
        return CurrentPhaseInfo.Value();
    }
    
       
    public Long GetDuration(){
        //Duration = (InitialTime - EndTime);
        return Duration;
    }
    
    public Date GetInitialTime()
    {
        return InitialTime;
    }
    
    public PhasesInfoEnum GetPhaseInfo()
    {
        return CurrentPhaseInfo;
    }
    private void SetPhaseInfo(int phaseNum)
    {  
        CurrentPhaseInfo=GetPhaseInfoByNum(phaseNum);
    }
    
    private PhasesInfoEnum GetPhaseInfoByNum(int phNum)
    {  
        PhasesInfoEnum cInfoPhase= null;
        switch(phNum){
            case 0: cInfoPhase=PhasesInfoEnum.INIT;
                break;
            case 1: cInfoPhase=PhasesInfoEnum.READ_FILEINFO;
                break;
            case 2: cInfoPhase=PhasesInfoEnum.READ_MODEL;
                break;
            case 3: cInfoPhase=PhasesInfoEnum.READ_RECORDS;
                break;
            case 4: cInfoPhase=PhasesInfoEnum.TRANSFORM;
                break;
            case 5: cInfoPhase=PhasesInfoEnum.EXPORT;
        }
        return cInfoPhase;
    }
}
