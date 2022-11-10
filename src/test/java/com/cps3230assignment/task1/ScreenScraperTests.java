package com.cps3230assignment.task1;

import com.cps3230assignment.Constants;
import com.cps3230assignment.task1.models.Alert;
import com.cps3230assignment.task1.models.AlertType;
import com.cps3230assignment.task1.models.Product;
import com.cps3230assignment.task1.page_objects.ClassifiedsPane;
import com.cps3230assignment.task1.page_objects.MaltaparkHomePage;
import com.cps3230assignment.task1.utils.IAlertClient;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ScreenScraperTests {
    ScreenScraper ss;
    @BeforeEach
    public void setup(){
        ss = new ScreenScraper();
    }

    @Test
    public void testGetProducts() throws Exception {
        //Setup
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        //Stop web driver from communicating across network
        doNothing().when(webDriver).get(anyString());
        ss.setWebDriver(webDriver);

        //Create mock Dependent-upon components from web
        MaltaparkHomePage homePage = Mockito.mock(MaltaparkHomePage.class);
        homePage.setWebDriver(webDriver);
        ss.setHomePage(homePage);

        ClassifiedsPane classifiedsPane = Mockito.mock(ClassifiedsPane.class);
        classifiedsPane.setWebDriver(webDriver);
        WebElement dummyClassifieds = mock(WebElement.class);
        classifiedsPane.setClassifieds(dummyClassifieds);

        when(homePage.getClassifiedsPane()).thenReturn(classifiedsPane);
        //Prepare some dummy data for mocks to return
        Product dummyProduct = new Product(
                "Dummy Heading",
                "description",
                "dummy.com",
                "iamgeUrl.com",
                "400"

        );

        List<String> dummyUrls = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dummyUrls.add("dummy url");
        }
        when(classifiedsPane.getItemsUrls()).thenReturn(dummyUrls);
        when(classifiedsPane.getProduct(anyString())).thenReturn(dummyProduct);
        List<Alert> alerts = new ArrayList<>();
        //Exercise
        try {
             alerts = ss.getFiveProductsAsAlerts(AlertType.BOAT, "boat");
        }catch (Exception exception){
            throw new RuntimeException(exception);
        };

        //Verify
        Assertions.assertEquals(5, alerts.size());
        Mockito.verify(homePage).getClassifiedsPane();
        Mockito.verify(classifiedsPane, times(5)).getProduct(anyString());
    }

    @Test
    public void testGetProductsFailedToReachWebsite(){
        //Setup
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        doThrow(WebDriverException.class).when(webDriver).get(anyString());
        ss.setWebDriver(webDriver);

        //Exercise & Verify
        Assertions.assertThrowsExactly(
                Exception.class,
                () -> ss.getFiveProductsAsAlerts(AlertType.BOAT, "boat")
        );
    }

    @Test
    public void testGetProductsFailedToSkipWarning() throws InterruptedException {
        //Setup
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        ss.setWebDriver(webDriver);

        MaltaparkHomePage homePage = mock(MaltaparkHomePage.class);
        doThrow(InterruptedException.class).when(homePage).skipWarning();
        ss.setHomePage(homePage);

        //Exercise & Verify
        Assertions.assertThrowsExactly(
                Exception.class,
                () -> ss.getFiveProductsAsAlerts(AlertType.BOAT, "boat")
        );
    }

    @Test
    public void testGetProductsFailedToGetSearchResults() throws Exception {
        //Setup
        String term = "boat";

        WebDriver webDriver = Mockito.mock(WebDriver.class);
        ss.setWebDriver(webDriver);

        MaltaparkHomePage homePage = mock(MaltaparkHomePage.class);
        doThrow(Exception.class).when(homePage).search(term);
        ss.setHomePage(homePage);

        //Exercise & Verify
        Assertions.assertThrowsExactly(
                Exception.class,
                () -> ss.getFiveProductsAsAlerts(AlertType.BOAT, term)
        );
    }

    @Test
    public void testGetProductsFailedToGetClassifiedsPane() throws Exception {
        //Setup
        WebDriver webDriver = Mockito.mock(WebDriver.class);
        ss.setWebDriver(webDriver);

        MaltaparkHomePage homePage = mock(MaltaparkHomePage.class);
        doThrow(Exception.class).when(homePage).getClassifiedsPane();
        ss.setHomePage(homePage);

        //Exercise & Verify
        Assertions.assertThrowsExactly(
                Exception.class,
                () -> ss.getFiveProductsAsAlerts(AlertType.BOAT, "boat")
        );
    }

        @Test
    public void testUploadFiveAlerts(){
        //Setup
        //Dependency mocks and behaviours
        IAlertClient maClient = Mockito.mock(IAlertClient.class);
        HttpResponse<JsonNode> response = Mockito.mock(HttpResponse.class);
        Mockito.when(response.getStatus()).thenReturn(201);
        Mockito.when(maClient.postAlert(any(Alert.class))).thenReturn(response);

        ss.setMarketAlertClient(maClient);
        //Dummy Data
        Alert alert = new Alert();
        alert.setAlertType(AlertType.ELECTRONICS.value());
        alert.setHeading("Jumper Windows 11 Laptop");
        alert.setDescription("Jumper Windows 11 Laptop 1080P Display,12GB RAM 256GB SSD");
        alert.setUrl("https://www.amazon.co.uk/Windows-Display-Ultrabook-Processor-Bluetooth");
        alert.setImageUrl("https://m.media-amazon.com/images/I/712Xf2LtbJL._AC_SX679_.jpg");
        alert.setPostedBy("7ca5f131-0ff0-42cd-85e8-cae25a4ee41f");
        alert.setPriceInCents(24999);
        List<Alert> alerts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            alerts.add(alert);
        }

        //Exercise
        boolean result = ss.uploadFiveAlerts(alerts);
        //Verify
        Assertions.assertTrue(result);
        Mockito.verify(maClient, Mockito.times(5)).postAlert(any(Alert.class));
        //Teardown
    }

    @Test
    public void testUploadAlertsOneFailure(){
        //Setup
        //Dependency mocks and behaviours
        IAlertClient maClient = Mockito.mock(IAlertClient.class);
        HttpResponse<JsonNode> successfulResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(successfulResponse.getStatus()).thenReturn(201);
        HttpResponse<JsonNode> failureResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(failureResponse.getStatus()).thenReturn(400);

        ss.setMarketAlertClient(maClient);

        List<Alert> alerts = new ArrayList<>();
        Alert goodAlert = new Alert();
        Alert badAlert = new Alert(); //An example would be an alert with non-existent userId
        alerts.add(goodAlert);
        alerts.add(goodAlert);
        alerts.add(badAlert);
        alerts.add(goodAlert);
        alerts.add(goodAlert);
        Mockito.when(maClient.postAlert(goodAlert)).thenReturn(successfulResponse);
        Mockito.when(maClient.postAlert(badAlert)).thenReturn(failureResponse);

        //Exercise
        boolean result = ss.uploadFiveAlerts(alerts);

        //Verify
        Assertions.assertFalse(result);
        Mockito.verify(maClient, Mockito.times(3)).postAlert(any(Alert.class));
        Mockito.verify(maClient).purgeAlerts();
    }
}
