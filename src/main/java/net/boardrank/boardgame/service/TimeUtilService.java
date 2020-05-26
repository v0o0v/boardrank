package net.boardrank.boardgame.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class TimeUtilService {

    public static LocalDateTime getLocalDateTimeInUTC(){
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static LocalDateTime transUTCToKTC(LocalDateTime utcDateTime){
        if(utcDateTime==null) return null;

        ZonedDateTime utc = ZonedDateTime.of(utcDateTime, ZoneOffset.UTC);
        ZonedDateTime currentZone = utc.withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        return currentZone.toLocalDateTime();
    }

    public static LocalDateTime transKTCToUTC(LocalDateTime currentZoneTime){
        if(currentZoneTime==null) return null;

        ZonedDateTime currentZone = ZonedDateTime.of(currentZoneTime, ZoneId.of("Asia/Seoul"));
        ZonedDateTime utc = currentZone.withZoneSameInstant(ZoneOffset.UTC);

        return utc.toLocalDateTime();
    }

    public static void main(String[] args) {
        LocalDateTime utcNow = getLocalDateTimeInUTC();
        System.out.println(utcNow);

        LocalDateTime localDateTime = transUTCToKTC(utcNow);
        System.out.println(localDateTime);

        LocalDateTime transUTC = transKTCToUTC(localDateTime);
        System.out.println(transUTC);
    }
}
