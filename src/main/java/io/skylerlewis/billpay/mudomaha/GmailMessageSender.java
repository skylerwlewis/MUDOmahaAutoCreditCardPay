package io.skylerlewis.billpay.mudomaha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class GmailMessageSender {

    private static Logger LOGGER = LoggerFactory.getLogger(GmailMessageSender.class);

    private static Properties GMAIL_SESSION_PROPERTIES;
    static {
        String host = "imap.gmail.com";
        GMAIL_SESSION_PROPERTIES = new Properties();
        GMAIL_SESSION_PROPERTIES.put("mail.imap.host", host);
        GMAIL_SESSION_PROPERTIES.put("mail.imap.port", "993");
        GMAIL_SESSION_PROPERTIES.put("mail.imap.starttls.enable", "true");
        GMAIL_SESSION_PROPERTIES.put("mail.imap.ssl.trust", host);
        GMAIL_SESSION_PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
        GMAIL_SESSION_PROPERTIES.put("mail.smtp.port", "587");
        GMAIL_SESSION_PROPERTIES.put("mail.smtp.auth", "true");
        GMAIL_SESSION_PROPERTIES.put("mail.smtp.starttls.enable", "true");
    }

    public GmailMessageSender(String gmailUsername, String gmailPassword, String toAddress) throws AddressException {
        this.session = Session.getInstance(GMAIL_SESSION_PROPERTIES, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(gmailUsername, gmailPassword);
            }
        });

        this.fromAddress = new InternetAddress(gmailUsername);
        this.toAddresses = new HashSet<>(Arrays.asList(InternetAddress.parse(toAddress)));
    }

    Session session;
    InternetAddress fromAddress;
    Set<InternetAddress> toAddresses;

    public void sendMessage(String emailMessage) {
        try {
            LOGGER.info("Sending alert message");

            Message message = new MimeMessage(session);
            message.setFrom(fromAddress);
            message.setRecipients(
                    Message.RecipientType.TO, toAddresses.toArray(new Address[0]));
            message.setReplyTo(new InternetAddress[]{fromAddress});

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(emailMessage, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            LOGGER.error(String.format("There was a problem sending out an error message: \"%s\".", emailMessage), e);
        }

    }

}
