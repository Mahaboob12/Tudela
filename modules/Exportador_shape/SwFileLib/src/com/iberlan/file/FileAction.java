/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file;

/**
 *
 * @author Fernando
 */
public enum FileAction {
    NONE(0), READ(2), WRITE(1);
    
    private int val;
    FileAction(int val){
        this.val = val;
    }
    public int getVal(){
        return val;
    }
    public static FileAction get(int val){
        FileAction result = null;
        for (FileAction action : FileAction.values())
        {
            if (action.val == val){
                result = action;
                break;
            }
        }
        return result;
    }
}
