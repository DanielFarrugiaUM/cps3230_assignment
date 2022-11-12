package com.cps3230assignment.task2.page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MarketAlertLogin {
    private final WebDriver webDriver;

    public MarketAlertLogin(WebDriver wd){
        this.webDriver = wd;
        String pageUrl = "https://www.marketalertum.com/Alerts/Login";
        webDriver.get(pageUrl);
    }

    public void login(String userId){
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("UserId"))).sendKeys(userId);
        WebElement submitBtn = webDriver.findElement(By.xpath("//input[@type='submit']"));
        submitBtn.click();
    }
}
