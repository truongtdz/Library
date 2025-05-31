package com.build.core_restful.util.system;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.build.core_restful.domain.response.BookSendEmailResponse;

import java.time.Instant;
import java.util.List;

@Service
public class EmailUtil {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailUtil(
        JavaMailSender mailSender,
        TemplateEngine templateEngine
    ){
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    };

    public void sendEmailTemplate(String to, String name, List<BookSendEmailResponse> books) {
        Context context = new Context();
        context.setVariable("now", Instant.now());
        context.setVariable("books", books);

        String htmlContent = templateEngine.process("template", context);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Xin chào " + name);
            helper.setText(htmlContent, true);
            helper.setFrom("truongtd0190@gmail.com");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email", e);
        }
    }
}

