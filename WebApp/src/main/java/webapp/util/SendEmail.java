package webapp.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Class to send emails
 */
public class SendEmail {

    /**
     * Sends email.
     *
     * @param recipientEmail   the recipient email
     * @param body the body of email
     */
    public static void sendFromGMail(String recipientEmail, String body) {
        String sender = "testtasktest"; // GMail user name (just the part before "@gmail.com")
        String pass = "tasktesttest"; // GMail password
        String subject = "Bill from autobahn";
        sendFromGMailImp(sender, pass, recipientEmail, subject, body);
    }

    private static Properties getProperties(String sender, String pass, String host) {
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", sender);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        return props;
    }

    private static void sendFromGMailImp(String sender, String pass, String recipientEmail,
                                         String subject, String body) {
        String host = "smtp.gmail.com";

        Session session = Session.getDefaultInstance(getProperties(sender, pass, host));
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(sender));
            InternetAddress toAddress = new InternetAddress(recipientEmail);

            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, sender, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
