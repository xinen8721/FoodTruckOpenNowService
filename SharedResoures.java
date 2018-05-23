package com.foodtruck;

import java.util.HashMap;
import java.util.Map;

public class SharedResoures {
    public static Map<String, Integer> DAY_OF_WEEK_MAP = createMap();
    public static Map<String, Integer> createMap() {
        HashMap<String, Integer> map  = new HashMap<>();
        map.put("Mo", 1);
        map.put("Tu", 2);
        map.put("We", 3);
        map.put("Th", 4);
        map.put("Fr", 5);
        map.put("Sa", 6);
        map.put("Su", 7);
        return map;
    }
}
