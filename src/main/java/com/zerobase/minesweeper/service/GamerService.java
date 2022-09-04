package com.zerobase.minesweeper.service;

import com.zerobase.minesweeper.component.MailComponent;
import com.zerobase.minesweeper.dto.GamerDto;
import com.zerobase.minesweeper.entity.Gamer;
import com.zerobase.minesweeper.exception.GamerException;
import com.zerobase.minesweeper.repository.GamerRepository;
import com.zerobase.minesweeper.type.ErrorCode;
import com.zerobase.minesweeper.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GamerService {

    private final GamerRepository gamerRepository;
    private final MailComponent mailComponent;

    @Transactional
    public boolean emailDuplicateCheck(String email) {

        return gamerRepository.existsByMail(email);

    }

    @Transactional
    public void gamerSignUp(String email, String name, String password) {

        String validationKey = generateValidationKey();
        System.out.println(validationKey);

        String encValidationKey = BCrypt.hashpw(validationKey, BCrypt.gensalt());
        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        gamerRepository.save(Gamer.builder()
                .name(name)
                .pswd(encPassword)
                .mail(email)
                .authCode(encValidationKey)
                .isVerified(false)
                .isSuspend(false)
                .verifiedDt(LocalDateTime.MIN)
                .role(Role.ROLE_USER).build());

        sendValidationKey(email, validationKey);
    }

    private String generateValidationKey() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

    private boolean sendValidationKey(String email, String validationKey) {
        String subject = validationKey + "는 회원님의 인증코드 입니다. Minesweeper";
        String text = "<p>안녕하세요 Minesweeper 입니다.<p>" +
                "<p>회원님의 인증코드는</p>" +
                "<h2>" + validationKey + "</h2>" +
                "<p>입니다.</p>" +
                "<p>인증번호를 입력한 후 회원 가입을 마무리 해 주세요!</p>" +
                "<p>감사합니다</p>" +
                "<div><a target='_blank' href='???'> Minesweeper 로 이동하기 </a></div>";
        return mailComponent.sendMail(email, subject, text);
    }

    @Transactional
    public void gamerActivation(String email, String validationKey) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        if (!gamer.isVerified()) {
            if (BCrypt.checkpw(validationKey, gamer.getAuthCode())) {

                gamer.setVerified(true);
                gamer.setVerifiedDt(LocalDateTime.now());
                gamerRepository.save(gamer);

            } else {
                throw new GamerException(ErrorCode.VALIDATION_KEY_MIS_MATCH);
            }
        } else {
            throw new GamerException(ErrorCode.USER_ALREADY_VALIDATED);
        }
    }

    @Transactional
    public void reissueValidationKey(String email) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        if (!gamer.isVerified()) {

            String validationKey = generateValidationKey();
            String encValidationKey = BCrypt.hashpw(validationKey, BCrypt.gensalt());

            gamer.setAuthCode(encValidationKey);
            gamerRepository.save(gamer);

            sendValidationKey(email, validationKey);

        } else {
            throw new GamerException(ErrorCode.USER_ALREADY_VALIDATED);
        }
    }

    @Transactional
    public void updateGamerName(String email, String name, String password) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        if (BCrypt.checkpw(password, gamer.getPswd())) {

            gamer.setName(name);
            gamerRepository.save(gamer);

        } else {

            throw new GamerException(ErrorCode.PASSWORD_MIS_MATCH);
        }

    }

    @Transactional
    public void withdrawalGamer(String email, String password) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        if (BCrypt.checkpw(password, gamer.getPswd())) {

            gamerRepository.delete(gamer);

        } else {

            throw new GamerException(ErrorCode.PASSWORD_MIS_MATCH);
        }

    }



    @Transactional
    public void updateGamerPassword(String email, String oldPassword, String newPassword) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        if (BCrypt.checkpw(oldPassword, gamer.getPswd())) {

            gamer.setPswd(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            gamerRepository.save(gamer);

        } else {

            throw new GamerException(ErrorCode.PASSWORD_MIS_MATCH);
        }
    }

    @Transactional
    public void lostGamerPassword(String email) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        String password = generateRandomPassword();
        gamer.setPswd(BCrypt.hashpw(password, BCrypt.gensalt()));
        gamerRepository.save(gamer);

        sendRandomPassword(email, password);

    }

    private String generateRandomPassword() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(10000000, 100000000));
    }

    private boolean sendRandomPassword(String email, String randomPassword) {
        String subject = randomPassword + "는 회원님의 임시 비밀번호 입니다. Minesweeper";
        String text = "<p>안녕하세요 Minesweeper 입니다.<p>" +
                "<p>회원님의 임시 비밀번호는</p>" +
                "<h2>" + randomPassword + "</h2>" +
                "<p>입니다.</p>" +
                "<p>임시 비밀번호를 사용하여 로그인 하신 후, 비밀번호를 변경해 주세요</p>" +
                "<p>감사합니다</p>" +
                "<div><a target='_blank' href='???'> Minesweeper 로 이동하기 </a></div>";
        return mailComponent.sendMail(email, subject, text);
    }
}

