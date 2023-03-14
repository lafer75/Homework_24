package org.example;

import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {
    public static void main(String[] args) {
        start(MyTests.class);
    }

    public static void start(Class<?> clazz) {
        List<Method> beforeSuiteMethods = new ArrayList<>();
        List<Method> afterSuiteMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(MyTests.BeforeSuite.class)) {
                    beforeSuiteMethods.add(method);
                } else if (annotation.annotationType().equals(MyTests.AfterSuite.class)) {
                    afterSuiteMethods.add(method);
                } else if (annotation.annotationType().equals(MyTests.Test.class)) {
                    testMethods.add(method);
                }
            }
        }

        if (beforeSuiteMethods.size() > 1 || afterSuiteMethods.size() > 1) {
            throw new RuntimeException("Повинен бути тільки один метод, анотований з @BeforeSuite і @AfterSuite");
        }

        Collections.sort(testMethods, Comparator.comparingInt(TestRunner::getPriority));

        try {
            if (!beforeSuiteMethods.isEmpty()) {
                beforeSuiteMethods.get(0).invoke(null);
            }

            for (Method testMethod : testMethods) {
                testMethod.invoke(null);
            }

            if (!afterSuiteMethods.isEmpty()) {
                afterSuiteMethods.get(0).invoke(null);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static int getPriority(Method method) {
        if (method.isAnnotationPresent(Test.class)) {
            Test annotation = method.getAnnotation(Test.class);
            return annotation.priority();
        }
        return 0;
    }

}

