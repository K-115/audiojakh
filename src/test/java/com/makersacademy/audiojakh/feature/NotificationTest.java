package com.makersacademy.audiojakh.feature;

import com.github.javafaker.Faker;
import com.makersacademy.audiojakh.model.Notification;
import com.makersacademy.audiojakh.model.NotificationType;
import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.NotificationRepository;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NotificationTest {
    WebDriver driver;
    Faker faker;
    WebDriverWait wait;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    User testUser;
    User otherUser;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        driver = new ChromeDriver();
        faker = new Faker();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        testUser = userRepository.findUserByEmailAddress("hira.test@test.com")
                .orElseThrow(() -> new IllegalStateException("Seed user hira.test@test.com not found"));

        otherUser = userRepository.findUserByEmailAddress("abi.test@test.com")
                .orElseThrow(() -> new IllegalStateException("Seed user abi.test@test.com not found. Check your database seed file for the exact email spelling!"));

        cleanDatabase();
        login();
    }

    private void cleanDatabase() {
        notificationRepository.deleteAll(
                notificationRepository.findByRecipientOrderByCreatedAtDesc(testUser)
        );
        notificationRepository.flush();
    }

    private void login() {
        driver.get("http://localhost:8080/");

        wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Sync in..."))
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
        cleanDatabase();
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void userVisitsNotificationsButtonAndReturnsNotificationsPage() {
        wait.until(
                ExpectedConditions.elementToBeClickable(By.className("notif-bell-circle"))
        ).click();

        wait.until(
                ExpectedConditions.urlContains("/notifications")
        );
        assertTrue(driver.getCurrentUrl().contains("/notifications"));
    }

    @Test
    public void userWithNoNotificationsSeesEmptyState() {
        wait.until(
                ExpectedConditions.elementToBeClickable(By.className("notif-bell-circle"))
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("body"))
        );

        assertTrue(driver.getPageSource().contains("You have no notifications yet."));
    }

    @Test
    public void userWithNotificationsSeesThemListed() {
        Notification n = new Notification();
        n.setRecipient(testUser);
        n.setSender(otherUser);
        n.setType(NotificationType.FOLLOW);

        String expectedMessage = otherUser.getUsername() + " started following you.";
        n.setMessage(expectedMessage);
        n.setCreatedAt(LocalDateTime.now());

        notificationRepository.saveAndFlush(n);

        wait.until(
                ExpectedConditions.elementToBeClickable(By.className("notif-bell-circle"))
        ).click();

        WebElement body = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("body"))
        );

        assertTrue(body.getText().contains(expectedMessage),
                "The notification list text layout did not contain: " + expectedMessage);
    }
}
