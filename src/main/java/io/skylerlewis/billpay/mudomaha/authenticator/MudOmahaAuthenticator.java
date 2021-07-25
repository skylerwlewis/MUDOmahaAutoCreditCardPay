package io.skylerlewis.billpay.mudomaha.authenticator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MudOmahaAuthenticator {

    public static void login(String username, String password, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 120);
        driver.get("https://myaccount.mudomaha.com/");

        wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOfElementLocated(By.id("__xmlview0--nameInput-inner")), ExpectedConditions.visibilityOfElementLocated(By.id("__xmlview0--passwordInput-inner")), ExpectedConditions.elementToBeClickable(By.id("__button0"))));
        driver.findElement(By.id("__xmlview0--nameInput-inner")).sendKeys(username);
        driver.findElement(By.id("__xmlview0--passwordInput-inner")).sendKeys(password);
        driver.findElement(By.id("__button0")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("__xmlview1--accountsSummaryTile")));
    }

    public static void logout(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 120);
        String currentUrl = driver.getCurrentUrl();
        driver.get("https://myaccount.mudomaha.com/sap/public/bc/icf/logoff");

        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));
    }
}
