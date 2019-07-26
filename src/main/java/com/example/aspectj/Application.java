package com.example.aspectj;

import com.example.beans.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;


@SpringBootApplication
public class Application {


    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        String[] beanNames = ctx.getBeanDefinitionNames();

        Arrays.sort(beanNames);

        for (String beanName : beanNames)
        {
            System.out.println(beanName);
        }

        User us = new User();
        us.setName("Manolo");
        System.out.println("Nombre en GET: "+us.getName());

        User u = (User)ctx.getBean("user");
        System.out.println("Nombre2 en GET: "+u.getName());
    }

    @Bean
    public User user(ApplicationContext ctx) {
        User user =  new User();
        user.setName("Otro");
        return user;
    }

}