package com.serviceTests;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.Services.EmailService;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
	
	@Mock
	private JavaMailSender mailSender;
	
	@InjectMocks
	private EmailService serviceUnderTest;
	
	@Test
	void canSendEmail() {
		
        String sender = "agromart@agromart.com";
        String sentTo = "to";
        String subject = "subject";
        String text = "text";
        
        
        serviceUnderTest.sendMail(sentTo, subject, text);
        
        ArgumentCaptor<SimpleMailMessage> mailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
	
        verify(mailSender).send(mailCaptor.capture());
        
        SimpleMailMessage capturedMail = mailCaptor.getValue();
        
        assertEquals(capturedMail.getFrom(),sender);
        assertEquals(capturedMail.getTo()[0],sentTo);
        assertEquals(capturedMail.getSubject(),subject);
        assertEquals(capturedMail.getText(),text);
        
        
	}

}
