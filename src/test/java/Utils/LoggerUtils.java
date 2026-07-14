package Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LoggerUtils {

    private static Logger getCallingClassLogger() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // stackTrace[0] = getStackTraceElement
        // stackTrace[1] = getCallingClassLogger
        // stackTrace[2] = actual calling method
        String callingClassName = stackTrace[3].getClassName();
        
        // Set MDC context for routing appender
        MDC.put("class", callingClassName);
        
        return LoggerFactory.getLogger(callingClassName);
    }

    public static Logger getLogger(Class<?> clazz) {
        MDC.put("class", clazz.getName());
        return LoggerFactory.getLogger(clazz);
    }


    public static void logRequest(String url) {
        Logger logger = getCallingClassLogger();
        logger.info("\n========== API REQUEST ==========");
        logger.info("URL : " + url);
    }

    public static void logRequestwithbody(String url, String body) {
        Logger logger = getCallingClassLogger();
        logger.info("\n========== API REQUEST ==========");
        logger.info("URL : " + url);
        logger.info("REQUEST BODY : \n" + body);
    }

    public static void logInfo(String message) {
        Logger logger = getCallingClassLogger();
        logger.info(message);
    }
    public static void loggerInfo(String message,String message2) {
        Logger logger = getCallingClassLogger();
        logger.info(message);
        logger.info(message2);
    }

    public static void logDebug(String message) {
        Logger logger = getCallingClassLogger();
        logger.debug(message);
    }

    public static void logWarning(String message) {
        Logger logger = getCallingClassLogger();
        logger.warn(message);
    }
    public static void logError(String message) {
        Logger logger = getCallingClassLogger();
        logger.error(message);
    }
    public static void logError(String message, Exception exception) {
        Logger logger = getCallingClassLogger();
        logger.error(message, exception);
    }
    public static void logResponse(int statusCode, String responseBody) {
        Logger logger = getCallingClassLogger();
        logger.info("\n========== API RESPONSE ==========");
        logger.info("STATUS CODE : " + statusCode);
        logger.info("RESPONSE BODY : \n" + responseBody);
    }
    public static void clearContext() {
        MDC.clear();
    }
}