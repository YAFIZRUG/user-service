package edu.aston.service;

import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.aston.dao.UserDao;
import edu.aston.dao.UserDaoImpl;
import edu.aston.model.User;

public class ViewService {

    private static boolean isRunning = true;
    private static Scanner scanner = new Scanner(System.in);
    private static UserDao userDao = new UserDaoImpl();
    private static final Logger logger = LogManager.getLogger(ViewService.class);

    public static void showMainPage() {

        while (isRunning) {

            System.out.println("\nВыберите действие:");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Обновить информацию о пользователе");
            System.out.println("3. Удалить пользователя");
            System.out.println("4. Просмотреть список пользователей");
            System.out.println("5. Выход из приложения\n");

            String key = checkKey();

            switch (key) {

                case "1" -> addUser();
                case "2" -> updateUser();
                case "3" -> deleteUser();
                case "4" -> showUsers();
                case "5" -> exitApp();
            }
        }
    }

    public static void printWelcome() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║                 USER SERVICE v1.0                  ║");
        System.out.println("║              Database Management Tool              ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.println("║   Create      Read        Update      Delete       ║");
        System.out.println("║   PostgreSQL      Hibernate           Java         ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
        System.out.println();
    }

    public static void addUser() {
        try {
            logger.info("Начало операции: создание пользователя");
            System.out.print("\nВведите имя: ");
            String name = scanner.nextLine();

            System.out.print("\nВведите email: ");
            String email = scanner.nextLine();

            System.out.print("\nВведите возраст: ");
            int age = Integer.parseInt(scanner.nextLine());

            User user = new User(name, age, email);

            userDao.create(user);
            logger.info("Пользователь успешно создан: ID={}, email={}", user.getId(), user.getEmail());

        } catch (Exception e) {
            logger.error("Error creating user", e);
            e.printStackTrace();
            logger.error("Ошибка при создании пользователя", e);
        }
    }

    public static void showUsers() {
        logger.info("Начало операции: получение списка пользователей");

        try {
            List<User> users = userDao.findAll();
            logger.info("Получено {} пользователей из БД", users.size());

            if (users.isEmpty()) {
                System.out.println("\nСписок пользователей пуст\n");
            } else {
                System.out.println("\nСписок пользователей:");
                System.out.println("┌─────┬──────────────────┬────────────────────┬─────┬─────────────────────┐");
                System.out.println("│ ID  │ Имя              │ Email              │ Возр│ Дата создания       │");
                System.out.println("├─────┼──────────────────┼────────────────────┼─────┼─────────────────────┤");

                for (User user : users) {
                    String formattedDate = user.getCreatedAt().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

                    System.out.printf("│ %-3d │ %-16s │ %-18s │ %-3d │ %-19s │\n",
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getAge(),
                            formattedDate);
                }
                System.out.println("└─────┴──────────────────┴────────────────────┴─────┴─────────────────────┘");
                System.out.println();
            }
        } catch (Exception e) {
            logger.error("Ошибка при получении списка пользователей", e);
        }
    }

    public static void deleteUser() {
        logger.info("Начало операции: удаление пользователя");

        try {
            System.out.print("\nВведите ID пользователя для удаления: ");
            Long id = Long.parseLong(scanner.nextLine());
            logger.info("Поиск пользователя для удаления: ID={}", id);

            User user = userDao.findById(id);
            if (user == null) {

                logger.warn("Пользователь с ID={} не найден для удаления", id);
                System.out.println("\nПользователь с ID " + id + " не найден\n");
                return;
            }

            System.out.println("\nВы действительно хотите удалить пользователя:");
            System.out.println("ID: " + user.getId() + ", Имя: " + user.getName() + ", Email: " + user.getEmail());
            System.out.print("Подтвердите удаление (y/n): ");

            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (confirmation.equals("y")) {
                userDao.delete(id);
                logger.info("Пользователь успешно удален: ID={}", id);
                System.out.println("\nПользователь удален!\n");
            } else {
                logger.info("Удаление пользователя отменено: ID={}", id);
                System.out.println("\nУдаление отменено\n");
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении пользователя", e);
            System.out.println("\nОшибка при удалении пользователя: " + e.getMessage());
        }
    }

    public static void updateUser() {
        logger.info("Начало операции: обновление пользователя");

        try {
            System.out.print("\nВведите ID пользователя для обновления: ");
            Long id = Long.parseLong(scanner.nextLine());

            logger.info("Поиск пользователя для обновления: ID={}", id);

            User user = userDao.findById(id);
            if (user == null) {
                logger.warn("Пользователь с ID={} не найден для обновления", id);
                System.out.println("\nПользователь с ID " + id + " не найден\n");
                return;
            }

            logger.info("Найден пользователь для обновления: ID={}, текущие данные: name={}, email={}, age={}",
                    user.getId(), user.getName(), user.getEmail(), user.getAge());

            System.out.println("\nТекущие данные пользователя:");
            System.out.println("ID: " + user.getId() + ", Имя: " + user.getName() +
                    ", Email: " + user.getEmail() + ", Возраст: " + user.getAge());

            System.out.print("\nВведите новое имя (оставьте пустым чтобы не менять): ");
            String newName = scanner.nextLine();
            if (!newName.trim().isEmpty()) {
                user.setName(newName);
            }

            System.out.print("Введите новый email (оставьте пустым чтобы не менять): ");
            String newEmail = scanner.nextLine();
            if (!newEmail.trim().isEmpty()) {
                user.setEmail(newEmail);
            }

            System.out.print("Введите новый возраст (оставьте пустым чтобы не менять): ");
            String ageInput = scanner.nextLine();
            if (!ageInput.trim().isEmpty()) {
                user.setAge(Integer.parseInt(ageInput));
            }

            logger.info("Обновление пользователя: новые данные - name={}, email={}, age={}",
                    user.getName(), user.getEmail(), user.getAge());

            userDao.update(user);

            logger.info("Пользователь успешно обновлен: ID={}", user.getId());
            System.out.println("\nДанные пользователя обновлены!\n");

        } catch (Exception e) {
            logger.error("Ошибка при обновлении пользователя", e);
            System.out.println("\nОшибка при обновлении пользователя: " + e.getMessage());
        }
    }

    public static String checkKey() {

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("\nОшибка: Введите значение!\n");
                continue;
            }

            if (input.matches("[1-5]")) {
                return input;
            } else {
                System.out.println("\nОшибка: Введите число от 1 до 5!\n");
            }
        }
    }

    private static void exitApp() {
        logger.info("Завершение работы приложения");
        System.out.println("\nВыход из приложения...\n");
        isRunning = false;
        scanner.close();
    }
}
