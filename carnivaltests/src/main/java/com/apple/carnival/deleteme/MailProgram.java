package com.apple.carnival.deleteme;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailProgram {
	private static Session session = null;
	private static Store store = null;
	private static Message msg=null;


	public static void main(String[] args) throws MessagingException{

	       Properties props = new Properties();
	        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
	        props.put("mail.smtp.port", "465"); //TLS Port


	        Session session = Session.getInstance(props, null);

		try{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress("hari@bayamp.com"));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO,new InternetAddress("hari@bayamp.com"));

			// Set Subject: header field
			message.setSubject("This is the Subject Line!");

			// Now set the actual message
			message.setText("This is actual message");

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

}
