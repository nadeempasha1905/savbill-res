package com.savbill.mailerservice;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailerService {
	ResourceBundle rb=ResourceBundle.getBundle("cloudbillrb");

	String emailFrom = rb.getString("emailFrom"); //client mail account
	String password = rb.getString("emailFromPasswd"); //client Account Password
	String smtpHost = rb.getString("smtpHost"); //client Account SMTP HostName
	String smtpPort= rb.getString("smtpPort");
	@SuppressWarnings("static-access")
	public String sendMail(String emailID, String subj, String messageHtmlText) {
		Properties properties = new Properties();
		
		properties.put("mail.smtp.host",smtpHost);
		properties.put("mail.smtp.socketFactory.port", smtpPort);
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", smtpPort);
		
		try {
			Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                	protected PasswordAuthentication getPasswordAuthentication() {
                    	return new PasswordAuthentication(emailFrom,password);
                	}
            	});
			session.setDebug(false);//set to true if required to trace the mail
			javax.mail.Message message = new MimeMessage(session);
			//Email Address of the Receiver
			message.addRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(emailID));
			//Email Address of the Sender
			message.addFrom(new InternetAddress[] {new InternetAddress(emailFrom)});
			message.setSubject(subj); //Setting the Email Subject
			//Create a message part for the time,location
			MimeBodyPart messagePart = new MimeBodyPart();
			messageHtmlText = javax.mail.internet.MimeUtility.encodeText(messageHtmlText);
			messagePart.setContent(messageHtmlText,"text/html");
			MimeMultipart multipart=new MimeMultipart();
			multipart.addBodyPart(messagePart);
			message.setContent(multipart);
			message.setSentDate(new Date());
			Transport transport = session.getTransport("smtp");
			transport.connect();
			System.out.println(transport.isConnected());
			transport.send(message);
			transport.close();
		}
		catch(UnsupportedEncodingException une){
			une.printStackTrace();
			return "SentMailFailure";
		}catch(MessagingException me){
			me.printStackTrace();
			return "SentMailFailure";
		}catch(Exception me){
			me.printStackTrace();
			return "SentMailFailure";
		}
		return "Mail Sent Success";
	}
}


