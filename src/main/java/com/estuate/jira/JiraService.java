package com.estuate.jira;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Base64;

public class JiraService {

    private final String domain, email, token, authHeader;

    public JiraService(String domain, String email, String token) {
        this.domain = domain;
        this.email = email;
        this.token = token;
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString((email + ":" + token).getBytes());
    }

    public String getAttachmentUrl(String issueKey, String fileName) throws IOException {
        String url = domain + "/rest/api/3/issue/" + issueKey + "?fields=attachment";
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", authHeader);
        request.setHeader("Accept", "application/json");

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request)) {

            if (response.getStatusLine().getStatusCode() != 200) return null;

            String json = EntityUtils.toString(response.getEntity());
            JsonNode attachments = new ObjectMapper().readTree(json).path("fields").path("attachment");

            for (JsonNode attachment : attachments) {
                if (fileName.equals(attachment.get("filename").asText())) {
                    return attachment.get("content").asText();
                }
            }
        }
        return null;
    }

    public String getAuthHeader() {
        return authHeader;
    }
}
