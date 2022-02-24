package io.skylerlewis.billpay.mudomaha.authenticator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MudOmahaAuthenticator {

    private String mudOmahaUrl;
    private WebDriver driver;
    private WebDriverWait wait;

    public MudOmahaAuthenticator(String mudOmahaUrl, WebDriver driver, WebDriverWait wait) {
        this.mudOmahaUrl = mudOmahaUrl;
        this.driver = driver;
        this.wait = wait;
    }

    public void login(String username, String password) {
        driver.get(mudOmahaUrl);

        wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOfElementLocated(By.id("__xmlview0--nameInput-inner")), ExpectedConditions.visibilityOfElementLocated(By.id("__xmlview0--passwordInput-inner")), ExpectedConditions.elementToBeClickable(By.id("__button0"))));
        driver.findElement(By.id("__xmlview0--nameInput-inner")).sendKeys(username);
        driver.findElement(By.id("__xmlview0--passwordInput-inner")).sendKeys(password);
        driver.findElement(By.id("__button0")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("__xmlview1--accountsSummaryTile")));
    }

    public void logout() {
        String currentUrl = driver.getCurrentUrl();
        driver.get(mudOmahaUrl + "/sap/public/bc/icf/logoff");
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));
    }
}
