package edu.aston.service;

import edu.aston.dao.UserDao;
import edu.aston.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ViewServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private Scanner scanner;

    @BeforeEach
    void setUp() {

        try {
            var scannerField = ViewService.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            scannerField.set(null, scanner);

            var userDaoField = ViewService.class.getDeclaredField("userDao");
            userDaoField.setAccessible(true);
            userDaoField.set(null, userDao);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void showUsers_shouldDisplayUsersWhenListNotEmpty() {

        List<User> users = Arrays.asList(
            new User("User1", 25, "user1@example.com"),
            new User("User2", 30, "user2@example.com")
        );
        when(userDao.findAll()).thenReturn(users);

        ViewService.showUsers();

        verify(userDao).findAll();
    }

    @Test
    void showUsers_shouldDisplayEmptyMessageWhenNoUsers() {

        when(userDao.findAll()).thenReturn(List.of());

        ViewService.showUsers();

        verify(userDao).findAll();
    }

    @Test
    void deleteUser_shouldDeleteWhenUserExistsAndConfirmed() {

        User user = new User("Test User", 25, "test@example.com");
        when(scanner.nextLine()).thenReturn("1", "y");
        when(userDao.findById(1L)).thenReturn(user);

        ViewService.deleteUser();

        verify(userDao).findById(1L);
        verify(userDao).delete(1L);
    }

    @Test
    void deleteUser_shouldNotDeleteWhenUserNotFound() {

        when(scanner.nextLine()).thenReturn("999"); 
        when(userDao.findById(999L)).thenReturn(null);

        ViewService.deleteUser();

        verify(userDao).findById(999L);
        verify(userDao, never()).delete(anyLong());
    }

    @Test
    void deleteUser_shouldCancelWhenConfirmationNo() {

        User user = new User("Test User", 25, "test@example.com");
        when(scanner.nextLine()).thenReturn("1", "n"); 
        when(userDao.findById(1L)).thenReturn(user);

        ViewService.deleteUser();

        verify(userDao).findById(1L);
        verify(userDao, never()).delete(anyLong());
    }

    @Test
    void updateUser_shouldUpdateWhenUserExists() {
        
        User user = new User("Old Name", 25, "old@example.com");
        when(scanner.nextLine())
            .thenReturn("1")              
            .thenReturn("New Name")       
            .thenReturn("new@example.com")
            .thenReturn("30");            
        when(userDao.findById(1L)).thenReturn(user);

        ViewService.updateUser();

        verify(userDao).findById(1L);
        verify(userDao).update(user);
        assertEquals("New Name", user.getName());
        assertEquals("new@example.com", user.getEmail());
        assertEquals(30, user.getAge());
    }

    @Test
    void checkKey_shouldReturnValidKey() {

        when(scanner.nextLine()).thenReturn("3"); 

        String result = ViewService.checkKey();

        assertEquals("3", result);
    }

    @Test
    void checkKey_shouldRetryOnInvalidInput() {

        when(scanner.nextLine())
            .thenReturn("invalid") 
            .thenReturn("6")       
            .thenReturn("2");      

        String result = ViewService.checkKey();

        assertEquals("2", result);
    }
}