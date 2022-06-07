package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealsDAOMapImpl;
import ru.javawebinar.topjava.dao.MealsDAO;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    MealsDAO mealsDAO = new MealsDAOMapImpl();
    private static final Logger log = getLogger(MealServlet.class);
    public static final int CALORIES_PER_DAY = 2000;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String s;
        if ((s = request.getParameter("action")) != null){
            if (s.equals("delete")) {
                int i = Integer.parseInt(request.getParameter("id"));
                mealsDAO.deleteMeal(i);
                log.debug("meals #{} deleted", i);
                response.sendRedirect("meals");
            }
            else if (s.equals("edit")){
                int i = Integer.parseInt(request.getParameter("id"));
                Meal meal = mealsDAO.getMeal(i);
                log.debug("meals #{} edited", i);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("add_edit_form.jsp").forward(request, response);
            }
        }
        else convertAndForward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect from POST to meals");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        int cal = Integer.parseInt(request.getParameter("calories"));
        String s = new String(request.getParameter("description").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        Meal meal = new Meal(dateTime, s, cal);
        String id = request.getParameter("id");
        if (id.isEmpty()) {
            log.debug("meals added");
            mealsDAO.addMeal(meal);
        }
        else {
            meal.setId(Integer.parseInt(id));
            log.debug("meals #{} updated", meal.getId());
            mealsDAO.updateMeal(meal);
        }
        convertAndForward(request, response);
    }

    private void convertAndForward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealsTo = MealsUtil.noTimeFiltered(mealsDAO.getAllMeals(), CALORIES_PER_DAY);
        request.setAttribute("mealsTo", mealsTo);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
