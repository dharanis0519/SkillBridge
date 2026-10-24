package com.skillbridge;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedRoles(RoleRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Role("Java Developer", "Java, OOP, SQL, JDBC, Git, REST API"));
                repository.save(new Role("Frontend Developer", "HTML, CSS, JavaScript, Responsive Design, Git"));
                repository.save(new Role("Data Analyst", "SQL, Excel, Data Visualization, Statistics, Communication"));
                repository.save(new Role("Backend Developer", "Java, SQL, REST API, Git, Authentication, Testing"));
            }
        };
    }
}
