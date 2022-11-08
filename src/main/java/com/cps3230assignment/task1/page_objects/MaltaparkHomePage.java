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
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        //While the following looks dodgy (as it is), I could find no
        // other way to access the close button using wait. This is
        // probably due to how JS is manipulating the element in question.
        // The button appears and is covered immediately, so once it is
        // located it cannot be clicked. Therefor, wait for the state just
        // before it becomes clickable, and then get the clickable state.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Close (1)')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Close')]"))).click();
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
