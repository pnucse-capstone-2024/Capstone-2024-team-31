package com.wasin.wasin.service.impl;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.NotFoundException;
import com.wasin.wasin.domain.dto.UserRequest;
import com.wasin.wasin.domain.entity.Email;
import com.wasin.wasin.domain.validation.MailValidation;
import com.wasin.wasin.domain.validation.UserValidation;
import com.wasin.wasin.repository.MailRepository;
import com.wasin.wasin.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 *  [이메일 검증]
 *  1. 이메일 확인을 위해 코드를 전송한다.
 *  2. 이메일 코드를 확인한다.
 *  3. 회원가입
 *
 *  [조건]
 *  1. 레디스에 이메일 데이터를 저장한다.
 *     해당 내용은 1시간 동안 유효하다.
 *  2. 이메일 인증 코드의 유효기간은 5분이다.
 *  3. 검증이 완료된 경우에만 회원가입이 가능하다.
 *     레디스의 이메일 데이터에서 이를 관리한다.
 */

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final UserValidation userValidation;
    private final MailRepository mailRepository;
    private final MailValidation mailValidation;

    @Value("${spring.mail.email}")
    private String fromMail;

    public void sendMail(UserRequest.EmailDTO requestDTO) {
        String toMail = requestDTO.email();

        // 이메일이 존재하는지 확인
        userValidation.checkEmailAlreadyExist(toMail);

        // 메일 코드 생성(6자리 수)
        String mailCode = createMailCode();

        // MimeMessage 생성 및 메일 전송
        MimeMessage message = createMessage(toMail, mailCode);
        javaMailSender.send(message);

        // 메일 객체 생성 - 검증 전이므로 isVerified = false
        Email email = Email.builder().email(toMail).code(mailCode).isVerified(false).build();

        // 해당 메일의 데이터를 모두 삭제 -> 제일 최신 값만 유효하도록 변경
        mailRepository.deleteById(toMail);

        // 새로운 메일 내용 저장
        mailRepository.save(email);
    }

    public void checkMailCode(UserRequest.EmailCheckDTO requestDTO) {
        Email email = findByEmail(requestDTO.email());

        // 유효성 검증 - 이메일 코드가 일치한지,  만료되진 않았는지
        mailValidation.checkCodeValid(email, requestDTO.code());

        // 검증이 완료되었으므로 isVerified = true
        email.updateVerified();
        
        // 이메일 내용 업데이트
        mailRepository.save(email);
    }

    public void checkVerified(String email) {
        Email email2 = findByEmail(email);

        // 이메일 검증 완료되었는지 확인
        mailValidation.checkVerified(email2);
    }

    // 레디스 내에서 이메일 데이터 찾기
    private Email findByEmail(String email) {
        return mailRepository.findById(email).orElseThrow(
                () -> new NotFoundException(BaseException.EMAIL_NOT_VERIFIED)
        );
    }

    // 이메일 객체 생성
    private MimeMessage createMessage(String email, String mailCode) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(fromMail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[와신상담] 인증 코드");
            message.setText(getBody(mailCode),"UTF-8", "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    // 이메일 메시지로 들어갈 내용 작성
    private String getBody(String mailCode) {
        String body = "<h3 style='color:#3A7DFF'> 안녕하세요. 와신상담 서비스입니다. </h3>";
        body += "<p>" + "인증코드 확인 후 회원가입을 진행해주세요. 아래 인증 모드를 복사하여 입력해 주시기 바랍니다." + "</p> <br>";
        body += "<h3 style='color:#3A7DFF'> *이메일 인증 코드: " + mailCode + "</h3>";
        return body;
    }

    // 6자리 랜덤 값 생성
    private String createMailCode() {
        int code = (int)(Math.random() * (90000)) + 100000;
        return Integer.toString(code);
    }
}
