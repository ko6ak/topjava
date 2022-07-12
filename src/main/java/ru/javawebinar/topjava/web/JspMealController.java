package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    @PostMapping("/meals")
    public String postCreate(HttpServletRequest request, Model model){
        int userId = SecurityUtil.authUserId();
        final Meal meal = getMeal(request);
        checkNew(meal);
        log.info("create {} for user {}", meal, userId);
        service.create(meal, userId);
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "redirect:meals";
    }

    @PostMapping("/meals")
    public String postUpdate(HttpServletRequest request, Model model){
        int userId = SecurityUtil.authUserId();
        if (StringUtils.hasLength(request.getParameter("id"))) {
            int id = getId(request);
            final Meal meal = getMeal(request);
            assureIdConsistent(meal, id);
            log.info("update {} for user {}", meal, userId);
            service.update(meal, id);
        }
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "redirect:meals";
    }

    @GetMapping("/meals")
    public String delete(HttpServletRequest request, Model model){
        int userId = SecurityUtil.authUserId();
        if (StringUtils.hasLength(request.getParameter("id"))) {
            int id = getId(request);
            log.info("delete meal {} for user {}", id, userId);
            service.delete(id, userId);
        }
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "redirect:meals";
    }

    @GetMapping("/meals")
    public String createOrUpdate(HttpServletRequest request, Model model){
        Meal meal = null;
        if (StringUtils.hasLength(request.getParameter("id"))) {
            meal = service.get(getId(request), SecurityUtil.authUserId());
        }
        if (meal == null) meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "forward:mealForm";
    }

    @GetMapping("/meals")
    public String getAll(Model model){
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
        return "forward:meals";
    }

    @GetMapping("/meals")
    public String filter(HttpServletRequest request, Model model){
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        model.addAttribute("meals", MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "forward:meals";
    }

    private Meal getMeal(HttpServletRequest request){
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
