package com.cps3230assignment.task1.page_objects;

import com.cps3230assignment.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MaltaparkHomePage {

    WebDriver webDriver;
    private final String address = "https://www.maltapark.com/";

    public void setWebDriver(WebDriver driver){
        System.setProperty("webdriver.chrome.driver", Constants.WEBDRIVER_PATH.value());
        this.webDriver = driver;
    }

    public void skipWarning() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        //While I am aware that the way to wait is not the following
        //the design of the website made it impossible to reach with the
        //proper methods, since the click was being intercepted by some other element.
        //The code as meant to be without the thread wait is still included,
        //but will not work.
        Thread.sleep(10000);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("okbutton"))).click();
        ;
    }

    public void search(String term){
        WebElement searchBar = webDriver.findElement(By.id("search"));
        searchBar.sendKeys(term);
        webDriver.findElement(By.className("btn-search")).click();
    }

    public ClassifiedsPane getClassifiedsPane(){
        WebElement classifieds = webDriver.findElement(By.className("classifieds"));
        return new ClassifiedsPane(webDriver, classifieds);
    }
}
