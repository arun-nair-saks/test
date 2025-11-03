package com.saks.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4J {
    private Logger logger = null;

    public Log4J(final Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz);
    }

    public Logger getLogger() {
        return this.logger;
    }
}