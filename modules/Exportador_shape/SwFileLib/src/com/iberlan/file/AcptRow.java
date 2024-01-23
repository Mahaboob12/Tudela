/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Fernando
 */
public class AcptRow extends HashMap<String, Object>{
    
    private List<String> keyList = new ArrayList<String>();
    public static final String fieldName_ID = "sw_ids";
    
    public AcptRow(){
        super();
    }
    
    @Override
    public Object put(String key, Object val){
        Object result = super.put(key, val);
        keyList.add(key);
        return result;
    }
    public String toString(){
        StringBuilder result = new StringBuilder();
        for (String fieldName : this.keyList){
            result.append(fieldName);
            result.append(": ");
            Object obj = this.get(fieldName);
            if (obj == null){
                result.append("null");
            } else {
                result.append(this.get(fieldName).toString());
            }
            result.append(", ");
        }
        
        return result.toString();
    }
    
    public String fullRwoIds(){
        String rwoIds= null;
        rwoIds = (String) this.get(fieldName_ID);
        if(rwoIds == null){rwoIds = "";}
        System.err.print(String.format("Rwo: ",rwoIds));
        return rwoIds;        
    }
}
