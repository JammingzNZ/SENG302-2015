/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package seng302.group6;

import javafx.scene.control.TableView;
import javafx.util.StringConverter;
import seng302.group6.Model.ItemClasses.Task;

import java.text.DecimalFormat;

/**
 * CustomDoubleConverter
 * This is a custom string converter used to convert a String to the Double.
 *
 * When editing a priority value in the table, this class attempts to convert it to
 * an Double. If it does not convert, it returns the value in the table to the original
 * priority. This is a temporary fix to a trivial problem.
 */
public class CustomDoubleConverter extends StringConverter<Double> {

    private TableView table;


    public CustomDoubleConverter(TableView table){
        this.table = table;
    }

    /** {@inheritDoc} */
    @Override public Double fromString(String value) {
        Double newDouble;
        DecimalFormat df = new DecimalFormat("#.##");

        try {
            newDouble = Double.valueOf(value);
            newDouble = Double.valueOf(df.format(newDouble));
        }
        catch(NumberFormatException e) {
            try {
                Task task = (Task) table.getSelectionModel().getSelectedItem();
                return task.getEstimation();
            } catch(Exception ex){
                // Just in case.
                return 0.00;
            }

        }
        return newDouble;
    }


    /** {@inheritDoc} */
    @Override public String toString(Double value) {
        // If the specified value is null, return a zero-length String
        return (Double.toString(((Double)value).doubleValue()));
    }
}