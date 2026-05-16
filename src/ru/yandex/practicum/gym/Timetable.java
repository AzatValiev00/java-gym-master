package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();
    //TreeMap<TimeOfDay, List<TrainingSession>> currentDaySessions = new TreeMap<>();
    //List<TrainingSession> currentTimeSessions = new ArrayList<>();


    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании

        if (trainingSession.getTimeOfDay().getHours() < 0 || trainingSession.getTimeOfDay().getHours() > 23 ||
                trainingSession.getTimeOfDay().getMinutes() < 0 || trainingSession.getTimeOfDay().getMinutes() > 59) {
            System.out.println("Введено некорректное время!");
            return;
        }

        if (timetable.containsKey(trainingSession.getDayOfWeek())) {
            TreeMap<TimeOfDay, List<TrainingSession>> currentDaySessions = timetable.get(trainingSession.getDayOfWeek());
            if (currentDaySessions.containsKey(trainingSession.getTimeOfDay())) {
                List<TrainingSession> currentTimeSessions = currentDaySessions.get(trainingSession.getTimeOfDay());
                currentTimeSessions.add(trainingSession);
                currentDaySessions.put(trainingSession.getTimeOfDay(), currentTimeSessions);
            } else {
                currentDaySessions.put(trainingSession.getTimeOfDay(),
                        new ArrayList<TrainingSession>(List.of(trainingSession)));
                timetable.put(trainingSession.getDayOfWeek(), currentDaySessions);
            }
        } else {
            TreeMap<TimeOfDay, List<TrainingSession>> currentDaySessions = new TreeMap<>();
            currentDaySessions.put(trainingSession.getTimeOfDay(), new ArrayList<TrainingSession>(List.of(trainingSession)));
            timetable.put(trainingSession.getDayOfWeek(), currentDaySessions);
        }
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).get(timeOfDay);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachMap = new HashMap<>();
        if (timetable.isEmpty()) {
            System.out.println("Занятий нет!");
            return null;
        }
        for (Map.Entry<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> entry : timetable.entrySet()) {
            Map<TimeOfDay, List<TrainingSession>> daySession = entry.getValue();
            for (Map.Entry<TimeOfDay, List<TrainingSession>> insideEntry : daySession.entrySet()) {
                for (TrainingSession trainingSession : insideEntry.getValue()) {
                    int count = coachMap.getOrDefault(trainingSession.getCoach(),0);
                    coachMap.put(trainingSession.getCoach(), count + 1);
                }
            }
        }
        List<CounterOfTrainings> counter = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachMap.entrySet()) {
            counter.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        Collections.sort(counter);
        return counter;
    }
}
