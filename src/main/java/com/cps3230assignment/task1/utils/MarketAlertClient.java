package com.cps3230assignment.task1.utils;

import com.cps3230assignment.Constants;
import com.cps3230assignment.task1.models.Alert;
import com.cps3230assignment.task1.models.PostAlertResponse;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class MarketAlertClient implements IAlertClient{

    private final String baseURL = Constants.MARKET_ALERT_BASE_URL.value();
    //userId should be parsed from an encrypted config file for security,
    //but that is beyond the scope of this assignment.
    private final String userId = Constants.USER_ID.value();
    @Override
    public HttpResponse<JsonNode> postAlert(Alert alert) {
        Unirest.config().defaultBaseUrl(baseURL);
        return Unirest.post("/Alert")
                .header("Content-Type", "application/json")
                .body(alert)
                .asJson();
    }

    @Override
    public HttpResponse<JsonNode> purgeAlerts() {
        Unirest.config().defaultBaseUrl(baseURL);
        return Unirest.delete("/Alert?userId={userId}")
                .routeParam("userId", userId).asJson();
    }
}
