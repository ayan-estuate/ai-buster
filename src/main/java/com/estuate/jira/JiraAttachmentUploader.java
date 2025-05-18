package com.estuate.jira;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;

public class JiraAttachmentUploader {

    public static void upload(String domain, String issueKey, String filePath, String authHeader) throws IOException {
        String url = domain + "/rest/api/3/issue/" + issueKey + "/attachments";
        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("❌ File not found: " + filePath);
        }

        HttpPost post = new HttpPost(url);
        post.setHeader("Authorization", authHeader);
        post.setHeader("X-Atlassian-Token", "no-check");

        HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName())
                .build();

        post.setEntity(entity);

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post)) {

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200 || statusCode == 201) {
                System.out.println("✅ File uploaded successfully to Jira issue: " + issueKey);
            } else {
                System.out.println("❌ Upload failed. Status code: " + statusCode);
            }
        }
    }
}
