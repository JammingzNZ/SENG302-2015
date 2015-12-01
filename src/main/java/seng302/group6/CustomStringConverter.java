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
import seng302.group6.Model.ItemClasses.Story;

/**
 * CustomStringConverter
 * This is a custom string converter used by the backlog stories TableView.
 *
 * When editing a priority value in the table, this class attempts to convert it to
 * an integer. If it does not convert, it returns the value in the table to the original
 * priority. This is a temporary fix to a trivial problem.
 */
public class CustomStringConverter extends StringConverter<Integer> {

    private TableView table;

    public CustomStringConverter(TableView table){
        this.table = table;
    }

    /** {@inheritDoc} */
    @Override public Integer fromString(String value) {

        // Try and convert new value to an integer
        Integer newInt;
        try{
            newInt = Integer.valueOf(value);
        } catch(NumberFormatException e){

            try {
                // Get the story that is being edited and return the value to the original
                Story story = (Story) table.getSelectionModel().getSelectedItem();
                return story.getCurrentPriority();

            } catch(Exception ex){
                // Just in case.
                return 0;
            }
        }

        return newInt;
    }

    /** {@inheritDoc} */
    @Override public String toString(Integer value) {
        // If the specified value is null, return a zero-length String
        return (Integer.toString(((Integer)value).intValue()));
    }
}