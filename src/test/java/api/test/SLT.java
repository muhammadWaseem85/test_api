package api.test;



import org.testng.annotations.Test;



import javax.mail.Session;
import java.util.Properties;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SLT
{

    public  void sendEmailUsingTSL() {
        // Gmail username
        final String username = "t309066292@gmail.com";

        // Gmail password
        final String password = "wasim123";

        // Receiver's email ID
        String receiver = "t109066292@gmail.com";

        // Sender's email ID
        String sender = "t509066292@gmail.com";

        // Sending email from gmail
        String host = "smtp.gmail.com";

        // Port of SMTP
        String port = "587";

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        // Create session object passing properties and authenticator instance
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try
        {
            // Create MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set the Senders mail to From
            message.setFrom(new InternetAddress(sender));

            // Set the recipients email address
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));

            // Subject of the email
            message.setSubject("Java Send Email Gmail SMTP with TLS Authentication");

            // Body of the email
            message.setText("Welcome to Java Interviewpoint");

            // Send email.
            Transport.send(message);
            System.out.println("Mail sent successfully >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Mail sent successfully >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Mail sent successfully >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        } catch (MessagingException me)
        {
            me.printStackTrace();
        }
    }

    @Test
    public  void  run () {
        sendEmailUsingTSL();
    }
}
