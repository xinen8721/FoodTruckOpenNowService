package com.foodtruck;

import java.time.DayOfWeek;
import java.util.*;

public class FoodTruckInfo {
    private String name;
    private String address;
    private Map<DayOfWeek, List<Integer>> openDaysAndHours;
    private boolean shouldUseDefault;

    public FoodTruckInfo(String allInfo) {
        String[] info = allInfo.split(",");
        if(info.length == 2) {
            shouldUseDefault = true;
        }
        openDaysAndHours = new HashMap<>();
        buildOpenHoursMap(info, shouldUseDefault);
    }

    public void buildOpenHoursMap(String[] info, boolean shouldUseDefault) {
        this.name = info[0];
        this.address = info[1];
        if(shouldUseDefault) {
            // In case there is no time available in CSV for certain foodtruck, by default, it will be open Mon-Sun, 10AM-5PM
            for(DayOfWeek dow : DayOfWeek.values()) {
                openDaysAndHours.put(dow, Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17));
            }
        } else {
            String[] timeInfos = info[2].split(";");
            //timeInfo will be the format of (Mo-Fr:10AM-6PM) OR (Mo/Tu/Th:10AM-6PM)
            for(String timeInfo : timeInfos) {
                String[] parts = timeInfo.split(":");
                String openDays = parts[0];
                String openHours = parts[1];
                List<Integer> hourList = getHours(openHours);

                if(openDays.contains("-")) {
                    String[] days = parts[0].split("-");
                    int start = SharedResoures.DAY_OF_WEEK_MAP.get(days[0]);
                    int end = SharedResoures.DAY_OF_WEEK_MAP.get(days[1]);

                    for(int i = start; i <= end; i++) {
                        DayOfWeek dow = DayOfWeek.of(i);
                        openDaysAndHours.put(dow, new ArrayList<>(hourList));
                    }
                } else if(openDays.contains("/")) {
                    String[] days = openDays.split("/");
                    for(String day : days) {
                        int dayValue = SharedResoures.DAY_OF_WEEK_MAP.get(day);
                        DayOfWeek dow = DayOfWeek.of(dayValue);
                        openDaysAndHours.put(dow, new ArrayList<>(hourList));
                    }
                }
            }
        }

    }

    public List<Integer> getHours(String hourString) {
        List<Integer> res = new ArrayList<>();
        String[] hourGroups = hourString.split("/");
        for(String hours : hourGroups) {
            String[] hourParts = hours.split("-");
            String startHour = hourParts[0];
            String endHour = hourParts[1];
            int start = 0, end = 0, i=0;

            //Parse Start Time
            if(startHour.contains("AM")){
                i=0;
                while(i < startHour.length() && Character.isDigit(startHour.charAt(i++)));
                int tmp = Integer.parseInt(startHour.substring(0,i-1));
                start = tmp == 12 ? 0 : tmp;

            } else {
                i=0;
                while(i < startHour.length() && Character.isDigit(startHour.charAt(i++)));
                int tmp = Integer.parseInt(startHour.substring(0,i-1));
                start = tmp == 12 ? 12 : tmp + 12;
            }
            //Parse End Time
            if(endHour.contains("AM")){
                i=0;
                while(i < endHour.length() && Character.isDigit(endHour.charAt(i++)));
                int tmp = Integer.parseInt(endHour.substring(0,i-1));
                end = tmp == 12 ? 0 : tmp;

            } else {
                i=0;
                while(i < endHour.length() && Character.isDigit(endHour.charAt(i++)));
                int tmp = Integer.parseInt(endHour.substring(0,i-1));
                end = tmp == 12 ? 12 : tmp + 12;
            }

            for(int idx=start; idx < end; idx++) {
                res.add(idx);
            }
        }

        return res;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<DayOfWeek, List<Integer>> getOpenDaysAndHours() {
        return openDaysAndHours;
    }
}
