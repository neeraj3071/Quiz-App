package com.demoproject.quizapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogWrapper {
    private final Logger logger;

    public LogWrapper(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable t) {
        logger.error(message, t);
    }

    // Add other logging methods as needed (debug, warn, etc.)
}
