package edu.aston.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    private Long id;
    
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    
    @NotNull(message = "Возраст должен быть указан")
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    private Integer age;
    
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;
    
    private LocalDateTime createdAt;
}