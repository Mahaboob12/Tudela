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
public enum FileBlock {
    NONE(0), ACTION(1), FILENAME(2), MODEL(3), RECORD(4), END(5);
    
    private int val;
    FileBlock(int val){
        this.val = val;
    }
    public int getVal(){
        return val;
    }
    public static FileBlock get(int val){
        FileBlock result = null;
        for (FileBlock action : FileBlock.values())
        {
            if (action.val == val){
                result = action;
                break;
            }
        }
        return result;
    }
}
