package dev.cheun.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    // Session Factory
    // You should only every have one per application.
    // Session factories are very large objects.
    // Creating them multiple time would be very slow.
    // A singleton is a class that you only every have one of.
    private static SessionFactory sf;

    public static SessionFactory getSessionFactory() {
        if (sf == null) {
            Configuration cfg = new Configuration();
            sf = cfg.configure().buildSessionFactory();
        }
        return sf;
    }
}
