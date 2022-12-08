package com.orkDevEngine.core.engine.utils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Observer {

    protected static final Map<String, Long> notifications = new HashMap<>();

    public static void notify(String notification) {
        notifications.put(notification, Instant.now().toEpochMilli());
    }

    public static boolean containsNotification(String notification) {
        return notifications.containsKey(notification);
    }
}
