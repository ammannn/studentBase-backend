package com.university.mcmaster.utils;

import com.university.mcmaster.enums.DayPeriod;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.models.entities.RentalUnitFeatures;
import com.university.mcmaster.models.entities.Time;
import jakarta.servlet.http.HttpServletRequest;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.zone.ZoneRulesException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.university.mcmaster.utils.Constants.EMAIL_REGEX;

public class Utility {

    public static CustomUserDetails customUserDetails(HttpServletRequest request){
        if(null != request){
            Object temp = request.getAttribute("user");
            if(null != temp) return (CustomUserDetails) temp;
        }
        return null;
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static <T> boolean areListsEqual(List<T> list1, List<T> list2) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            T element1 = list1.get(i);
            T element2 = list2.get(i);
            if (false == element1.equals(element2)) {
                return false;
            }
        }
        return true;
    }

    public static double getAverageRating(Map<String, Integer> ratings) {
        int sum = 0;
        int totalCount = 0;
        if(null != ratings && false == ratings.isEmpty()){
            for (Map.Entry<String, Integer> entry : ratings.entrySet()) {
                int rating = Integer.parseInt(entry.getKey());
                int count = entry.getValue();
                sum += rating * count;
                totalCount += count;
            }
            return (double) sum / totalCount;
        }
        return 0.0;
    }

    public static <K, V> boolean areMapsEqual(Map<K, V> m1, Map<K, V> m2) {
        // Check if both maps are null
        if (m1 == null && m2 == null) {
            return true;
        }
        // Check if one of the maps is null while the other is not
        if (m1 == null || m2 == null) {
            return false;
        }
        // Check if the size of the maps is equal
        if (m1.size() != m2.size()) {
            return false;
        }
        // Check if each key-value pair in m1 exists in m2
        for (Map.Entry<K, V> entry : m1.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();

            if (!m2.containsKey(key) || !Objects.equals(value, m2.get(key))) {
                return false;
            }
        }
        return true;
    }

    public static long getTimeStamp(String date, Time time, String timeZone) {
        try {
            String dateTimeStr = date + " " + time.getHour() + ":" + time.getMinute();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, formatter);

            ZoneId zoneId = ZoneId.of(timeZone);
            ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

            return zonedDateTime.toInstant().toEpochMilli();
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + date);
            return -1;
        } catch (ZoneRulesException e) {
            System.err.println("Invalid time zone: " + timeZone);
            return -1;
        }
    }

    public static boolean verifyDateFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    public static long getTimeStamp(LocalDate date, String timeZone, Time time) {
        Map<String, Long> timestamps = new HashMap<>();

        // Define the time zone
        ZoneId zoneId = ZoneId.of(timeZone);

        // Convert start and end hours to 24-hour format
        int startHour24 = (time.getDayPeriod() == DayPeriod.PM && time.getHour() != 12) ? time.getHour() + 12 : time.getHour();

        // Construct start and end LocalDateTime objects
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.of(startHour24, time.getMinute()));

        // Convert LocalDateTime objects to ZonedDateTime with the specified time zone
        ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDateTime, zoneId);

        // Convert ZonedDateTime objects to epoch timestamps
        return startZonedDateTime.toEpochSecond();
    }
    public static Month getCurrentMonth(String timeZone) {
        // Get the current date and time in the specified time zone
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timeZone));
        // Extract and return the month
        return zonedDateTime.getMonth();
    }

    public static List<String> getRentalUnitFeatureList(RentalUnitFeatures features) {
        List<String> featureSearchList = new ArrayList<>();
        if(null == features) return featureSearchList;
        if(null != features.getFeaturesAmenities()){
            for (Map.Entry<String, Boolean> eminityEntry : features.getFeaturesAmenities().entrySet()) {
                if(eminityEntry.getValue()) featureSearchList.add(eminityEntry.getKey());
            }
        }
        if(null != features.getFeaturesUtilities()){
            for (Map.Entry<String, Boolean> utilityEntry : features.getFeaturesUtilities().entrySet()) {
                if(utilityEntry.getValue()) featureSearchList.add(utilityEntry.getKey());
            }
        }
        StringBuilder builder = new StringBuilder();
        if(null != features.getFeaturesNumbers()){
            if(null != features.getFeaturesNumbers().get("room") && features.getFeaturesNumbers().get("room") > 0){
                builder.append(features.getFeaturesNumbers().get("room")).append("b");
            }
            if(null != features.getFeaturesNumbers().get("hall") && features.getFeaturesNumbers().get("hall") > 0){
                builder.append(features.getFeaturesNumbers().get("hall")).append("h");
            }
            if(null != features.getFeaturesNumbers().get("kitchen") && features.getFeaturesNumbers().get("kitchen") > 0){
                builder.append(features.getFeaturesNumbers().get("kitchen")).append("k");
            }
        }
        String roomStr = builder.toString();
        if(false == roomStr.trim().isEmpty()){
            featureSearchList.add(roomStr);
        }
        return featureSearchList;
    }

    public static String getPlatformUrl() {
        return "http://localhost:4200";
    }
}

