package com.estuate.jira;

import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import java.io.*;

public class JiraAttachmentDownloader {

    public static void download(String url, String fileName, String authHeader) throws IOException {
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", authHeader);
        request.setHeader("Accept", "*/*");

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request);
             InputStream in = response.getEntity().getContent();
             FileOutputStream out = new FileOutputStream("src/test/resources/" + fileName)) {

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Failed to download file. Status: " + response.getStatusLine().getStatusCode());
            }

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            System.out.println("✅ Downloaded to src/test/resources/" + fileName);
        }
    }
}
