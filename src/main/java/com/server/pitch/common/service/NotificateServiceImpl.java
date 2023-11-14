package com.server.pitch.common.service;

import com.server.pitch.common.domain.Notificate;
import com.server.pitch.common.repository.NotificateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NotificateServiceImpl implements NotificateService{

    @Autowired
    private NotificateRepository notificateRepository;

    private final Map<String, DeferredResult<List<String>>> clients = new HashMap<>();

    @Override
    public void createNotification(List<Notificate> notificates) {
        notificateRepository.saveAll(notificates);
    }

    @Override
    public void registerClientForNotification(String user_id, DeferredResult<List<String>> deferredResult) {
        clients.put(user_id, deferredResult);
    }

    @Override
    public void notifyClient(List<String> userIds) {
        for (String userId : userIds) {
            DeferredResult<List<String>> client = clients.get(userId);
            if (client != null) {
                    List<String> notificationMessage = List.of("find new notification");
                    client.setResult(notificationMessage);
                // 클라이언트에게 알림을 전송한 후 해당 클라이언트를 맵에서 제거
                clients.remove(userId);
            }
        }
    }

    @Override
    public List<Notificate> findAllByUserId(String user_id) {
        return notificateRepository.findAllByUserId(user_id);
    }

    @Override
    public void delteNotificate(String id) {
        notificateRepository.deleteById(id);
    }
}
