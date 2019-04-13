package de.easter;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class EasterCalculator {
    private static final Duration FULL_MOON_PERIOD = Duration.ofSeconds(2551392);
    private static final MonthDay SPRING_BEGIN = MonthDay.of(3,21);
    private static final LocalDateTime KNOWN_FULL_MOON = LocalDateTime.of(LocalDate.of(2019,3,21), LocalTime.of(2,43));

    private EasterCalculator(){}

    private static LocalDate calculateFullMoon(int year) throws IllegalArgumentException{
        if(year < 0){
            throw new IllegalArgumentException("There was no easter before Jesus existed!");
        }
        LocalDateTime spring = LocalDateTime.of(LocalDate.of(year,SPRING_BEGIN.getMonth(),SPRING_BEGIN.getDayOfMonth()),LocalTime.of(0,0));
        LocalDateTime moonPhase;
        if(year >= 2019){
            moonPhase = KNOWN_FULL_MOON.plusSeconds(FULL_MOON_PERIOD.toSeconds() * ((Math.abs(year-2019))) * 12);
            while(!moonPhase.isAfter(spring)){
                moonPhase = moonPhase.plusSeconds(FULL_MOON_PERIOD.toSeconds());
            }
        } else{
            moonPhase = KNOWN_FULL_MOON.minusSeconds(FULL_MOON_PERIOD.toSeconds() * ((Math.abs(year-2019))) * 12);
            while (Math.abs(ChronoUnit.DAYS.between(spring, moonPhase)) > 30){
                moonPhase = moonPhase.minusSeconds(FULL_MOON_PERIOD.toSeconds());
            }
        }
        return moonPhase.toLocalDate();
    }

    public static LocalDate calculateEaster(int year){
        if(year != 2019) {
            return calculateFullMoon(year).with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        } else{
            return KNOWN_FULL_MOON.plusSeconds(FULL_MOON_PERIOD.toSeconds()).toLocalDate().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        }
    }

    public static void main(String[] args) {
        System.out.println(calculateEaster(2038));
        System.out.println(calculateEaster(2019));
        System.out.println(calculateFullMoon(2019));
    }
}
