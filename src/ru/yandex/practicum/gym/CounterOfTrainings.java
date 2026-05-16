package ru.yandex.practicum.gym;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    Coach coach;
    int count;

    public CounterOfTrainings(Coach coach, int count) {
        this.coach = coach;
        this.count = count;
    }

@Override
    public int compareTo(CounterOfTrainings o) {
return Integer.compare(o.count, this.count);
}

}
