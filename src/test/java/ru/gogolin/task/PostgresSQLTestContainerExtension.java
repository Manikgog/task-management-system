package ru.gogolin.task;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresSQLTestContainerExtension {
    private static final PostgreSQLContainer<?> postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:14.0")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");

        postgresContainer.start();
    }

    @DynamicPropertySource
    public static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        System.setProperty("spring.security.cors.allowedOrigins", "none");
        System.setProperty("spring.security.cors.allowedMethods", "none");
        System.setProperty("jwt.secret", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");
    }

}
