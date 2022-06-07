package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsDAOMapImpl implements MealsDAO{
    private AtomicInteger count;
    private Map<Integer, Meal> data;

    public MealsDAOMapImpl() {
        count = new AtomicInteger();
        data = new ConcurrentHashMap<>();
        data.put(count.incrementAndGet(), new Meal(count.get(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        data.put(count.incrementAndGet(), new Meal(count.get(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        data.put(count.incrementAndGet(), new Meal(count.get(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        data.put(count.incrementAndGet(), new Meal(count.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        data.put(count.incrementAndGet(), new Meal(count.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        data.put(count.incrementAndGet(), new Meal(count.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        data.put(count.incrementAndGet(), new Meal(count.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public void addMeal(Meal meal) {
        data.put(meal.getId(), meal);
        System.out.println(meal.getDescription());
    }

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Meal updateMeal(int id) {
        return data.get(id);
    }

    @Override
    public void deleteMeal(int id) {
        data.remove(id);
    }
}
