package com.cps3230assignment.task1.page_objects;

import com.cps3230assignment.task1.models.AlertType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MaltaparkHomePageTests {
    MaltaparkHomePage homePage;

    @BeforeEach
    public void setup(){
        homePage = new MaltaparkHomePage();
    }

    @Test
    public void testSetWebDriver(){
        //Setup
        WebDriver webDriver = mock(WebDriver.class);

        //Exercise
        homePage.setWebDriver(webDriver);

        //Verify
        Assertions.assertEquals(webDriver, homePage.webDriver);
    }

    @Test
    public void testSearch() throws Exception {
        //Setup
        String term = "test";
        WebDriver webDriver = mock(WebDriver.class);
        homePage.setWebDriver(webDriver);

        WebElement searchBar = mock(WebElement.class);
        WebElement dummyButton = mock(WebElement.class);
        when(webDriver.findElement(By.id("search"))).thenReturn(searchBar);
        when(webDriver.findElement(By.className("btn-search")))
                .thenReturn(dummyButton);
        doNothing().when(dummyButton).click();

        //Exercise
        homePage.search(term);

        //Verify
        verify(searchBar, times(1)).sendKeys(term);
        verify(dummyButton, times(1)).click();
    }

    @Test
    public void testSearchNoSearchBar(){
        //Setup
        String term = "test";
        WebDriver webDriver = mock(WebDriver.class);
        homePage.setWebDriver(webDriver);

        doThrow(WebDriverException.class).when(webDriver).findElement(By.id("search"));

        //Exercise & Verify
        Assertions.assertThrowsExactly(
                Exception.class,
                () -> homePage.search(term),
                "Searching for '" + term + "' failed"
        );
    }

    @Test
    public void testGetClassifiedsPane() throws Exception {
        //Setup
        WebDriver webDriver = mock(WebDriver.class);
        homePage.setWebDriver(webDriver);
        WebElement classifieds = mock(WebElement.class);

        when(webDriver.findElement(By.className("classifieds"))).thenReturn(classifieds);
        //Exercise
        ClassifiedsPane cp = homePage.getClassifiedsPane();

        // Verify
        Assertions.assertEquals(cp.classifieds, classifieds);
        Assertions.assertEquals(cp.webDriver, webDriver);
    }

    @Test
    public void testGetClassifiedsPaneFailure() throws Exception {
        //Setup
        WebDriver webDriver = mock(WebDriver.class);
        homePage.setWebDriver(webDriver);
        WebElement classifieds = mock(WebElement.class);

        doThrow(WebDriverException.class).when(webDriver).findElement(By.className("classifieds"));
        //Exercise & Verify
        Assertions.assertThrowsExactly(
                Exception.class,
                () -> homePage.getClassifiedsPane(),
                "Unable to get classifieds pane"
        );
    }
}
