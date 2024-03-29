package com.cps3230assignment.task1.utils;

import com.cps3230assignment.task1.models.Alert;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

public interface IAlertClient {
    public HttpResponse<JsonNode> postAlert(Alert alert);
    public HttpResponse<JsonNode> purgeAlerts();
}
