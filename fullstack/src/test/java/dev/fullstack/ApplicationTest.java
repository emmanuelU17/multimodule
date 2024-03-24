package dev.fullstack;

import org.springframework.boot.SpringApplication;

class ApplicationTest {

    public static void main(String[] args) {
        SpringApplication
                .from(Application::main)
                .with(TestConfig.class)
                .run(args);
    }

}