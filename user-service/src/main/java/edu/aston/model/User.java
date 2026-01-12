package edu.aston.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_age")
    private int age;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email")
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User(String name, int age, String email) {
        this.age = age;
        this.email = email;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", age=" + age + ", email=" + email + ", name=" + name + ", createdAt=" + createdAt
                + "]";
    }
}
