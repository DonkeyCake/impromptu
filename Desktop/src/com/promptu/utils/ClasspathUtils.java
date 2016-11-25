package com.promptu.utils;

/**
 * Created by Guy on 24/11/2016.
 */
public class ClasspathUtils {

    public static String classPathToFilePath(Class<?> cls) {
        return classPathToFilePath(cls, true);
    }

    public static String classPathToFilePath(Class<?> cls, boolean useParentsPath) {
        String classPath;
        if(useParentsPath) classPath = cls.getPackage().getName();
        else classPath = cls.getCanonicalName();
        return classPath.replace(".", "/");
    }

}
