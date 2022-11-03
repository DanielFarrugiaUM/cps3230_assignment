package com.cps3230assignment.task1;

import com.cps3230assignment.task1.models.Alert;
import com.cps3230assignment.task1.models.PostAlertResponse;
import com.cps3230assignment.task1.utils.IAlertClient;
import kong.unirest.HttpResponse;

import java.util.List;

public class ScreenScraper {
    private IAlertClient maClient;

    public void setMarketAlertClient(IAlertClient maClient) {
        this.maClient = maClient;
    }

    //Visit website
    //Scrape results
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


}
