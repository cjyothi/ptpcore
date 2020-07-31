package com.ptp.campaign.util;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailUtil {

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendMailToDMSAdmin(String username) throws MessagingException, IOException, TemplateException {
		log.info("inside sendMailToDMSAdmin. username: " + username);

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("arnabashutosh.d@tataelxsi.co.in");
		msg.setFrom("arnabashutosh.d@tataelxsi.co.in");
		msg.setSubject("Request for Campaign Accept/Reject");
		msg.setText("Campaign Request for " + username  + " is pending\n. "
				+ "Please approve or reject.");

		javaMailSender.send(msg);
	}


	public void sendMail(String username, String flag, String reason) throws MessagingException, IOException, TemplateException {
		log.info("inside sendMail. mail To: " + username);

		if (flag.equals("Y")) {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setSubject("Campaign approval Successful");
			msg.setTo(username);
			msg.setFrom("arnabashutosh.d@tataelxsi.co.in");
			msg.setText("Hi, Campaign approved successfully.");

			javaMailSender.send(msg);
		} else {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setSubject("Campaign approval UnSuccessful");
			msg.setTo(username);
			msg.setFrom("arnabashutosh.d@tataelxsi.co.in");
			msg.setText("Hi, Campaign rejected due to following reason : " + reason);

			javaMailSender.send(msg);
		}
	}
}
