package com.makersacademy.audiojakh.feature;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotificationTest {
    WebDriver driver;
    Faker faker;
    WebDriverWait wait;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        driver = new ChromeDriver();
        faker = new Faker();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:8081/");

        wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Sync in"))
        ).click();

        WebElement username = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("username"))
        );

        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("password"))
        );

        username.clear();
        password.clear();

        username.sendKeys("hira.test@test.com");
        password.sendKeys("Password123!");

        wait.until(
                ExpectedConditions.elementToBeClickable(By.name("action"))
        ).click();

        wait.until(
                ExpectedConditions.urlContains("/")
        );
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void userVisitsNotificationsButtonAndReturnsNotificationsPage() {
        assertTrue(driver.getCurrentUrl().contains("/notifications"));
    }
}
