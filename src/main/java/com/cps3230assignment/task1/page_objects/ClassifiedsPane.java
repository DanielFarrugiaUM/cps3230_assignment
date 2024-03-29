package com.cps3230assignment.task1.page_objects;

import com.cps3230assignment.task1.models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ClassifiedsPane {

    WebDriver webDriver;
    WebElement classifieds;

    List<WebElement> items;

    public ClassifiedsPane(){
        //This constructor is to be used by mockito.spy
    }
    public ClassifiedsPane(WebDriver driver, WebElement classifieds){
        this.webDriver = driver;
        this.classifieds = classifieds;
    }

    //The following setters are needed for
    // testing hen creating a mock with mockito
    //because within production flow, constructor injection
    //would take place.
    public void setWebDriver(WebDriver webDriver){
        this.webDriver = webDriver;
    }

    public void setClassifieds(WebElement classifieds){
        this.classifieds = classifieds;
    }
    public List<String> getItemsUrls(){
        //Returns empty list if it finds no items
        List<String> urls = new ArrayList<>();
        List<WebElement> items = classifieds.findElements(By.className("item"));
        if(items.size() == 0){
            return urls;
        }
        for (WebElement item:
            items) {
            List<WebElement> linkTag = item.findElements(By.className("imagelink"));
            if (linkTag.size() > 0){
                String url = linkTag.get(0).getAttribute("href");
                urls.add(url);
            }

        }
        return  urls;
    }

    public Product getProduct(String itemUrl){

        webDriver.get(itemUrl);

        String heading =
                webDriver.findElement(By.className("top-title")).findElement(By.tagName("span")).getText();
        String description =
                webDriver.findElement(By.className("readmore-wrapper")).getText();
        String url =
                webDriver.getCurrentUrl();
        String imageUrl =
                webDriver.findElement(By.className("image-wrapper"))
                        .findElement(By.tagName("img")).getAttribute("src");
        String price =
                webDriver.findElement(By.className("top-price")).getText();
        webDriver.navigate().back();
        return new Product(heading, description, url, imageUrl, price);
    }

}
