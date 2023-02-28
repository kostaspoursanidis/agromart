package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService{
    
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendMail(String to,String subject,String text){ 

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("agromart@agromart.com");
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
        
    }

}
