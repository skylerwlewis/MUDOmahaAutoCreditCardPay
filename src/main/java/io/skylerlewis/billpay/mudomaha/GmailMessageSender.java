package io.skylerlewis.billpay.mudomaha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Properties;

public class GmailMessageSender {

    private static Logger LOGGER = LoggerFactory.getLogger(GmailMessageSender.class);

    private static Properties GMAIL_SESSION_PROPERTIES;

    static {
        GMAIL_SESSION_PROPERTIES = new Properties();
        GMAIL_SESSION_PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
        GMAIL_SESSION_PROPERTIES.put("mail.smtp.port", "587");
        GMAIL_SESSION_PROPERTIES.put("mail.smtp.auth", "true");
        GMAIL_SESSION_PROPERTIES.put("mail.smtp.starttls.enable", "true");
    }

    Session session;
    InternetAddress fromAddress;
    InternetAddress[] toAddresses;

    public GmailMessageSender(String gmailUsername, String gmailPassword, String toAddress) throws AddressException {
        this.session = Session.getInstance(GMAIL_SESSION_PROPERTIES, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(gmailUsername, gmailPassword);
            }
        });

        this.fromAddress = new InternetAddress(gmailUsername);
        this.toAddresses = new InternetAddress[]{new InternetAddress(toAddress)};
    }

    public boolean sendMessage(String emailMessage) {
        return sendMessage(emailMessage, null);
    }

    public boolean sendMessage(String emailMessage, String... imageStrings) {
        boolean successfulSend = false;
        try {
            LOGGER.info("Sending alert message");

            Message message = new MimeMessage(session);
            message.setFrom(fromAddress);
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            message.setReplyTo(new InternetAddress[]{fromAddress});

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(emailMessage);

            MimeMultipart multipart = new MimeMultipart();

            multipart.addBodyPart(textPart);

            if (imageStrings != null) {
                for (String imageString : imageStrings) {
                    if (imageString != null) {
                        MimeBodyPart filePart = new PreencodedMimeBodyPart("base64");
                        filePart.setContent(imageString, "image/*");
                        multipart.addBodyPart(filePart);
                    }
                }
            }

            message.setContent(multipart);

            Transport.send(message);

            successfulSend = true;
        } catch (Exception e) {
            LOGGER.error(String.format("There was a problem sending out an error message: \"%s\".", emailMessage), e);
        }

        return successfulSend;
    }

}
