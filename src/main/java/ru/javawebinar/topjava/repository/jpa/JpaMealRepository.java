package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User ref = em.getReference(User.class, userId);
            meal.setUser(ref);
            em.persist(meal);
        } else {
            em.createQuery("UPDATE Meal m SET m.description=:description, m.calories=:calories, m.dateTime=:date_time WHERE m.id=:mid AND m.user.id=:usrId")
                    .setParameter("description", meal.getDescription())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("date_time", meal.getDateTime())
                    .setParameter("mid", meal.id())
                    .setParameter("usrId", userId)
                    .executeUpdate();
        }
        return meal;
    }

    @Transactional
    @Override
    public boolean delete(int id, int userId) {
        return em.createQuery("DELETE FROM Meal m WHERE m.id =: mid AND m.user.id =: usrId")
                .setParameter("usrId", userId)
                .setParameter("mid", id)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = em.createQuery("SELECT m FROM Meal m WHERE m.id =: mid AND m.user.id =: usrId", Meal.class)
                .setParameter("usrId", userId)
                .setParameter("mid", id)
                .getResultList();
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createQuery("SELECT m FROM Meal m WHERE m.user.id =: usrId ORDER BY m.dateTime DESC", Meal.class)
                .setParameter("usrId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createQuery("SELECT m FROM Meal m WHERE m.user.id =: usrId AND m.dateTime >=:  startDateTime AND m.dateTime <: endDateTime ORDER BY m.dateTime DESC", Meal.class)
                .setParameter("usrId", userId)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }
}