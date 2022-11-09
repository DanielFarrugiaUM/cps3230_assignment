package com.cps3230assignment.task1.page_objects;

import com.cps3230assignment.task1.models.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClassifiedsPaneTests {

    ClassifiedsPane cPane;

    @BeforeEach
    public void setup(){
        cPane = new ClassifiedsPane();
    }

    @Test
    public void testSetWebDriver(){
        //Setup
        WebDriver webDriver = mock(WebDriver.class);

        //Exercise
        cPane.setWebDriver(webDriver);

        //Verify
        Assertions.assertEquals(cPane.webDriver, webDriver);
    }

    @Test
    public void testSetClassifieds(){
        //Setup
        WebElement classifieds = mock(WebElement.class);

        //Exercise
        cPane.setClassifieds(classifieds);

        //Verify
        Assertions.assertEquals(cPane.classifieds, classifieds);
    }

    @Test
    public void testGetItemUrlsNoItems(){
        //Setup
        WebElement classifieds = mock(WebElement.class);
        cPane.setClassifieds(classifieds);
        when(classifieds.findElements(By.className("item"))).thenReturn(new ArrayList<>());

        //Exercise
        List<String> result = cPane.getItemsUrls();

        //Verify
        Assertions.assertEquals(result.size(), 0);
    }

    @Test
    public void testGetItemUrls5Urls(){
        //Setup
        String expectedUrl = "goodurl.com";
        List<WebElement> items = new ArrayList<>();
        WebElement mockItem = mock(WebElement.class);
        for (int i = 0; i < 5; i++) {
            items.add(mockItem);
        }
        List<WebElement> linkTags = new ArrayList<>();
        WebElement linkTag = mock(WebElement.class);
        linkTags.add(linkTag);

        WebElement classifieds = mock(WebElement.class);
        cPane.setClassifieds(classifieds);
        when(classifieds.findElements(By.className("item"))).thenReturn(items);
        when(mockItem.findElements(By.className("imagelink"))).thenReturn(linkTags);
        when(linkTag.getAttribute("href")).thenReturn(expectedUrl);
        //Exercise
        List<String> result = cPane.getItemsUrls();

        //Verify
        Assertions.assertEquals(result.size(), 5);
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(result.get(i), expectedUrl);
        }
    }

    @Test
    public void testGetProduct(){
        //Setup
        String url = "website.com/item";
        String heading = "heading";
        String description = "bla bla";
        String imageUrl = "image/93210";
        String priceIn = "4.99";
        double expectedPrice = 499;

        WebElement topTitle = mock(WebElement.class);
        WebElement span = mock(WebElement.class);
        WebElement readMore = mock(WebElement.class);
        WebElement imageWrapper = mock(WebElement.class);
        WebElement img = mock(WebElement.class);
        WebElement topPrice = mock(WebElement.class);

        WebDriver webDriver = mock(WebDriver.class);
        when(webDriver.findElement(By.className("top-title")))
                .thenReturn(topTitle);
        when(topTitle.findElement(By.tagName("span"))).thenReturn(span);
        when(span.getText()).thenReturn(heading);
        when(webDriver.findElement(By.className("readmore-wrapper")))
                .thenReturn(readMore);
        when(readMore.getText()).thenReturn(description);
        when(webDriver.getCurrentUrl()).thenReturn(url);
        when(webDriver.findElement(By.className("image-wrapper")))
                .thenReturn(imageWrapper);
        when(imageWrapper.findElement(By.tagName("img"))).thenReturn(img);
        when(img.getAttribute("src")).thenReturn(imageUrl);
        when(webDriver.findElement(By.className("top-price"))).thenReturn(topPrice);
        when(topPrice.getText()).thenReturn(priceIn);

        WebDriver.Navigation nav = mock(WebDriver.Navigation.class);
        when(webDriver.navigate()).thenReturn(nav);
        cPane.setWebDriver(webDriver);

        //Exercise
        Product product = cPane.getProduct(url);

        //Verify
        Assertions.assertEquals(heading, product.getHeading());
        Assertions.assertEquals(description, product.getDescription());
        Assertions.assertEquals(url, product.getUrl());
        Assertions.assertEquals(imageUrl, product.getImageUrl());
        Assertions.assertEquals(expectedPrice, product.getPriceAsCents());
        verify(webDriver, times(1)).get(url);
        verify(nav, times(1)).back();

    }

}
