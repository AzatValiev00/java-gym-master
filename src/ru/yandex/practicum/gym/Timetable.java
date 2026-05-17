package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();

        if (timeOfDay.getHours() < 0 || timeOfDay.getHours() > 23 || timeOfDay.getMinutes() < 0 ||
                timeOfDay.getMinutes() > 59) {
            System.out.println("Введено некорректное время!");
            return;
        }

        TreeMap<TimeOfDay, List<TrainingSession>> currentDaySessions = timetable.get(dayOfWeek);
        if (currentDaySessions == null) {
            currentDaySessions = new TreeMap<>();
            currentDaySessions.put(timeOfDay, new ArrayList<>(List.of(trainingSession)));
            timetable.put(dayOfWeek, currentDaySessions);
        } else {
            List<TrainingSession> currentTimeSessions = currentDaySessions.get(timeOfDay);
            if (currentTimeSessions == null) {
                currentTimeSessions = new ArrayList<TrainingSession>(List.of(trainingSession));
                currentDaySessions.put(timeOfDay, currentTimeSessions);
            } else {
                currentTimeSessions.add(trainingSession);
            }
        }
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (timetable.get(dayOfWeek) == null) {
            return new TreeMap<TimeOfDay, List<TrainingSession>>();
        }
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (timetable.get(dayOfWeek) == null) {
            return new ArrayList<TrainingSession>();
        }
        return timetable.get(dayOfWeek).get(timeOfDay);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachMap = new HashMap<>();
        if (timetable.isEmpty()) {
            System.out.println("Занятий нет!");
            return new ArrayList<CounterOfTrainings>();
        }
        for (TreeMap<TimeOfDay, List<TrainingSession>> currentDaySessions : timetable.values()) {
            for (List<TrainingSession> currentTimeSession : currentDaySessions.values()) {
                for (TrainingSession trainingSession : currentTimeSession) {
                    int count = coachMap.getOrDefault(trainingSession.getCoach(), 0);
                    coachMap.put(trainingSession.getCoach(), count + 1);
                }
            }
        }
        List<CounterOfTrainings> counter = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachMap.entrySet()) {
            counter.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        Collections.sort(counter);
        Collections.reverse(counter);
        return counter;
    }
}
