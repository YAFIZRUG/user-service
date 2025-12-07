package edu.aston.service;

import edu.aston.dto.UserDto;
import edu.aston.model.User;
import edu.aston.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    private UserDto convertToDto(User user) {
        return new UserDto(
            user.getId(),
            user.getName(),
            user.getAge(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }
    
    private User convertToEntity(UserDto userDto) {
        User user = new User(
            userDto.getName(),
            userDto.getAge(),
            userDto.getEmail()
        );

        if (userDto.getId() != null) {
            user.setId(userDto.getId());
            user.setCreatedAt(userDto.getCreatedAt() != null ? 
                userDto.getCreatedAt() : LocalDateTime.now());
        }
        return user;
    }
    
    public UserDto createUser(UserDto userDto) {
        userDto.setId(null);
        userDto.setCreatedAt(LocalDateTime.now());
        
        User user = convertToEntity(userDto);
        User savedUser = userRepository.save(user);
        
        return convertToDto(savedUser);
    }
    
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));
        
        return convertToDto(user);
    }
    
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        
        return users.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));
        
        userDto.setCreatedAt(existingUser.getCreatedAt());
        userDto.setId(id);
        
        User userToUpdate = convertToEntity(userDto);
        User updatedUser = userRepository.save(userToUpdate);
        
        return convertToDto(updatedUser);
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Пользователь с ID " + id + " не найден");
        }
        
        userRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Пользователь с email " + email + " не найден");
        }
        return convertToDto(user);
    }
}