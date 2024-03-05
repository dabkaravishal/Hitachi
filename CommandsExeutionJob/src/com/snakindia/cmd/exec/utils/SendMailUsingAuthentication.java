package com.snakindia.cmd.exec.utils;

/*
Some SMTP servers require a username and password authentication before you
can use their Server for Sending mail. This is most common with couple
of ISP's who provide SMTP Address to Send Mail.

This Program gives any example on how to do SMTP Authentication
(User and Password verification)
*/

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

import com.sun.mail.smtp.SMTPAddressFailedException;

import java.util.*;

/**
  To use this program, change values for the following three constants,

    SMTP_HOST_NAME -- Has your SMTP Host Name
    SMTP_AUTH_USER -- Has your SMTP Authentication UserName
    SMTP_AUTH_PWD  -- Has your SMTP Authentication Password

  Next change values for fields

  emailMsgTxt  -- Message Text for the Email
  emailSubjectTxt  -- Subject for email
  emailFromAddress -- Email Address whose name will appears as "from" address

  Next change value for "emailList".
  This String array has List of all Email Addresses to Email Email needs to be sent to.


  Next to run the program, execute it as follows,

  SendMailUsingAuthentication authProg = new SendMailUsingAuthentication();

*/

public class SendMailUsingAuthentication
{
	private static Logger logger = Logger.getLogger(SendMailUsingAuthentication.class);

	private static String smtpHost=ApplicationProperties.getProperty("SMTP_HOST_NAME");
	
	private static String smtpUser=ApplicationProperties.getProperty("SMTP_USER_ID");
	
	private static String smtpPassword = ApplicationProperties.getProperty("SMTP_USER_PASSWORD");
	
	private static String smtpPort = ApplicationProperties.getProperty("SMTP_PORT");
	
	private static String fromEmailId = ApplicationProperties.getProperty("NOTIF_EMAIL_FROM_ID");
	
	private static String smtpAuthRequired = ApplicationProperties.getProperty("SMTP_AUTH_REQ");
	
	/**
	 * The Function is used to send Text Mail to End User
	 * @param toList
	 * @param subject
	 * @param message
	 * @throws MessagingException
	 */
	public static boolean newPostTextMail(ArrayList<String> toList, String subject,String message) 
	{
		boolean debug = false;
		try
		{
			/**
			 * Set the host smtp address
			 */
			Properties props = new Properties();
		    props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
		    props.setProperty("mail.smtp.from", fromEmailId);
			props.setProperty("mail.smtp.host", smtpHost);
		    props.setProperty("mail.smtp.port", smtpPort);
		    props.setProperty("mail.smtp.starttls.enable", "true");
		    props.put("mail.smtp.socketFactory.port", smtpPort);
		    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			
		    
		    Session session = null;
		    if(null!=smtpAuthRequired && smtpAuthRequired.equals("Y"))
		    {
		    	logger.info("newPostHTMLMailWithAttachment() :: Authentication is required.");
		    	/*
		    	 * AUTH REQUIRED IS TRUE
		    	 */
		    	props.setProperty("mail.smtp.auth", "true");
		    	session = Session.getDefaultInstance(props, auth);
		    }
		    else
		    {
		    	logger.info("newPostHTMLMailWithAttachment() :: Authentication is not required.");
		    	/*
		    	 * AUTH REQUIRED IS FALSE
		    	 */
		    	props.setProperty("mail.smtp.auth", "false");
		    	session = Session.getDefaultInstance(props);
		    }
			
		    if(null!=session)
		    {
		    	session.setDebug(debug);
				/**
				 * create a message
				 */
				MimeMessage msg = new MimeMessage(session);
				
				/**
				 * set the from and to address
				 */
				InternetAddress addressFrom = new InternetAddress(fromEmailId);
				msg.setFrom(addressFrom);

				InternetAddress[] addressTo = new InternetAddress[toList.size()];
				for (int i = 0; i < toList.size(); i++)
				{
					addressTo[i] = new InternetAddress(toList.get(i).toString());
				}

				msg.addRecipients(Message.RecipientType.TO, addressTo);

				/**
				 * Setting the Subject and Content Type
				 */
				msg.setSubject(subject);
				msg.setContent(message, "text/plain");
				/**
				 * Another Way Could be
				 * 	SMTPMessage smtp  = new SMTPMessage(session);
					smtp.setFrom(addressFrom);
					smtp.setSubject(subject);
					smtp.setContent(message, "text/plain");
					smtp.addRecipients(Message.RecipientType.TO,  addressTo);
					smtp.setReturnOption(SMTPMessage.RETURN_HDRS);
					smtp.setNotifyOptions(SMTPMessage.NOTIFY_DELAY|SMTPMessage.NOTIFY_FAILURE|SMTPMessage.NOTIFY_SUCCESS);
					Transport.send(smtp);
				 * 
				 */			  
				Transport.send(msg);
		    }
		}
		catch(SMTPAddressFailedException e)
		{
			Utilities.printStackTraceToLogs(SendMailUsingAuthentication.class.getName(), "newPostTextMail()", e);
			return false;
		}
		catch(SendFailedException e)
		{
			Utilities.printStackTraceToLogs(SendMailUsingAuthentication.class.getName(), "newPostTextMail()", e);
			return false;
		}
		catch (MessagingException mex) 
		{
			Utilities.printStackTraceToLogs(SendMailUsingAuthentication.class.getName(), "newPostTextMail()", mex);
			return false;
		}
		catch(Exception e)
		{
			Utilities.printStackTraceToLogs(SendMailUsingAuthentication.class.getName(), "newPostTextMail()", e);
			return false;
		}
		return true;
	}
	
