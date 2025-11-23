package edu.aston;

import edu.aston.service.ViewService;

public class Main {

    public static void main(String[] args) {

        ViewService.printWelcome();
        ViewService.showMainPage();
    }
}

/*
 * System.out.println("\nHello World!\n");
 * System.out.println("\nHello World!\n");
 * 
 * SessionFactory sessionFactory = HibernateConfiguration.sessionFactory();
 * Session session = sessionFactory.openSession();
 * 
 * User user1 = new User("Vasya", 23, "mail@mail.com");
 * User user2 = new User("Volodya", 20, "gmail@gmail.com");
 * 
 * session.beginTransaction();
 * session.persist(user1);
 * session.persist(user2);
 * User userById1 = session.find(User.class, 1L);
 * User userById2 = session.find(User.class, 2L);
 * System.out.println(userById1);
 * System.out.println(userById2);
 * session.getTransaction().commit();
 * 
 * session.beginTransaction();
 * User userForUpdate = session.find(User.class, 1L);
 * userForUpdate.setAge(69);
 * System.out.println(userForUpdate);
 * session.getTransaction().commit();
 * 
 * session.beginTransaction();
 * User userForDelete = session.find(User.class, 2L);
 * session.remove(userForDelete);
 * session.getTransaction().commit();
 * 
 * sessionFactory.close();
 */