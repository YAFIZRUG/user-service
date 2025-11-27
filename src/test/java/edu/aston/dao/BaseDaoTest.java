package edu.aston.dao;

import edu.aston.model.User;
import jakarta.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Transactional
public abstract class BaseDaoTest {

    @Container
    @SuppressWarnings("resource")
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("user-service-test")
            .withUsername("postgres")
            .withPassword("root");

    protected static SessionFactory testSessionFactory;

    @BeforeAll
    static void setUpAll() {
        postgres.start();

        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop"); 
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.format_sql", "true");

        configuration.addAnnotatedClass(User.class);

        testSessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    static void tearDownAll() {
        if (testSessionFactory != null && testSessionFactory.isOpen()) {
            testSessionFactory.close();
        }
    }

    @BeforeEach
    void cleanUp() {
        try (Session session = testSessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clean up database", e);
        }
    }
}