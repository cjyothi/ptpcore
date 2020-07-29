package com.dms.ptp.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.dms.ptp.repository.UserRepository;
import com.dms.ptp.controller.AffinityReachCalculator;
import com.dms.ptp.dto.Mail;
import com.dms.ptp.service.EmailService;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailUtil {

	@Autowired
	private EmailService emailService;
	
	@Autowired
    private JavaMailSender javaMailSender;

	@Autowired
	private Environment env;
	
	@Autowired
	UserRepository repo;
	
	static Logger logger = LoggerFactory.getLogger(EmailUtil.class);

	public void sendMailForSignUp(String userName) throws MessagingException, IOException, TemplateException {
		logger.info("inside sendMailForSignUp: " + userName);
		List<String> toList = new ArrayList<String>();
		toList.add(userName);
		Mail mail = new Mail();
	//	mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setFrom("arnabashutosh.d@tataelxsi.co.in");
		mail.setTo(toList);
		mail.setSubject("Registration Pending");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("userName", userName);
		mail.setModel(model);
		String mailTemplate = "email-templateSignUp.ftl";
	//	String username = env.getProperty("spring.mail.username");

		emailService.sendSimpleMessage(mail, userName, mailTemplate);
	}

	public void sendMailToAdmin(String username, int role) throws MessagingException, IOException, TemplateException {
		logger.info("inside sendMailToAdmin. username: " + username);
		
		SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("arnabashutosh.d@tataelxsi.co.in");
        msg.setFrom("arnabashutosh.d@tataelxsi.co.in");
        msg.setSubject("Registration Request for Accept/Reject");
        msg.setText("Registration Request for " + username  + " is pending\n. "
        		+ "Please approve or reject.");

        javaMailSender.send(msg);
	}
	
	
	public void sendMail(String username, String flag, String reason) throws MessagingException, IOException, TemplateException {
		logger.info("inside sendMail. mail To: " + username);
		List<String> toList = new ArrayList<String>();
		toList.add(username);
		Mail mail = new Mail();
	//	mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setFrom("arnabashutosh.d@tataelxsi.co.in");
		mail.setTo(toList);
		if (flag.equals("Y")) {
			mail.setSubject("Registration Successful");

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("firstName", username);
			mail.setModel(model);
			String mailTemplate = "email-templateAccept.ftl";
			emailService.sendSimpleMessage(mail, username, mailTemplate);
		} else {
			SimpleMailMessage msg = new SimpleMailMessage();
	        msg.setTo(username);
	        msg.setFrom("arnabashutosh.d@tataelxsi.co.in");

	        msg.setSubject("Registration UnSuccessful");
	        msg.setText("Registration Request for " + username  + " has been denied by the Administrator\n. "
	        		+ "Reason: " + reason);

	        javaMailSender.send(msg);
		}
	}
	
	
	
	public void sendOTPMail(String otp, String username) throws MessagingException, IOException, TemplateException {
		logger.info("inside sendOTPMail: " + "otp: " + otp  + "mail To: "  + username);
		List<String> toList = new ArrayList<String>(); 
		toList.add(username);
		Mail mail = new Mail();
		mail.setTo(toList);
	//	mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setFrom("arnabashutosh.d@tataelxsi.co.in");
		mail.setSubject("Login OTP");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("otp", otp);
		mail.setModel(model);
		String mailTemplate = "email-templateOTP.ftl";
	//	String username1 = env.getProperty("spring.mail.username");

		emailService.sendSimpleMessage(mail, username, mailTemplate);
	}
	
	public void sendMailToDMSAdmin(String username) throws MessagingException, IOException, TemplateException {
        logger.info("inside sendMailToDMSAdmin. username: " + username);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("arnabashutosh.d@tataelxsi.co.in");
        msg.setFrom("arnabashutosh.d@tataelxsi.co.in");
        msg.setSubject("Request for Campaign Accept/Reject");
        msg.setText("Campaign Request for " + username  + " is pending\n. "
                + "Please approve or reject.");

        javaMailSender.send(msg);
    }
	
	public void sendMailCampaign(String username, String flag, String reason) throws MessagingException, IOException, TemplateException {
        logger.info("inside sendMail. mail To: " + username);

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
