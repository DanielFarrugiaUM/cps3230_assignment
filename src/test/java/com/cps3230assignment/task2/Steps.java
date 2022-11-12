package com.cps3230assignment.task2;

import com.cps3230assignment.Constants;
import com.cps3230assignment.task1.ScreenScraper;
import com.cps3230assignment.task1.models.Alert;
import com.cps3230assignment.task1.models.AlertType;
import com.cps3230assignment.task1.utils.IAlertClient;
import com.cps3230assignment.task1.utils.MarketAlertClient;
import com.cps3230assignment.task2.page_objects.MarketAlertAlertsPage;
import com.cps3230assignment.task2.page_objects.MarketAlertLogin;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class Steps {

    private WebDriver webDriver;
    private MarketAlertLogin loginPage;

    private MarketAlertAlertsPage alertsPage;
    private String userId;

    @Before
    public void setup(){
        System.setProperty("webdriver.chrome.driver", Constants.WEBDRIVER_PATH.value());
        webDriver = new ChromeDriver();
        loginPage = new MarketAlertLogin(webDriver);
        alertsPage = new MarketAlertAlertsPage(webDriver);
        //We want to clean the previous alerts before running any scenario
        //consider those that include the number of alerts (eg > 5) as a condition
        IAlertClient alertClient = new MarketAlertClient();
        alertClient.purgeAlerts();
    }

    @After
    public void teardown(){
        webDriver.close();
    }

    @Given("I am a user of marketalertum")
    public void iAmAUserOfMarketalertum() {
        userId = Constants.USER_ID.value();
    }

    @When("I login using valid credentials")
    public void iLoginUsingValidCredentials() {
        loginPage.login(userId);
    }

    @Then("I should see my alerts")
    public void iShouldSeeMyAlerts() {
        //@TODO check if alerts are really VISIBLE
        String currentPageUrl = webDriver.getCurrentUrl();
        String alertsPageUrl = "https://www.marketalertum.com/Alerts/List";
        Assertions.assertEquals(alertsPageUrl, currentPageUrl);
    }

    @When("I login using invalid credentials")
    public void iLoginUsingInvalidCredentials() {
        String invalidID = "ThisCannotPossiblyBeAnID";
        loginPage.login(invalidID);
    }

    @Then("I should see the login screen again")
    public void iShouldSeeTheLoginScreenAgain() {
        String currentPageUrl = webDriver.getCurrentUrl();
        String alertsPageUrl = "https://www.marketalertum.com/Alerts/Login";
        Assertions.assertEquals(alertsPageUrl, currentPageUrl);
    }

    //Alerts---------------------------------------------------------------------
    //Scenario 3
    @Given("I am an administrator of the website and I upload {int} alerts")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadAlerts(int arg0) {
        userId = Constants.USER_ID.value();
        //Dummy Data
        Alert alert = new Alert();
        alert.setAlertType(AlertType.ELECTRONICS.value());
        alert.setHeading("Jumper Windows 11 Laptop");
        alert.setDescription("Jumper Windows 11 Laptop 1080P Display,12GB RAM 256GB SSD");
        alert.setUrl("https://www.amazon.co.uk/Windows-Display-Ultrabook-Processor-Bluetooth");
        alert.setImageUrl("https://m.media-amazon.com/images/I/712Xf2LtbJL._AC_SX679_.jpg");
        alert.setPostedBy(userId);
        alert.setPriceInCents(24999);

        List<Alert> alerts = new ArrayList<>();

        for (int i = 0; i < arg0; i++) {
            alerts.add(alert);
        }

        ScreenScraper uploadTool = new ScreenScraper();
        uploadTool.setMarketAlertClient(new MarketAlertClient());
        uploadTool.uploadAlerts(alerts);
    }

    @When("I view a list of alerts")
    public void iViewAListOfAlerts() {
        loginPage.login(userId);
        webDriver.get("https://www.marketalertum.com/Alerts/List");
        alertsPage.getAlerts();
    }

    @Then("each alert should contain an icon")
    public void eachAlertShouldContainAnIcon() {
        Assertions.assertTrue(alertsPage.checkIconIsPresent());
    }

    @And("each alert should contain a heading")
    public void eachAlertShouldContainAHeading() {
        Assertions.assertTrue(alertsPage.checkHeadingIsPresent());
    }

    @And("each alert should contain a description")
    public void eachAlertShouldContainADescription() {
        Assertions.assertTrue(alertsPage.checkDescriptionIsPresent());
    }

    @And("each alert should contain an image")
    public void eachAlertShouldContainAnImage() {
        Assertions.assertTrue(alertsPage.checkImageIsPresent());
    }

    @And("each alert should contain a price")
    public void eachAlertShouldContainAPrice() {
        Assertions.assertTrue(alertsPage.checkPriceIsPresent());
    }

    @And("each alert should contain a link to the original product website")
    public void eachAlertShouldContainALinkToTheOriginalProductWebsite() {
        Assertions.assertTrue(alertsPage.checkLinkIsPresent());
    }
    //Scenario 4
    @Given("I am an administrator of the website and I upload more than {int} alerts")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadMoreThanAlerts(int arg0) {
        userId = Constants.USER_ID.value();
        //Dummy Data
        Alert alert = new Alert();
        alert.setAlertType(AlertType.ELECTRONICS.value());
        alert.setHeading("Jumper Windows 11 Laptop");
        alert.setDescription("Jumper Windows 11 Laptop 1080P Display,12GB RAM 256GB SSD");
        alert.setUrl("https://www.amazon.co.uk/Windows-Display-Ultrabook-Processor-Bluetooth");
        alert.setImageUrl("https://m.media-amazon.com/images/I/712Xf2LtbJL._AC_SX679_.jpg");
        alert.setPostedBy(userId);
        alert.setPriceInCents(24999);

        List<Alert> alerts = new ArrayList<>();
        //Upload one extra alert
        for (int i = 0; i < arg0 + 1; i++) {
            alerts.add(alert);
        }

        ScreenScraper uploadTool = new ScreenScraper();
        uploadTool.setMarketAlertClient(new MarketAlertClient());
        uploadTool.uploadAlerts(alerts);
    }

    @Then("I should see {int} alerts")
    public void iShouldSeeAlerts(int arg0) {
        Assertions.assertEquals(arg0, alertsPage.numberOfAlerts());
    }

    @Given("I am an administrator of the website and I upload an alert of type {int}")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadAnAlertOfTypeAlertType(int arg0) {
        userId = Constants.USER_ID.value();
        //Dummy Data
        Alert alert = new Alert();
        alert.setAlertType(arg0);
        alert.setHeading("Jumper Windows 11 Laptop");
        alert.setDescription("Jumper Windows 11 Laptop 1080P Display,12GB RAM 256GB SSD");
        alert.setUrl("https://www.amazon.co.uk/Windows-Display-Ultrabook-Processor-Bluetooth");
        alert.setImageUrl("https://m.media-amazon.com/images/I/712Xf2LtbJL._AC_SX679_.jpg");
        alert.setPostedBy(userId);
        alert.setPriceInCents(24999);

        List<Alert> alerts = new ArrayList<>();
        alerts.add(alert);

        ScreenScraper uploadTool = new ScreenScraper();
        uploadTool.setMarketAlertClient(new MarketAlertClient());
        uploadTool.uploadAlerts(alerts);
    }

    @And("the icon displayed should be {string}")
    public void theIconDisplayedShouldBe(String arg0) {
        Assertions.assertEquals(arg0, alertsPage.getIconFileName());
    }
}
