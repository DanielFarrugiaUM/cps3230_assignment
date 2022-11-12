package com.cps3230assignment.task1;

import com.cps3230assignment.Constants;
import com.cps3230assignment.task1.models.Alert;
import com.cps3230assignment.task1.models.AlertType;
import com.cps3230assignment.task1.utils.MarketAlertClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", Constants.WEBDRIVER_PATH.value());
        WebDriver webDriver = new ChromeDriver();
        MarketAlertClient client = new MarketAlertClient();
        ScreenScraper screenScraper = new ScreenScraper();

        screenScraper.setWebDriver(webDriver);
        screenScraper.setMarketAlertClient(client);

        List<Alert> alerts = screenScraper.getFiveProductsAsAlerts(AlertType.BOAT, "boat");

        webDriver.quit();

        screenScraper.uploadAlerts(alerts);
    }
}
