package org.example;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
public class MyTests {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Test {
        int priority() default 5;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface BeforeSuite {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface AfterSuite {
    }

    @BeforeSuite
    public static void before() {
        System.out.println("Before suite");
    }

    @Test(priority = 2)
    public static void test1() {
        System.out.println("Test 1");
    }

    @Test(priority = 3)
    public static void test2() {
        System.out.println("Test 2");
    }

    @Test(priority = 1)
    public static void test3() {
        System.out.println("Test 3");
    }

    @AfterSuite
    public static void after() {
        System.out.println("After suite");
    }

    public static void main(String[] args) {
        TestRunner.start(MyTests.class);
    }

}
