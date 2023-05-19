package com.nobes.timetable.calendar.factory.strategies;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * public strategy interface
 * */
public interface UniComponentStrategy {
    HashMap handle(ArrayList<String> names, String term) throws Exception;

}