	/**
	 * The function is used to send html mails to end users
	 * @param toList
	 * @param subject
	 * @param message
	 * @throws MessagingException
	 */
	public static boolean newPostHTMLMail(List<String> toList, String subject,String message) 
	{
		logger.info("newPostHTMLMail() :: Method Starts.");
		boolean debug = false;
		try
		{
			/**
			 * Get SMTP Server Details
			 */

			logger.info("newPostHTMLMail() :: Email Gateway Details are Not null, proceed for Sending Email.");
			/**
			 * Set the host smtp address
			 */
			Properties props = new Properties();
		    props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
			props.setProperty("mail.smtp.from", fromEmailId);
			props.setProperty("mail.smtp.host", smtpHost);
		    props.setProperty("mail.smtp.port", smtpPort);
		    props.setProperty("mail.smtp.starttls.enable", "true");
//		    props.put("mail.smtp.socketFactory.port", smtpPort);
//		    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			
		    
		    Session session = null;
		    if(null!=smtpAuthRequired && smtpAuthRequired.equals("Y"))
		    {
		    	logger.info("newPostHTMLMail() :: Authentication is required.");
		    	/*
		    	 * AUTH REQUIRED IS TRUE
		    	 */
		    	props.setProperty("mail.smtp.auth", "true");
		    	session = Session.getDefaultInstance(props, auth);
		    }
		    else
		    {
		    	logger.info("newPostHTMLMail() :: Authentication is not required.");
		    	/*
		    	 * AUTH REQUIRED IS FALSE
		    	 */
		    	props.setProperty("mail.smtp.auth", "false");
		    	session = Session.getDefaultInstance(props);
		    }
			/*
			 * check here if session is not null, then only proceed 
			 * else return false
			 */
			if(null!=session)
			{
				session.setDebug(debug);
				/**
				 * create a message
				 */
				MimeMessage msg = new MimeMessage(session);
				// set Internet Address
				InternetAddress addressFrom = new InternetAddress(fromEmailId);
				msg.setFrom(addressFrom);

				/**
				 * set the to address
				 */

				InternetAddress[] addressTo = new InternetAddress[toList.size()];
				for (int i = 0; i < toList.size(); i++)
				{
					addressTo[i] = new InternetAddress(toList.get(i).toString());
				}

				msg.addRecipients(Message.RecipientType.TO, addressTo);

				/**
				 * Setting the Subject and Content Type
				 */
				msg.setSubject(subject);
				msg.setContent(message, "text/html");
				/**
				 * Another Way Could be
				 * 	SMTPMessage smtp  = new SMTPMessage(session);
					smtp.setFrom(addressFrom);
					smtp.setSubject(subject);
					smtp.setContent(message, "text/plain");
					smtp.addRecipients(Message.RecipientType.TO,  addressTo);
					smtp.setReturnOption(SMTPMessage.RETURN_HDRS);
					smtp.setNotifyOptions(SMTPMessage.NOTIFY_DELAY|SMTPMessage.NOTIFY_FAILURE|SMTPMessage.NOTIFY_SUCCESS);
					Transport.send(smtp);
				 * 
				 */
				Transport.send(msg);
				logger.info("newPostHTMLMail() :: Mail Sent Successfully.");
			}
			else
			{
				return false;
			}
		}
		catch(SMTPAddressFailedException e)
		{
			logger.info("newPostHTMLMail() :: SMTPAddressFailed Exception  :: >" + e.getMessage());
			Utilities.printStackTraceToLogs(SendMailUsingAuthentication.class.getName(), "newPostHTMLMail()", e);
			return false;
		}
		catch(SendFailedException e)
		{
			logger.info("newPostHTMLMail() :: SendFailed Exception  :: >" + e.getMessage());
			Utilities.printStackTraceToLogs(SendMailUsingAuthentication.class.getName(), "newPostHTMLMail()", e);
			return false;
		}
		catch (MessagingException mex) 
		{
			logger.info("newPostHTMLMail() :: Messaging Exception  :: >" + mex.getMessage());
			Utilities.printStackTraceToLogs(SendMailUsingAuthentication.class.getName(), "newPostHTMLMail()", mex);
			return false;
		}
		catch(Exception e)
		{
			logger.info("newPostHTMLMail() :: Exception  :: >" + e.getMessage());
			Utilities.printStackTraceToLogs(SendMailUsingAuthentication.class.getName(), "newPostHTMLMail()", e);
			return false;
		}
		logger.info("newPostHTMLMail() :: Method Ends.");
		return true;
	}

