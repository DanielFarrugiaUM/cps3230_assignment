package com.cps3230assignment.task1.utils;

import com.cps3230assignment.task1.models.Alert;
import com.cps3230assignment.task1.models.PostAlertResponse;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

public interface IAlertClient {
    public HttpResponse<PostAlertResponse> postAlert(Alert alert);
    public HttpResponse<JsonNode> purgeAlerts();
}
