package com.wasin.wasin.service.impl;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.NotFoundException;
import com.wasin.wasin._core.util.SshConnectionUtil;
import com.wasin.wasin._core.util.web_api.WebApiUtil;
import com.wasin.wasin.domain.dto.RouterRequest;
import com.wasin.wasin.domain.dto.RouterResponse;
import com.wasin.wasin.domain.entity.CompanyImage;
import com.wasin.wasin.domain.entity.Router;
import com.wasin.wasin.domain.entity.User;
import com.wasin.wasin.domain.mapper.RouterMapper;
import com.wasin.wasin.domain.validation.RouterValidation;
import com.wasin.wasin.repository.CompanyImageRepository;
import com.wasin.wasin.repository.RouterJPARepository;
import com.wasin.wasin.repository.UserJPARepository;
import com.wasin.wasin.service.RouterService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RouterServiceImpl implements RouterService {

    private final UserJPARepository userJPARepository;

    private final CompanyImageRepository companyImageRepository;
    private final RouterJPARepository routerJPARepository;
    private final RouterValidation routerValidation;
    private final RouterMapper routerMapper;
    private final WebApiUtil webApiUtil;
    private final SshConnectionUtil sshConnectionUtil;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.email}")
    private String fromMail;

    public RouterResponse.FindALl findAll(User userDetails) {
        User user = findUserById(userDetails.getId());
        List<Router> router = routerJPARepository.findAllRouterByCompanyId(user.getCompany().getId());
        CompanyImage image = getCompanyImage(user.getCompany().getId());

        return routerMapper.routerFindAllDTO(image, router);
    }

    public RouterResponse.FindByRouterId findByRouterId(User user, Long routerId) {
        Pair<Router, User> data = initDataWithPermissionCheck(user, routerId);
        CompanyImage image = getCompanyImage(data.getSecond().getCompany().getId());

        return routerMapper.routerToRouterByIdDTO(image, data.getFirst());
    }

    @Transactional
    public void create(User userDetails, RouterRequest.CreateDTO requestDTO) {
        // 1. 요청받은 라우터가 프로메테우스 서버에 있는 라우터인지 확인
        RouterResponse.WifiNetworkQuality prometheusRouter = getPrometheusRouter(requestDTO);

        // 2. 현재 서버에 없는 라우터인지 확인
        routerValidation.checkRouterNonExistInDB(requestDTO);

        // 3. 현재 서버에 라우터 추가
        User user = findUserById(userDetails.getId());
        Router router = routerMapper.dtoToRouter(requestDTO, prometheusRouter, user.getCompany());
        routerJPARepository.save(router);
    }

    @Transactional
    public void update(User user, RouterRequest.UpdateDTO requestDTO, Long routerId) {
        Pair<Router, User> data = initDataWithPermissionCheck(user, routerId);
        data.getFirst().updateColumns(requestDTO);
    }

    @Transactional
    public void delete(User user, Long routerId) {
        Pair<Router, User> data = initDataWithPermissionCheck(user, routerId);
        routerJPARepository.deleteById(data.getFirst().getId());
    }

    public RouterResponse.CompanyImageDTO findCompanyImage(User userDetails) {
        User user = findUserById(userDetails.getId());
        CompanyImage image = getCompanyImage(user.getCompany().getId());

        return routerMapper.imageEntityToDTO(image);
    }

    public RouterResponse.CheckRouter checkRouter(User user, Long routerId) {
        Pair<Router, User> data = initDataWithPermissionCheck(user, routerId);
        Router router = data.getFirst();
        String result = sshConnectionUtil.connect("./check_status", router);

        return new RouterResponse.CheckRouter(result);
    }

    public RouterResponse.LogRouter logRouter(User user, Long routerId) {
        Pair<Router, User> data = initDataWithPermissionCheck(user, routerId);
        Router router = data.getFirst();
        String result = sshConnectionUtil.connect("logread", router);

        return new RouterResponse.LogRouter(result);
    }

    public void logEmail(User user, RouterRequest.LogDTO requestDTO, Long routerId) {
        Pair<Router, User> data = initDataWithPermissionCheck(user, routerId);
        String email = data.getSecond().getEmail();
        MimeMessage message = createMessage(email, requestDTO.log(), data.getFirst());
        javaMailSender.send(message);
    }

    private MimeMessage createMessage(String email, String log, Router router) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(fromMail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[와신상담] 로그 전송");
            message.setText(getBody(log, router),"UTF-8", "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    private String getBody(String log, Router router) {
        String body = "<h3 style='color:#3A7DFF'> 안녕하세요. 와신상담 서비스입니다. </h3> <br>";
        body += "<h3 style='color:#3A7DFF'> [라우터 정보] </h3>";
        body += "<p> 이름: " + router.getName() + " </p> ";
        body += "<p> 인스턴스: " + router.getInstance() + " </p> ";
        body += "<p> MAC 주소: " + router.getMacAddress() + " </p> ";
        body += "<p> 시리얼 넘버: " + router.getSerialNumber() + " </p> ";
        body += "<p> SSH 접속 포트 번호: " + router.getPort() + " </p> <br>";
        body += "<h3 style='color:#3A7DFF'> [로그 정보] </h3>";
        body += "<p>" + log + "</p> ";

        return body;
    }
    private Pair<Router, User> initDataWithPermissionCheck(User _user, Long routerId) {
        Router router = findRouterById(routerId);
        User user = findUserById(_user.getId());
        routerValidation.checkRouterPermission(router, user);

        return Pair.of(router, user);
    }

    private CompanyImage getCompanyImage(Long companyId) {
        return companyImageRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new NotFoundException(BaseException.COMPANY_IMAGE_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userJPARepository.findActiveUserWithCompanyById(userId)
                .orElseThrow(() -> new NotFoundException(BaseException.USER_NOT_FOUND));
    }

    private Router findRouterById(Long routerId) {
        return routerJPARepository.findById(routerId)
                .orElseThrow(() -> new NotFoundException(BaseException.ROUTER_NOT_EXIST_IN_DB));
    }

    private RouterResponse.WifiNetworkQuality getPrometheusRouter(RouterRequest.CreateDTO requestDTO) {
        // 프로메테우스 서버 내에 있는 모든 라우터
        RouterResponse.prometheusRouter prometheusRouterList = webApiUtil.getPrometheusRouter();

        // 요청받은 라우터의 MAC 주소와 일치하는게 있는지 확인 -> 없으면 에러 / 있으면 반환
        return prometheusRouterList.data().stream()
                .filter(it -> it.bssid().equals(requestDTO.macAddress().toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(BaseException.ROUTER_NOT_EXIST_IN_PROMETHEUS));
    }

}
