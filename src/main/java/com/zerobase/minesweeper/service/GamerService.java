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
    public void updateGamerName(String email, String name) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        gamer.setName(name);
        gamerRepository.save(gamer);

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
    public List<GamerDto> getGamers() {

        List<Gamer> gamers = gamerRepository.findAll();

        return gamers.stream()
                .filter(gamer -> gamer.getRole().equals(Role.ROLE_USER))
                .map(gamer -> GamerDto.builder()
                        .id(gamer.getId())
                        .name(gamer.getName())
                        .mail(gamer.getMail())
                        .isVerified(gamer.isVerified())
                        .isSuspend(gamer.isSuspend())
                        .regDt(gamer.getRegDt())
                        .verifiedDt(gamer.getVerifiedDt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public GamerDto getGamerInfo(Long gamerId) {

        Gamer gamer = gamerRepository.findById(gamerId).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        return GamerDto.builder()
                .id(gamer.getId())
                .name(gamer.getName())
                .mail(gamer.getMail())
                .isVerified(gamer.isVerified())
                .isSuspend(gamer.isSuspend())
                .regDt(gamer.getRegDt())
                .verifiedDt(gamer.getVerifiedDt())
                .build();
    }

    @Transactional
    public void deleteGamer(Long gamerId) {

        Gamer gamer = gamerRepository.findById(gamerId).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        gamerRepository.delete(gamer);

    }

    @Transactional
    public void suspendGamer(Long gamerId, boolean suspend) {

        Gamer gamer = gamerRepository.findById(gamerId).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        gamer.setSuspend(suspend);

        gamerRepository.save(gamer);

    }

    @Transactional
    public List<GamerDto> searchGamers(String keyword) {

        List<Gamer> gamers = gamerRepository.findByNameAndMailContains(keyword);

        return gamers.stream()
                .filter(gamer -> gamer.getRole().equals(Role.ROLE_USER))
                .map(gamer -> GamerDto.builder()
                        .id(gamer.getId())
                        .name(gamer.getName())
                        .mail(gamer.getMail())
                        .isVerified(gamer.isVerified())
                        .isSuspend(gamer.isSuspend())
                        .regDt(gamer.getRegDt())
                        .verifiedDt(gamer.getVerifiedDt())
                        .build())
                .collect(Collectors.toList());
    }

    /*@Transactional
    public void updateGamerPassword(String email, String oldPassword, String newPassword) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        if (BCrypt.checkpw(oldPassword, gamer.getPswd())) {

            gamer.setPswd(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            gamerRepository.save(gamer);

        } else {

            throw new GamerException(ErrorCode.PASSWORD_MIS_MATCH);
        }
    }*/

    /*@Transactional
    public void lostGamerPassword(String email) {

        Gamer gamer = gamerRepository.findByMail(email).orElseThrow(() -> new GamerException(ErrorCode.USER_NOT_FOUND));

        gamer.setPswd(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        gamerRepository.save(gamer);

        if (BCrypt.checkpw(oldPassword, gamer.getPswd())) {



        } else {

            throw new GamerException(ErrorCode.PASSWORD_MIS_MATCH);
        }
    }*/
}

