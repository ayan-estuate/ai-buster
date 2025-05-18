package com.estuate.tests;

import com.estuate.config.PropertyLoader;
import com.estuate.jira.JiraService;
import com.estuate.jira.JiraAttachmentDownloader;
import org.testng.annotations.Test;

import java.util.Properties;

public class JiraAttachmentTest {

    @Test
    public void downloadAttachment() throws Exception {
        Properties props = PropertyLoader.load("src/main/resources/application.properties");

        String domain = props.getProperty("jira.domain");
        String email = props.getProperty("jira.email");
        String token = props.getProperty("jira.api.token");
        String issueKey = props.getProperty("jira.issue.key");
        String fileName = props.getProperty("jira.attachment.filename");

        JiraService service = new JiraService(domain, email, token);
        String url = service.getAttachmentUrl(issueKey, fileName);

        if (url != null) {
            JiraAttachmentDownloader.download(url, fileName, service.getAuthHeader());
        } else {
            System.out.println("No attachment found for: " + fileName);
        }
    }
}
