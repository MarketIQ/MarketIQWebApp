package com.app.server.services;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


public class EmailService {

    private static final String FROM = "teammarketiq@gmail.com";
    private static final String EMAIL_USERNAME = "teammarketiq@gmail.com";
    private static final String EMAIL_PASSWORD = "marketiq@123";

    public void sendEmail(String to, String emailBody) {

        // Get system properties
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");


        // Get the default Session object.
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message msg = new MimeMessage(session);

            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(FROM));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            msg.setSubject("test");
            msg.setText(emailBody);
            msg.setSentDate(new Date());
            Transport.send(msg);
            System.out.println("Sent message successfully...." + msg);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}

