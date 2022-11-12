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
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class ScreenScraper {
    private IAlertClient maClient;
    private WebDriver webDriver;
    private MaltaparkHomePage homePage;

    public void setMarketAlertClient(IAlertClient maClient) { this.maClient = maClient; }

    public void setWebDriver(WebDriver webDriver){
        this.webDriver = webDriver;
    }

    public void setHomePage(MaltaparkHomePage page){ this.homePage = page; }

    //Visit website
    //Scrape results
    public List<Alert> getFiveProductsAsAlerts(AlertType alertType, String searchTerm) throws Exception {
        List<Alert> result = new ArrayList<>();

        try{
            webDriver.get(Constants.WEB_PAGE_ADDRESS.value());
        }catch (Exception ex){
            throw new Exception("Could not get webpage.");
        }


        //In unit test we want to use the home page
        // injected by setter; but in prod it would be
        // inconvenient to set a home page object and inject it
        if(homePage == null){
            homePage = new MaltaparkHomePage();
        }

        homePage.setWebDriver(webDriver);

        try {
            homePage.skipWarning();
        } catch (InterruptedException e) {
            throw new Exception(e);
        }

        ClassifiedsPane classifiedsPane;
        try{
            homePage.search(searchTerm);
            classifiedsPane = homePage.getClassifiedsPane();
        }catch (Exception ex){
            throw new Exception(ex);
        }

        List<String> itemsUrls = classifiedsPane.getItemsUrls();

        for (int i = 0; i < 5; i++) {
            Product product = classifiedsPane.getProduct(itemsUrls.get(i));
            Alert alert = convertProductToAlert(product, alertType);
            result.add(alert);
        }

        return result;
    }
    public boolean uploadAlerts(List<Alert> alerts){

        for (Alert currentAlert : alerts) {
            HttpResponse<JsonNode> response = maClient.postAlert(currentAlert);
            if (response.getStatus() != 201) {
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
