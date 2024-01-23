/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Fernando
 */
public enum IOrdinalEnum {
    NULL(0);
    private int val;
    IOrdinalEnum(int val){
        this.val = val;
    }
    public static IOrdinalEnum get(int val){
        IOrdinalEnum result = null;
        for (IOrdinalEnum action : IOrdinalEnum.values())
        {
            if (action.val == val){
                result = action;
                break;
            }
        }
        return result;
    }
}
