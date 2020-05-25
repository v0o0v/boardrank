package net.boardrank.boardgame.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class UtilService {

    public static LocalDateTime getLocalDateTimeInUTC(){
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static LocalDateTime transUTCToCurrentZone(LocalDateTime utcDateTime){
        ZonedDateTime utc = ZonedDateTime.of(utcDateTime, ZoneOffset.UTC);
        ZonedDateTime currentZone = utc.withZoneSameInstant(ZoneId.systemDefault());

        return currentZone.toLocalDateTime();
    }

    public static void main(String[] args) {
        LocalDateTime utcNow = getLocalDateTimeInUTC();
        System.out.println(utcNow);

        LocalDateTime localDateTime = transUTCToCurrentZone(utcNow);
        System.out.println(localDateTime);
    }
}
