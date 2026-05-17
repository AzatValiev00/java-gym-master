package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    Timetable timetable;
    Coach coach;
    Group groupChild;

    @BeforeEach
    void beforeEach() {
        timetable = new Timetable();
        coach = new Coach("Васильев", "Николай", "Сергеевич");
        groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {

        TrainingSession singleTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        List<TrainingSession> currentTimeSessions = mondaySessions.get(singleTrainingSession.getTimeOfDay());
        Assertions.assertEquals(1, currentTimeSessions.size());

        //Проверить, что за вторник не вернулось занятий
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        List<TrainingSession> currentTimeSessions = mondaySessions.get(mondayChildTrainingSession.getTimeOfDay());
        Assertions.assertEquals(1, currentTimeSessions.size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        TreeMap<TimeOfDay, List<TrainingSession>> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());
        NavigableSet<TimeOfDay> thursdaySessionsTimes = thursdaySessions.navigableKeySet();
        Assertions.assertEquals(new TimeOfDay(13, 0), thursdaySessionsTimes.getFirst());
        Assertions.assertEquals(new TimeOfDay(20, 0), thursdaySessionsTimes.getLast());

        // Проверить, что за вторник не вернулось занятий
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {

        TrainingSession singleTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> monday13_00Session = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        Assertions.assertEquals(1, monday13_00Session.size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)));
    }

    @Test
    void testGetMultipleTrainingSessionsForDayAndTime() {
        // Проверка добавления нескольких тренировок в один день и в одно время

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);

        List<TrainingSession> monday10_00Sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(10, 0));
        Assertions.assertEquals(2, monday10_00Sessions.size());

        Assertions.assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(11, 0)));
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeWhenMinutesDiffer() {
        // Проверка сортировки списка тренировок при одинаковых значениях часов, но при разных минутах

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 1));
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);

        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(2, mondaySessions.size());
        NavigableSet<TimeOfDay> mondaySessionsTimes = mondaySessions.navigableKeySet();
        Assertions.assertEquals(new TimeOfDay(10, 0), mondaySessionsTimes.getFirst());
        Assertions.assertEquals(new TimeOfDay(10, 1), mondaySessionsTimes.getLast());
        Assertions.assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(10, 2)));
    }

    @Test
    void testForInputtingIncorrectTime() {
        TrainingSession monday1ChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(24, 0));
        timetable.addNewTrainingSession(monday1ChildTrainingSession);
        TrainingSession monday2ChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(-1, 0));
        timetable.addNewTrainingSession(monday2ChildTrainingSession);
        TrainingSession monday3ChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 60));
        timetable.addNewTrainingSession(monday3ChildTrainingSession);
        TrainingSession monday4ChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, -1));
        timetable.addNewTrainingSession(monday4ChildTrainingSession);

        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).isEmpty());
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
    }

    @Test
    void testGetCountByCoaches() {
        Coach secondCoach = new Coach("Иванов", "Павел", "Евгеньевич");
        TrainingSession tuesdayChildTrainingSession = new TrainingSession(groupChild, secondCoach,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0));
        timetable.addNewTrainingSession(tuesdayChildTrainingSession);

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(15, 0));
        TrainingSession sundayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SUNDAY, new TimeOfDay(15, 0));

        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(sundayChildTrainingSession);

        Coach thirdCoach = new Coach("Ливанов", "Дмитрий", "Алексеевич");
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, thirdCoach, DayOfWeek.SATURDAY,
                new TimeOfDay(16, 30));
        TrainingSession fridayChildTrainingSession = new TrainingSession(groupChild, thirdCoach, DayOfWeek.FRIDAY,
                new TimeOfDay(20, 50));

        timetable.addNewTrainingSession(saturdayChildTrainingSession);
        timetable.addNewTrainingSession(fridayChildTrainingSession);

        List<CounterOfTrainings> coachCount = timetable.getCountByCoaches();
        CounterOfTrainings maxCoach = coachCount.get(0);
        CounterOfTrainings midCoach = coachCount.get(1);
        CounterOfTrainings minCoach = coachCount.get(2);

        Assertions.assertEquals(3, coachCount.size());
        Assertions.assertEquals(3, maxCoach.getCount());
        Assertions.assertEquals(2, midCoach.getCount());
        Assertions.assertEquals(1, minCoach.getCount());
    }

    @Test
    void testGetCountByCoachesWhenNoCoaches() {
        Assertions.assertTrue(timetable.getCountByCoaches().isEmpty());
    }

    @Test
    void testGetCountByCoachesWhenSingleCoach() {
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(15, 0));
        TrainingSession anotherMondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(15, 0));

        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(anotherMondayChildTrainingSession);

        List<CounterOfTrainings> coachCount = timetable.getCountByCoaches();
        CounterOfTrainings maxCoach = coachCount.getFirst();

        Assertions.assertEquals(1, coachCount.size());
        Assertions.assertEquals(3, maxCoach.getCount());
    }
}
