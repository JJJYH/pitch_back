package com.server.pitch.common.repository;

import com.server.pitch.common.domain.Notificate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface NotificateRepository extends CrudRepository<Notificate, String> {
    //public Notificate save(Notificate notificate);
    public List<Notificate> findAllByUserId(String user_id);
}
