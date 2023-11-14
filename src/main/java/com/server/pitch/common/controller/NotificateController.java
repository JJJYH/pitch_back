package com.server.pitch.common.controller;

import com.server.pitch.aop.GetUserAccessToken;
import com.server.pitch.common.domain.Notificate;
import com.server.pitch.common.service.NotificateService;
import com.server.pitch.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/api/notificate")
public class NotificateController {

    @Autowired
    private NotificateService notificateService;

    //ExecutorService executorService = Executors.newCachedThreadPool();

    @PostMapping("/create")
    public ResponseEntity<String> createNoti(@RequestBody Map<String, Object> requestMap){
        List<String> userIds = (List<String>) requestMap.get("userIds");
        String message = (String) requestMap.get("message");
        String url = null;
        if(requestMap.get("url")!=null) {
            url = (String) requestMap.get("url");
        }
        log.info(userIds.toString());
        log.info(message);
        List<Notificate> notificateList = new ArrayList<>();
        for (String userId : userIds) {
            Notificate notificate = new Notificate();
            notificate.setUserId(userId);
            notificate.setText(message);
            notificate.setDate(new Date());
            if(url!=null) {
                notificate.setUrl(url);
            }
            notificateList.add(notificate);
        }
        log.info(notificateList.toString());


        notificateService.createNotification(notificateList);
        notificateService.notifyClient(userIds);

        return ResponseEntity.ok("Notifications creation process initiated");
    }

    @GetUserAccessToken
    @PostMapping("/polling")
    public DeferredResult<List<String>> pollingNotifications(Users loginUser){
        DeferredResult<List<String>> deferredResult = new DeferredResult<>(5000L, List.of());

        notificateService.registerClientForNotification(loginUser.getUser_id(), deferredResult);

        deferredResult.onTimeout(()-> deferredResult.setResult(List.of("No new notifications")));

        return deferredResult;
    }

    @GetUserAccessToken
    @GetMapping("/get-noti")
    public ResponseEntity<List<Notificate>> getNotificateList(Users loginUser){
        log.info("===============================get noti list=================================");
        List<Notificate> result = notificateService.findAllByUserId(loginUser.getUser_id());
        log.info(result.toString());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/delete-noti")
    public ResponseEntity<String> deleteNotificate(@RequestParam String id){
        log.info("=============================== delete notification By : "+id+"  =================================");
        notificateService.delteNotificate(id);
        return ResponseEntity.ok("delete");
    }
}
