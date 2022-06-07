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
        if ((s = request.getParameter("action")) != null && s.equals("delete")) {
            int i = Integer.parseInt(request.getParameter("id"));
            mealsDAO.deleteMeal(i);
            log.debug("meals #{} deleted", i);
            response.sendRedirect("meals");
            return;
        }
        else if ((s = request.getParameter("action")) != null && s.equals("edit")){
            int i = Integer.parseInt(request.getParameter("id"));
            Meal meal = mealsDAO.updateMeal(i);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("add_edit_form.jsp").forward(request, response);
            return;
        }

        List<MealTo> mealsTo = MealsUtil.noTimeFiltered(mealsDAO.getAllMeals(), CALORIES_PER_DAY);
        request.setAttribute("mealsTo", mealsTo);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect from POST to meals");
        int i = Integer.parseInt(req.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        int cal = Integer.parseInt(req.getParameter("calories"));
        Meal meal = new Meal(i, dateTime, req.getParameter("description"), cal);
        mealsDAO.addMeal(meal);
        List<MealTo> mealsTo = MealsUtil.noTimeFiltered(mealsDAO.getAllMeals(), CALORIES_PER_DAY);
        req.setAttribute("mealsTo", mealsTo);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }
}
