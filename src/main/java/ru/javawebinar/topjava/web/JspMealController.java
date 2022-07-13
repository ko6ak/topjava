package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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
    public String save(@RequestParam("id") String id, @RequestParam("dateTime") String dateTime,
                             @RequestParam("description") String description, @RequestParam("calories") int calories, Model model) {
        int userId = SecurityUtil.authUserId();
        final Meal meal = new Meal(LocalDateTime.parse(dateTime), description, calories);
        if (StringUtils.hasLength(id)) {
            assureIdConsistent(meal, Integer.parseInt(id));
            log.info("update {} for user {}", meal, userId);
            service.update(meal, userId);
        } else {
            checkNew(meal);
            log.info("create {} for user {}", meal, userId);
            service.create(meal, userId);
        }
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/meals")
    public String getAll(Model model) {
        log.info("getAll meals");
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        model.addAttribute("action", "create");
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") int id, Model model) {
        final Meal meal = service.get(id, SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id, Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "redirect:meals";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam("startDate") String requestStartDate, @RequestParam("startTime") String requestStartTime,
                         @RequestParam("endDate") String requestEndDate, @RequestParam("endTime") String requestEndTime, Model model) {
        LocalDate startDate = parseLocalDate(requestStartDate);
        LocalDate endDate = parseLocalDate(requestEndDate);
        LocalTime startTime = parseLocalTime(requestStartTime);
        LocalTime endTime = parseLocalTime(requestEndTime);
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        model.addAttribute("meals", MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }
}
