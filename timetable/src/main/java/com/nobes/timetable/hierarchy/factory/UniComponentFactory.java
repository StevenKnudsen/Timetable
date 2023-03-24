package com.nobes.timetable.hierarchy.factory;


import com.nobes.timetable.hierarchy.factory.strategies.UniComponentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UniComponentFactory {

    @Autowired
    private final Map<String, UniComponentStrategy> strategies = new ConcurrentHashMap<>();

    public UniComponentStrategy getStrategies(String activityId) {
        UniComponentStrategy reportInstance = this.strategies.get(activityId);
        return reportInstance;
    }

}
