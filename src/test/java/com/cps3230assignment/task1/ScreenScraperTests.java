package com.cps3230assignment.task1;

import com.cps3230assignment.task1.models.Alert;
import com.cps3230assignment.task1.models.AlertType;
import com.cps3230assignment.task1.models.PostAlertResponse;
import com.cps3230assignment.task1.utils.IAlertClient;
import kong.unirest.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class ScreenScraperTests {
    ScreenScraper ss;
    @BeforeEach
    public void setup(){
        ss = new ScreenScraper();
    }

    @Test
    public void testGetProducts(){

    }

    @Test
    public void testUploadFiveAlerts(){
        //Setup
        //Dependency mocks and behaviours
        IAlertClient maClient = Mockito.mock(IAlertClient.class);
        HttpResponse<PostAlertResponse> response = Mockito.mock(HttpResponse.class);
        Mockito.when(response.getStatus()).thenReturn(201);
        Mockito.when(maClient.postAlert(any(Alert.class))).thenReturn(response);

        ss.setMarketAlertClient(maClient);
        //Dummy Data
        Alert alert = new Alert();
        alert.setAlertType(AlertType.ELECTRONICS);
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
        HttpResponse<PostAlertResponse> successfulResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(successfulResponse.getStatus()).thenReturn(201);
        HttpResponse<PostAlertResponse> failureResponse = Mockito.mock(HttpResponse.class);
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
