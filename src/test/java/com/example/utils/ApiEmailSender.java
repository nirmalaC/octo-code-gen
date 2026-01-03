package com.example.utils;

import com.example.support.ConfigReader;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;

/**
 * API-based email sender (SendGrid) - No SMTP/App Passwords needed
 * Just requires an API key
 */
public class ApiEmailSender {
    
    private static final Logger logger = LoggerFactory.getLogger(ApiEmailSender.class);
    private static final OkHttpClient client = new OkHttpClient();
    
    /**
     * Sends email with screenshot using SendGrid API
     * No SMTP or App Passwords needed - just an API key
     * 
     * @param toEmail recipient email
     * @param subject email subject
     * @param body email body
     * @param screenshotBytes screenshot as byte array
     * @param screenshotFileName filename for screenshot
     * @return true if successful
     */
    public static boolean sendEmailViaSendGrid(String toEmail, String subject, String body, 
                                               byte[] screenshotBytes, String screenshotFileName) {
        try {
            String apiKey = ConfigReader.get("email.sendgrid.api.key");
            String fromEmail = ConfigReader.get("email.from");
            
            logger.info("Attempting to send email via SendGrid");
            logger.debug("From: {}, To: {}, Subject: {}", fromEmail, toEmail, subject);

            // Convert screenshot to base64
            String screenshotBase64 = Base64.getEncoder().encodeToString(screenshotBytes);
            
            // Escape JSON special characters
            String escapedSubject = subject.replace("\\", "\\\\").replace("\"", "\\\"");
            String escapedBody = body.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
            
            // Build JSON request
            String json = String.format(
                "{\n" +
                "  \"personalizations\": [{\n" +
                "    \"to\": [{\"email\": \"%s\"}]\n" +
                "  }],\n" +
                "  \"from\": {\"email\": \"%s\"},\n" +
                "  \"subject\": \"%s\",\n" +
                "  \"content\": [{\n" +
                "    \"type\": \"text/plain\",\n" +
                "    \"value\": \"%s\"\n" +
                "  }],\n" +
                "  \"attachments\": [{\n" +
                "    \"content\": \"%s\",\n" +
                "    \"type\": \"image/png\",\n" +
                "    \"filename\": \"%s\"\n" +
                "  }]\n" +
                "}",
                toEmail, fromEmail, escapedSubject, escapedBody, screenshotBase64, screenshotFileName
            );

            RequestBody requestBody = RequestBody.create(
                json, MediaType.parse("application/json")
            );
            
            Request request = new Request.Builder()
                .url("https://api.sendgrid.com/v3/mail/send")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();
            
            try (Response response = client.newCall(request).execute()) {
                ResponseBody responseBodyObj = response.body();
                String responseBody = responseBodyObj != null ? responseBodyObj.string() : "No response body";
                int statusCode = response.code();
                
                logger.info("SendGrid API Response - Status Code: {}", statusCode);

                return true;
            }
            
        } catch (IOException e) {
            logger.error("Failed to send email via SendGrid", e);
            return false;
        }
    }
}

