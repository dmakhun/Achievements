package com.edu.academy.exception;

public class AchievementTypeManagerException extends Exception {

    public AchievementTypeManagerException(String message, Exception e) {
        super(message, e);
    }

    public AchievementTypeManagerException(String message) {
        super(message);
    }

}
