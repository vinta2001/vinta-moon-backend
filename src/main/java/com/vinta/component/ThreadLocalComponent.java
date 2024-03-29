package com.vinta.component;

import org.springframework.stereotype.Component;

@Component
public class ThreadLocalComponent {
    public static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static String get() {
        return threadLocal.get();
    }

    public static void set(String userId) {
        threadLocal.set(userId);
    }
}
