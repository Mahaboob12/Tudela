/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file;

import com.gesmallworld.core.acpt.AcptType;

/**
 *
 * @author Fernando
 */
public class FieldDescription {

    private String name;
    private AcptType type;
    private boolean nullable = false;
    private boolean geometric = false;
    private int index;

    public FieldDescription(String name, AcptType type, boolean nullable, boolean geometric) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.geometric = geometric;
        
        
    }


    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append((name!=null?name:"null")).append(": ");
        result.append((type!=null?type.toString():"null")).append(", ");
        result.append((nullable?"Nullable":""));
        result.append((geometric?"Geometric":""));
        return result.toString();
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public AcptType getType() {
        return type;
    }

    /**
     * @return the nullable
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    public boolean isGeometric() {
        return geometric;
    }
}
