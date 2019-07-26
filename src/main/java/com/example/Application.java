package com.example;

import com.example.beans.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Application {
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        User us = new User();
        us.setName("Manolo");
        logger.debug("Nombre en GET: " + us.getName());

        User u = (User) ctx.getBean("user");
        logger.debug("Nombre2 en GET: " + u.getName());
    }

    @Bean
    public User user(ApplicationContext ctx) {
        User user = new User();
        user.setName("Otro");
        return user;
    }

}