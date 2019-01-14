package com.gnanaoly.flickr.util;


import android.util.Log;



public class AppLog {


    public static final String EMPTY_MSG = "AppLogger";

    private static boolean debugMode = true;

    public static final String classLog="Class : ";




    public static void setDebugEnable(boolean isDebugMode) {

        debugMode = isDebugMode;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static void print(String className, int count) {
        if (debugMode)
            Log.i("", classLog + className + " --> " + count);
    }

    public static void print(String className, final String description) {
        if (debugMode)
            Log.i("", classLog + className + ", Desc : " + description);
    }

    public static void info(final Exception description) {
        if (debugMode)
            System.err.println(description.getMessage());
    }

    public static void sysoutPrintln(final String description) {
        if (debugMode)
            System.err.println(description);
    }


    public static void print(String className, String description, int count) {
        if (debugMode)
            Log.i("", classLog + className + " --> " + count + ", Desc : " + description);
    }

    public static void print(String className, String methodName, String description) {
        if (debugMode)
            Log.i("", classLog + className + ", Method : " + methodName + ", Desc : " + description);
    }

    public static void logError(String tag, String stackTraceString) {
        if (debugMode)
            Log.e(tag, stackTraceString);
    }



    public static void setDebugLogEnable(boolean debug) {

        debugMode = debug;
    }


    private static boolean isDebug() {

        return debugMode;
    }




    public static void logError(final String tag, final String msg, final Exception e) {
        if (debugMode) {
            Log.e(tag, msg, e);
        }
    }

    public static void logDebug(final String tag, final String msg) {
        if (debugMode) {
            Log.d(tag, msg);
        }
    }

    public static void logDebug(final String tag, final String msg, final Exception e) {
        if (debugMode) {
            Log.d(tag, msg, e);
        }
    }

    public static void logWarning(final String tag, final String msg) {
        if (debugMode) {
            Log.w(tag, msg);
        }
    }

    public static void logWarning(final String tag, final String msg, Exception e) {
        if (debugMode) {
            Log.w(tag, msg, e);
        }
    }

    public static void logVerbose(final String tag, final String msg) {
        if (debugMode) {
            Log.v(tag, msg);
        }
    }

    public static void logVerbose(final String tag, final String msg, final Exception e) {
        if (debugMode) {
            Log.v(tag, msg, e);
        }
    }

    public static void logInfo(final String tag, final String msg) {
        if (debugMode) {
            Log.i(tag, msg);
        }
    }

    public static void logInfo(final String tag, final String msg, final Exception e) {
        if (debugMode) {
            Log.i(tag, msg, e);
        }
    }


    public static void logInfo(String message) {

        if (debugMode) {
            Log.i(EMPTY_MSG, message);
        }

    }

    public static void println(String message) {
    }
}
