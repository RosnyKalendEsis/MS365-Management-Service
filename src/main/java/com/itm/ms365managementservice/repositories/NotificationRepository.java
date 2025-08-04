package com.itm.ms365managementservice.repositories;

import com.itm.ms365managementservice.entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, String> {
    List<Notifications> findAllByMsgRead(boolean read);
}
