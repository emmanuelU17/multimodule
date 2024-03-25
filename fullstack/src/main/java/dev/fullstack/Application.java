package dev.fullstack;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints(value = {Application.MyRuntimeHints.class})
public class Application {

    static class MyRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            // migration
            hints.resources().registerPattern("db/migration/**");

            // car
            hints.serialization().registerType(Car.class);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}