package com.server.pitch.common.service;

import com.server.pitch.common.domain.Notificate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

public interface NotificateService {
    public void createNotification(List<Notificate> notificates);
    public void registerClientForNotification(String user_id, DeferredResult<List<String>> deferredResult);
    public void notifyClient(List<String> userIds);
    public List<Notificate> findAllByUserId(String user_id);
    public void delteNotificate(String id);
}
