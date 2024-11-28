package com.banking.util;

import com.banking.entity.User;

public class ThreadLocale {

    // Thread-local variable for User
    private static ThreadLocal<User> user = new ThreadLocal<>();

    // Thread-local variable for loginId
    private static ThreadLocal<Long> loginId = new ThreadLocal<>();

    // Getter for the thread-local User
    public static User getUser() {
        return user.get();
    }

    // Setter for the thread-local User
    public static void setUser(User user) {
        ThreadLocale.user.set(user);
    }

    // Getter for the thread-local loginId
    public static Long getLoginId() {
        return loginId.get();
    }

    // Setter for the thread-local loginId
    public static void setLoginId(Long loginId) {
        ThreadLocale.loginId.set(loginId);
    }

    // Optional cleanup method to remove thread-local values
    public static void clear() {
        user.remove();
        loginId.remove();
    }
}
