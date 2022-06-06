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
    private List<Meal> meals = Arrays.asList(
        new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
        new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
        new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
        new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
        new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
        new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
        new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    private Map<Integer, Meal> data = new ConcurrentHashMap<>();
    private AtomicInteger count = new AtomicInteger();

    {
        int i;
        while ((i = count.get()) < meals.size()){
            Meal meal = meals.get(i);
            meal.setId(i + 1);
            data.put(i, meal);
            count.incrementAndGet();
        }
    }

    @Override
    public void addMeal(Meal meal) {

    }

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateMeal(Meal meal) {

    }

    @Override
    public void deleteMeal(Meal meal) {

    }
}
