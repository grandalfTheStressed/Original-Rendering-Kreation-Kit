package com.orkDevEngine.core.engine.utils;

import com.orkDevEngine.core.engine.utils.exceptions.ClockException;

import java.util.HashMap;
import java.util.Map;

public class Clock {

    public enum TIME_UNIT {
        SECOND(1000000000l),
        MILLIS(1000000l),
        NANOS(1l),
        MINUTE(60000000000l),
        HOUR(3600000000000l);

        public final long TIME_VALUE;
        TIME_UNIT(long TIME_VALUE) {
            this.TIME_VALUE = TIME_VALUE;
        }
    }
    final static long START_TIME = System.nanoTime();
    private Map<String, Long> timer;

    public Clock() {
        timer = new HashMap<>();
    }

    public long getTimeElapsed() {
        return System.nanoTime() - START_TIME;
    }

    public void setTimedEvent(String event, TIME_UNIT time_unit, long time) throws ClockException{
        if(timer.containsKey(event))
            throw new ClockException("Timed Event " + event + " already exists!");

        timer.put(event, getTimeElapsed() + (time * time_unit.TIME_VALUE));
    }

    public void updateTimedEvent(String event, TIME_UNIT time_unit, long time) throws ClockException {
        if(!timer.containsKey(event))
            throw new ClockException("Timed Event " + event + " doesn't exist!");

        timer.put(event, getTimeElapsed() + (time * time_unit.TIME_VALUE));
    }

    public long checkTimedEvent(String event) throws ClockException {
        if(!timer.containsKey(event))
            throw new ClockException("Timed Event " + event + " doesn't exist!");

        return getTimeElapsed() - timer.get(event);
    }

    public void clearTimedEvent(String event) throws ClockException {
        if(!timer.containsKey(event))
            throw new ClockException("Timed Event " + event + " doesn't exist!");

        timer.remove(event);
    }

    public void setLapEvent(String event) throws ClockException{
        event += ":lap";
        if(timer.containsKey(event))
            throw new ClockException("Lap Event " + event + " already exists!");

        timer.put(event, getTimeElapsed());
    }

    public long checkLapEvent(String event) throws ClockException{
        event += ":lap";
        if(!timer.containsKey(event))
            throw new ClockException("Lap Event " + event + " doesn't exist!");

       return getTimeElapsed() - timer.get(event);
    }

    public void clearLapEvent(String event) throws ClockException{
        event += ":lap";
        if(!timer.containsKey(event))
            throw new ClockException("Lap Event " + event + " doesn't exist!");

        timer.remove(event);
    }

    public void clearEvents() {
        timer.clear();
    }
}