	/**
	 * 
	 * @param toList
	 * @param subject
	 * @param message
	 * @param fromEmailId
	 * @param attachment
	 * @return
	 */
	public static boolean newPostHTMLMailWithAttachment(List<String> toList, String subject, String message,String attachment) {
		logger.info("newPostHTMLMailWithAttachment() :: Method Starts.");
		boolean debug = false;
		try {
			/**
			 * Get SMTP Server Details
			 */

			logger.info("newPostHTMLMailWithAttachment() :: Email Gateway Details are Not null, proceed for Sending Email.");
			/**
			 * Set the host smtp address
			 */
			Properties props = new Properties();
		    props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
			props.setProperty("mail.smtp.from", fromEmailId);
			props.setProperty("mail.smtp.host", smtpHost);
		    props.setProperty("mail.smtp.port", smtpPort);
		    props.setProperty("mail.smtp.starttls.enable", "true");
//		    props.put("mail.smtp.socketFactory.port", smtpPort);
//		    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			
		    
		    Session session = null;
		    if(null!=smtpAuthRequired && smtpAuthRequired.equals("Y"))
		    {
		    	logger.info("newPostHTMLMailWithAttachment() :: Authentication is required.");
		    	/*
		    	 * AUTH REQUIRED IS TRUE
		    	 */
		    	props.setProperty("mail.smtp.auth", "true");
		    	session = Session.getDefaultInstance(props, auth);
		    }
		    else
		    {
		    	logger.info("newPostHTMLMailWithAttachment() :: Authentication is not required.");
		    	/*
		    	 * AUTH REQUIRED IS FALSE
		    	 */
		    	props.setProperty("mail.smtp.auth", "false");
		    	session = Session.getDefaultInstance(props);
		    }
			/*
			 * check here if session is not null, then only proceed else return
			 * false
			 */
			if (null != session) {
				session.setDebug(debug);
				/**
				 * create a message
				 */
				MimeMessage msg = new MimeMessage(session);
				// set Internet Address
				InternetAddress addressFrom = new InternetAddress(fromEmailId);
				msg.setFrom(addressFrom);

				/**
				 * set the to address
				 */

				InternetAddress[] addressTo = new InternetAddress[toList.size()];
				for (int i = 0; i < toList.size(); i++) {
					addressTo[i] = new InternetAddress(toList.get(i).toString());
				}

				msg.addRecipients(Message.RecipientType.TO, addressTo);

				/**
				 * Setting the Subject and Content Type
				 */
				msg.setSubject(subject);
				// Create the text part
				MimeBodyPart mpart = new MimeBodyPart();
				mpart.setContent(message, "text/html");

				// Create the attachment
				MimeBodyPart mattachPart = null;
				Multipart mp = new MimeMultipart();
				mp.addBodyPart(mpart);

				javax.activation.FileDataSource dataSource = new javax.activation.FileDataSource(
						attachment);
				mattachPart = new MimeBodyPart();
				mattachPart.setDataHandler(new javax.activation.DataHandler(
						dataSource));
				mattachPart.setFileName(dataSource.getName());
				// Default the description to the file name only
				mattachPart.setDescription(dataSource.getFile().getName());

				// add the attachment to the body part
				mp.addBodyPart(mattachPart);
				/**
				 * Another Way Could be SMTPMessage smtp = new
				 * SMTPMessage(session); smtp.setFrom(addressFrom);
				 * smtp.setSubject(subject); smtp.setContent(message,
				 * "text/plain"); smtp.addRecipients(Message.RecipientType.TO,
				 * addressTo); smtp.setReturnOption(SMTPMessage.RETURN_HDRS);
				 * smtp.setNotifyOptions(SMTPMessage.NOTIFY_DELAY|SMTPMessage.
				 * NOTIFY_FAILURE|SMTPMessage.NOTIFY_SUCCESS);
				 * Transport.send(smtp);
				 * 
				 */

				msg.setContent(mp);
				Transport.send(msg);
				logger.info("newPostHTMLMailWithAttachment() :: Mail Sent Successfully.");
			} else {
				return false;
			}
		} catch (SMTPAddressFailedException e) {
			logger.info("newPostHTMLMailWithAttachment() :: SMTPAddressFailed Exception  :: >"+ e.getMessage());
			e.printStackTrace();
			Utilities.printStackTraceToLogs(SendMailUsingAuthentication.class.getName(),"newPostHTMLMailWithAttachment()", e);
			return false;
		} catch (SendFailedException e) {
			logger.info("newPostHTMLMailWithAttachment() :: SendFailed Exception  :: >"+ e.getMessage());
			e.printStackTrace();
			Utilities.printStackTraceToLogs(SendMailUsingAuthentication.class.getName(),"newPostHTMLMailWithAttachment()", e);
			return false;
		} catch (MessagingException mex) {
			logger.info("newPostHTMLMailWithAttachment() :: Messaging Exception  :: >"
					+ mex.getMessage());
			mex.printStackTrace();
			Utilities.printStackTraceToLogs(
					SendMailUsingAuthentication.class.getName(),
					"newPostHTMLMailWithAttachment()", mex);
			return false;
		} catch (Exception e) {
			logger.info("newPostHTMLMailWithAttachment() :: Exception  :: >"
					+ e.getMessage());
			e.printStackTrace();
			Utilities.printStackTraceToLogs(
					SendMailUsingAuthentication.class.getName(),
					"newPostHTMLMailWithAttachment()", e);
			return false;
		}
		logger.info("newPostHTMLMailWithAttachment() :: Method Ends.");
		return true;
	}
	
	private static Authenticator auth = new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
        	String username = smtpUser;
			String password = smtpPassword;
        	return new PasswordAuthentication(username, password);
         }
	};
}