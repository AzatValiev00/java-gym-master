package ru.yandex.practicum.gym;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    private Coach coach;
    private int count;

    public CounterOfTrainings(Coach coach, int count) {
        this.coach = coach;
        this.count = count;
    }

@Override
    public int compareTo(CounterOfTrainings o) {
return Integer.compare(this.count, o.count);
}

    public int getCount() {
        return count;
    }
}
