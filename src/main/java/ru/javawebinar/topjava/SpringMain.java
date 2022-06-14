package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
//        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
//            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
//            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
//            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
//        }

        MealRestController mealRestController = new MealRestController(new MealService(new InMemoryMealRepository()));

        System.out.println(mealRestController.getAll());
//        System.out.println("--------------------");
//        System.out.println(mealRestController.get(4));
//        System.out.println("--------------------");
//        System.out.println(mealRestController.create(new Meal(null, null, LocalDateTime.now(), "desc", 3000)));
//        System.out.println(mealRestController.getAll());
//        System.out.println("--------------------");
//        mealRestController.update(new Meal(null, null, LocalDateTime.now(), "dsljkfhgh", 800), 7);
//        System.out.println(mealRestController.getAll());
//        System.out.println("--------------------");
//        mealRestController.delete(6);
//        System.out.println(mealRestController.getAll());
    }
}
