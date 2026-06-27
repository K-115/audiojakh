package com.makersacademy.audiojakh.feature;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignUpTest {

    WebDriver driver;
    Faker faker;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        driver = new ChromeDriver();
        faker = new Faker();
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void successfulSignUpAlsoLogsInUser() {
        String newEmail = faker.internet().emailAddress();

        driver.get("http://localhost:8081/");
        driver.findElement(By.linkText("Sign up")).click();
        driver.findElement(By.name("email")).sendKeys(newEmail);
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> driver.findElement(By.id("username")));
        driver.findElement(By.id("username")).sendKeys("testuser" + System.currentTimeMillis());
        driver.findElement(By.id("firstName")).sendKeys("Test");
        driver.findElement(By.id("surname")).sendKeys("User");
        driver.findElement(By.id("dob")).sendKeys("01021995");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("submit"))).click();

        wait.until(driver -> driver.findElement(By.id("greeting")));
        String greetingText = driver.findElement(By.id("greeting")).getText();
        assertTrue(greetingText.contains("Welcome back"));
    }

    @Test
    public void underageSignUpValidationError() {
        String newEmail = faker.internet().emailAddress();

        driver.get("http://localhost:8081/");
        driver.findElement(By.linkText("Sign up")).click();
        driver.findElement(By.name("email")).sendKeys(newEmail);
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> driver.findElement(By.id("username")));
        driver.findElement(By.id("username")).sendKeys("younguser" + System.currentTimeMillis());
        driver.findElement(By.id("firstName")).sendKeys("Young");
        driver.findElement(By.id("surname")).sendKeys("User");

        driver.findElement(By.id("dob")).sendKeys("01012016");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("submit"))).click();

        wait.until(driver -> driver.findElement(By.className("error")));
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("You must be at least 16 years old"));
    }
}
