package com.cps3230assignment.task2.page_objects;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MarketAlertAlertsPage {

    private final WebDriver webDriver;

    private List<WebElement> alerts;

    public MarketAlertAlertsPage(WebDriver wd){
        this.webDriver = wd;
        String pageUrl = "https://www.marketalertum.com/Alerts/List";
        webDriver.get(pageUrl);
    }

    public void getAlerts(){
        //We are assuming that the alert is always a separate table
        //and that there are no other tables with other usage
        alerts = webDriver.findElements(By.tagName("table"));
    }

    public int numberOfAlerts(){
        return alerts.size();
    }

    public boolean checkIconIsPresent(){
        for (int i = 1; i <= alerts.size(); i++) {
            List<WebElement> xpathResults =
                    webDriver.findElements(
                            By.xpath("/html/body/div/main/table[" + i + "]/tbody/tr[1]/td/h4/img")
                    );
            if(!(xpathResults.size() == 1)) return false;
        }

        return true;
    }

    public boolean checkHeadingIsPresent(){
        for (int i = 1; i <= alerts.size(); i++) {
            String text =
                    webDriver.findElement(
                            By.xpath("/html/body/div/main/table[" + i + "]/tbody/tr[1]/td/h4")
                    ).getText();
            if(text.isEmpty()) return false;
        }
        return true;
    }

    public boolean checkDescriptionIsPresent(){
        for (int i = 1; i <= alerts.size(); i++) {
            String text =
                    webDriver.findElement(
                            By.xpath("/html/body/div/main/table[" + i + "]/tbody/tr[3]/td")
                    ).getText();
            if(text.isEmpty()) return false;
        }
        return true;
    }

    public boolean checkImageIsPresent(){
        for (int i = 1; i <= alerts.size(); i++) {
            List<WebElement> xpathResults =
                    webDriver.findElements(
                            By.xpath("/html/body/div/main/table[" + i + "]/tbody/tr[2]/td/img")
                    );
            if(!(xpathResults.size() == 1)) return false;
        }
        return true;
    }

    public boolean checkPriceIsPresent(){
        for (int i = 1; i <= alerts.size(); i++) {
             String text =
                    webDriver.findElement(
                            By.xpath("/html/body/div/main/table[" + i + "]/tbody/tr[4]/td")
                    ).getText();
            if(text.isEmpty()) return false;
        }
        return true;
    }

    public boolean checkLinkIsPresent(){
        for (int i = 1; i <= alerts.size(); i++) {
            List<WebElement> xpathResults =
                    webDriver.findElements(
                            By.xpath("/html/body/div/main/table[" + i + "]/tbody/tr[5]/td/a")
                    );
            if(!(xpathResults.size() == 1)) return false;
        }
        return true;
    }

    public String getIconFileName(){
        //There will be only 1 table for this test
        //therefore xpath is predefined
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        String fullPath =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("/html/body/div/main/table[1]/tbody/tr[1]/td/h4/img"))
                ).getAttribute("src")
                        .trim();
        int index = fullPath.indexOf("icon-");
        return fullPath.substring(index);
    }
}
