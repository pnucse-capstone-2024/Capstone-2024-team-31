package com.wasin.wasin._core.util;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.ServerException;
import com.wasin.wasin.domain.entity.Router;
import com.wasin.wasin.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SendFirebaseMessage {

    public void sendFcmAlert(List<User> userList, Router router, String title, String body) {
        try {
            List<String> fcmTokenList = userList.stream().map(User::getFcmToken).toList();
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            FirebaseMessaging.getInstance().sendEachForMulticast(
                    MulticastMessage.builder().setNotification(notification)
                            .putData("routerId", router.getId().toString())
                            .addAllTokens(fcmTokenList)
                            .build()
            );
        } catch (FirebaseMessagingException e) {
            throw new ServerException(BaseException.FIREBASE_MESSAGE_FAIL);
        }
    }

}
