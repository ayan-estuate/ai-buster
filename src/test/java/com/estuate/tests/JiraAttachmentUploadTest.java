package com.estuate.tests;

import com.estuate.config.PropertyLoader;
import com.estuate.jira.JiraAttachmentUploader;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

public class JiraAttachmentUploadTest {

    @Test
    public void uploadAttachment() throws IOException {
        Properties config = PropertyLoader.load("src/main/resources/application.properties");

        String domain = config.getProperty("jira.domain");
        String email = config.getProperty("jira.email");
        String token = config.getProperty("jira.api.token");
        String issueKey = config.getProperty("jira.issue.key");
        String filePath = config.getProperty("jira.attachment.upload.path");

        String authHeader = "Basic " + Base64.getEncoder().encodeToString((email + ":" + token).getBytes());

        JiraAttachmentUploader.upload(domain, issueKey, filePath, authHeader);
    }
}
