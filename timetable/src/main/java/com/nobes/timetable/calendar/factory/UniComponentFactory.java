package com.nobes.timetable.calendar.factory;


import com.nobes.timetable.calendar.factory.strategies.UniComponentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *  This class is a factory for selecting and creating UniComponentStrategy instances based on the given componentId.
 *
 * */
@Service
public class UniComponentFactory {

    @Autowired
    private final Map<String, UniComponentStrategy> strategies = new ConcurrentHashMap<>();

    /**
     * find the strategy according to the componentId
     * @param componentId the componentId used to select the appropriate UniComponentStrategy instance
     * @return the selected UniComponentStrategy instance
     *
     * */
    public UniComponentStrategy getStrategies(String componentId) {
        UniComponentStrategy reportInstance = this.strategies.get(componentId);
        return reportInstance;
    }

}
