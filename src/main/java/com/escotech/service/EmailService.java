package com.escotech.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.escotech.entity.Order;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.escotech.SpringMailConfig;

@Service
public class EmailService {

    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static final String EMAIL_EDITABLE_TEMPLATE_CLASSPATH_RES = "classpath:mail/editablehtml/email-editable.html";

    private static final String BACKGROUND_IMAGE = "mail/editablehtml/images/background.png";
    private static final String LOGO_BACKGROUND_IMAGE = "mail/editablehtml/images/logo-background.png";
    private static final String ESCOTECH_LOGO_IMAGE = "mail/editablehtml/images/escotech-logo.png";
    private static final String THUMBNAIL_IMAGE = "mail/editablehtml/images/thumbnail.png";

    private static final String PNG_MIME = "image/png";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JavaMailSender mailSender;

    @Qualifier("htmlTemplateEngine")
    @Autowired
    private TemplateEngine stringTemplateEngine;

    public String getEditableMailTemplate() throws IOException {
        final Resource templateResource = this.applicationContext.getResource(EMAIL_EDITABLE_TEMPLATE_CLASSPATH_RES);
        final InputStream inputStream = templateResource.getInputStream();
        return IOUtils.toString(inputStream, SpringMailConfig.EMAIL_TEMPLATE_ENCODING);
    }

    public void sendEditableMail(Order order, final Locale locale)
                                    throws MessagingException {

        // Creates message
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message
                = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject("EscoTech - Receipt");
        message.setFrom("escotech@gmail.com");
        message.setTo(order.getEmail());

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("order", order);
        String myTemplate = null;
        try {
            myTemplate = getEditableMailTemplate();
        } catch (IOException e) {
            logger.error("sendEditableMail: " + e.getMessage());
        }
        // Create the HTML body with Thymeleaf
        final String output = stringTemplateEngine.process(myTemplate, ctx);
        message.setText(output, true /* isHtml */);

        // Add the inline images, referenced from the HTML code as "cid:image-name"
        message.addInline("background", new ClassPathResource(BACKGROUND_IMAGE), PNG_MIME);
        message.addInline("logo-background", new ClassPathResource(LOGO_BACKGROUND_IMAGE), PNG_MIME);
        message.addInline("escotech-logo", new ClassPathResource(ESCOTECH_LOGO_IMAGE), PNG_MIME);
        message.addInline("thumbnail", new ClassPathResource(THUMBNAIL_IMAGE), PNG_MIME);

        // Send emmail
        this.mailSender.send(mimeMessage);
    }
}
