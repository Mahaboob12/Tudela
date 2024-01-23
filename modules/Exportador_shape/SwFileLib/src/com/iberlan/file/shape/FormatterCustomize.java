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


import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FormatterCustomize extends Formatter {
    public String format(LogRecord record) {
        return formatMessage(record);
    }
}
