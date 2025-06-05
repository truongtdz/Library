package com.build.core_restful.system;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.build.core_restful.domain.response.BookLateResponse;
import com.build.core_restful.domain.response.BookRecommendResponse;

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

    public void sendEmailRecommendBook(String to, String name, List<BookRecommendResponse> books) {
        Context context = new Context();
        context.setVariable("now", Instant.now());
        context.setVariable("books", books);

        String htmlContent = templateEngine.process("recommend", context);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Xin chào " + name);
            helper.setText(htmlContent, true);
            helper.setFrom("HaUiLibrary");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email", e);
        }
    }

    public void sendEmailLateBook(String to, String name, List<BookLateResponse> books) {
        Context context = new Context();
        context.setVariable("now", Instant.now());
        context.setVariable("books", books);

        String htmlContent = templateEngine.process("late", context);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Xin chào " + name);
            helper.setText(htmlContent, true);
            helper.setFrom("HaUiLibrary");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email", e);
        }
    }
}

