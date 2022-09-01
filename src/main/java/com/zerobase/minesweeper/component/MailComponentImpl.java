package com.zerobase.minesweeper.component;

import com.zerobase.minesweeper.exception.GamerException;
import com.zerobase.minesweeper.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MailComponentImpl implements MailComponent{

    private final JavaMailSender javaMailSender;

    @Override
    public boolean sendMail(String emailAddress, String subject, String text) {

        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(emailAddress);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
        };

        try {

            javaMailSender.send(msg);
            return true;
            //throw new GamerException(ErrorCode.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            throw new GamerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
