package com.activequant.utils.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

    private String host, user, password, from;
    private int port;

    public SendMail(String host, int port, String username, String password, String from) {
        this.host = host;
        this.port = port;
        this.user = username;
        this.password = password;
        this.from = from;
    }

    public String generateHtmlTable(String[] header, Object[][] cells) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'>");
        sb.append("<tr>");
        for (String s : header)
            sb.append("<td><b>").append(s).append("</b></td>");
        sb.append("</tr>");
        for (int i = 0; i < cells.length; i++) {
            boolean append = false; 
            String line = ("<tr>");
            for (int j = 0; j < cells[i].length; j++) {
                if(cells[i][j]!=null)append = true; 
                line+=("<td>")+(cells[i][j])+("</td>");
            }
            
            line +=("</tr>");
            if(append)sb.append(line);
        }
        sb.append("</table>");
        return sb.toString();
    }

    public void sendMail(String[] recipients, String subject, String htmlBody) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", host);
        props.setProperty("mail.port", "" + port);
        props.setProperty("mail.user", user);
        props.setProperty("mail.password", password);

        Session mailSession = Session.getDefaultInstance(props, null);
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject(subject);
        message.setFrom(new InternetAddress(from));
        message.setSentDate(new Date());
        for (String recipient : recipients) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }

        //
        // This HTML mail have to 2 part, the BODY and the embedded image
        //
        MimeMultipart multipart = new MimeMultipart("related");

        // first part (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
        // String htmlText = "<H1>Hello</H1><img src=\"cid:image\">";
        messageBodyPart.setContent(htmlBody, "text/html");

        // add it
        multipart.addBodyPart(messageBodyPart);

        // second part (the image)
        /*
         * messageBodyPart = new MimeBodyPart(); DataSource fds = new
         * FileDataSource("C:\\images\\jht.gif");
         * messageBodyPart.setDataHandler(new DataHandler(fds));
         * messageBodyPart.setHeader("Content-ID", "<image>");
         * 
         * // add it multipart.addBodyPart(messageBodyPart);
         */
        // put everything together
        message.setContent(multipart);

        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}
