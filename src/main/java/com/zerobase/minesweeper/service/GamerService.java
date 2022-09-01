package com.zerobase.minesweeper.service;

import com.zerobase.minesweeper.component.MailComponent;
import com.zerobase.minesweeper.entity.Gamer;
import com.zerobase.minesweeper.repository.GamerRepository;
import com.zerobase.minesweeper.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.ThreadLocalRandom;

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
    public boolean gamerSignUp(String email, String name, String password) {

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
                .role(Role.ROLE_USER).build());

        sendValidationKey(email, validationKey);

        return true;
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
    public boolean gamerActivation(String email, String validationKey) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow();

        if (!gamer.isVerified()) {
            if (BCrypt.checkpw(validationKey, gamer.getAuthCode())) {

                gamer.setVerified(true);
                gamerRepository.save(gamer);

                return true;

            } else {
                return false;
            }
        } else {
            throw new RuntimeException("이미 인증 처리된 계정입니다");
        }
    }

    @Transactional
    public boolean reissueValidationKey(String email) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        if (!gamer.isVerified()) {

            String validationKey = generateValidationKey();
            String encValidationKey = BCrypt.hashpw(validationKey, BCrypt.gensalt());

            gamer.setAuthCode(encValidationKey);
            gamerRepository.save(gamer);

            sendValidationKey(email, validationKey);

            return true;

        } else {
            throw new RuntimeException("이미 인증 처리된 계정입니다");
        }
    }

    @Transactional
    public boolean updateGamerInfo(String email, String name) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        gamer.setName(name);
        gamerRepository.save(gamer);

        return true;
    }

    @Transactional
    public boolean deleteGamer(String email, String password) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        if (BCrypt.checkpw(password, gamer.getPswd())) {

            gamerRepository.delete(gamer);

            return true;

        } else {

            return false;
        }

    }
}

