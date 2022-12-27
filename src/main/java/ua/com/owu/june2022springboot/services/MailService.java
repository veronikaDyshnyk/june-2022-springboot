package ua.com.owu.june2022springboot.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ua.com.owu.june2022springboot.models.Customer;

import java.io.File;

@Service
@AllArgsConstructor
public class MailService {

    private JavaMailSender javaMailSender;

    public void send(Customer customer, File file) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setTo(customer.getEmail());
            helper.setText("<a href='http://localhost:8080/customers/activate/"+customer.getId()+"'>click</a>",true);
            helper.setFrom(new InternetAddress("veronika199942@gmail.com"));
            helper.addAttachment(file.getName(),file);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(mimeMessage);
    }

}
