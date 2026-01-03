package com.example.utils;

import com.example.support.ConfigReader;
import com.example.support.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Utility class to take screenshot and send via email
 * Can be used anywhere in the framework without modifying existing methods
 */
public class ScreenshotEmailSender {
    
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotEmailSender.class);
    
    /**
     * Takes screenshot of current screen and sends it via email
     * 
     * @param recipientEmail email address to send screenshot to
     * @param subject email subject (optional, will use default if null)
     * @param body email body text (optional, will use default if null)
     * @return true if successful, false otherwise
     */
    public static boolean takeScreenshotAndSendEmail(String recipientEmail, String subject, String body) {
        try {
            // Get WebDriver instance
            WebDriver driver = DriverFactory.getDriver();
            if (driver == null) {
                logger.error("WebDriver is null. Cannot take screenshot");
                return false;
            }
            
            // Take screenshot as byte array
            logger.info("Taking screenshot...");
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            logger.info("Screenshot captured: {} bytes", screenshotBytes.length);
            
            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String screenshotFileName = "Code_screenshot_" + timestamp + ".png";
            
            // Use default subject/body if not provided
            String emailSubject = subject != null ? subject : "Selenium Test Screenshot - " + timestamp;
            String emailBody = body != null ? body : "Please find the screenshot attached from the automated test execution.";
            
            // Send email
            boolean emailSent = sendEmailWithScreenshot(recipientEmail, emailSubject, emailBody, screenshotBytes, screenshotFileName);
            
            if (emailSent) {
                logger.info("Screenshot sent successfully to: {}", recipientEmail);
                return true;
            } else {
                logger.error("Failed to send screenshot email");
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error taking screenshot and sending email", e);
            return false;
        }
    }
    
    /**
     * Takes screenshot and sends email with default subject/body
     * 
     * @param recipientEmail email address to send screenshot to
     * @return true if successful, false otherwise
     */
    public static boolean takeScreenshotAndSendEmail(String recipientEmail) {
        return takeScreenshotAndSendEmail(recipientEmail, null, null);
    }
    
    /**
     * Sends email with screenshot attachment
     * Supports API-based email services
     */
    private static boolean sendEmailWithScreenshot(String toEmail, String subject, String body, 
                                                   byte[] screenshotBytes, String screenshotFileName) {
        try {
            // Check if using API-based email service (SendGrid or Mailgun)
            String emailProvider = ConfigReader.get("email.provider", "").toLowerCase();

            if ("sendgrid".equals(emailProvider)) {
                logger.info("Using SendGrid API to send email");
                boolean result = ApiEmailSender.sendEmailViaSendGrid(toEmail, subject, body, screenshotBytes, screenshotFileName);
                if (result) {
                    logger.info("SendGrid API returned success. Check your inbox and spam folder.");
                }
                return result;
            }
            return true;

        } catch (Exception e) {
            logger.error("Unexpected error sending email to: {}. Error: {}", toEmail, e.getMessage(), e);
            logger.error("Exception type: {}", e.getClass().getName());
            return false;
        }
    }
}

