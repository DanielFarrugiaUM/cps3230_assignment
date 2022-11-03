package com.cps3230assignment.task1.utils;

import com.cps3230assignment.task1.models.Alert;
import com.cps3230assignment.task1.models.PostAlertResponse;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class MarketAlertClient implements IAlertClient{

    private String baseURL = "https://api.marketalertum.com";
    //This should be parsed from an encrypted config file for security,
    //but that is beyond the scope of this assignment.
    private String userId = "d9bad528-b70f-4321-a1c5-e4977a2e2bed";
    @Override
    public HttpResponse<PostAlertResponse> postAlert(Alert alert) {
        Unirest.config().defaultBaseUrl(baseURL);
        HttpResponse<PostAlertResponse> response = Unirest.post("/Alert")
                .header("Content-Type", "application/json")
                .body(alert)
                .asObject(PostAlertResponse.class);
        return response;
    }

    @Override
    public HttpResponse<JsonNode> purgeAlerts() {
        Unirest.config().defaultBaseUrl(baseURL);
        HttpResponse<JsonNode> response = Unirest.delete("/Alert?={userId}")
                .routeParam("userId", userId).asJson();
        return response;
    }
}
