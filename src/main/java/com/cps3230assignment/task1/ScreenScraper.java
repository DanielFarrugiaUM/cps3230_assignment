package com.cps3230assignment.task1;

import com.beust.ah.A;
import com.cps3230assignment.Constants;
import com.cps3230assignment.task1.models.Alert;
import com.cps3230assignment.task1.models.AlertType;
import com.cps3230assignment.task1.models.PostAlertResponse;
import com.cps3230assignment.task1.models.Product;
import com.cps3230assignment.task1.page_objects.ClassifiedsPane;
import com.cps3230assignment.task1.page_objects.MaltaparkHomePage;
import com.cps3230assignment.task1.utils.IAlertClient;
import com.cps3230assignment.task1.utils.MarketAlertClient;
import kong.unirest.HttpResponse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ScreenScraper {
    private IAlertClient maClient;
    private WebDriver webDriver;
    private MaltaparkHomePage homePage;

    public void setMarketAlertClient(IAlertClient maClient) {
        this.maClient = maClient;
    }

    public void setWebDriver(WebDriver webDriver){
        this.webDriver = webDriver;
    }

    public void setHomePage(MaltaparkHomePage page){ this.homePage = page; }

    //Visit website
    //Scrape results
    public List<Alert> getFiveProductsAsAlerts(AlertType alertType, String searchTerm){
        List<Alert> result = new ArrayList<>();

        webDriver.get(Constants.WEB_PAGE_ADDRESS.value());

        //In unit test we want to use the home page
        // injected by setter; but in prod it would be
        // inconvenient to set a home page object and inject it
        if(homePage == null){
            homePage = new MaltaparkHomePage();
        }

        homePage.setWebDriver(webDriver);

        //@TODO can do a test case if the site changes wait time
        try {
            homePage.skipWarning();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        homePage.search(searchTerm);
        ClassifiedsPane classifiedsPane = homePage.getClassifiedsPane();
        List<WebElement> items = classifiedsPane.getItems();

        for (int i = 0; i < 5; i++) {
            Product product = classifiedsPane.getProduct(items.get(i));
            Alert alert = convertProductToAlert(product, alertType);
            result.add(alert);
        }

        return result;
    }
    public boolean uploadFiveAlerts(List<Alert> alerts){

        for (int i = 0; i < 5; i++) {
            Alert currentAlert = alerts.get(i);
            HttpResponse<PostAlertResponse> response = maClient.postAlert(currentAlert);
            if(response.getStatus() != 201){
                maClient.purgeAlerts();
                return false;
            }
        }
        return true;
    }

    private Alert convertProductToAlert(Product product, AlertType type){
        return new Alert(
                type.value(),
                product.getHeading(),
                product.getDescription(),
                product.getUrl(),
                product.getImageUrl(),
                Constants.USER_ID.value(),
                product.getPriceAsCents()
        );
    }
}
