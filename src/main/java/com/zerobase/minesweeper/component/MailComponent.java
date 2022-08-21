package com.zerobase.minesweeper.component;

public interface MailComponent {
    boolean sendMail(String emailAddress, String subject, String text);
}
