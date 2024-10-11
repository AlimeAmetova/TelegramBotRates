package ru.ametova.bot_exchange.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.ametova.bot_exchange.model.Notifications;

public interface NotificationsRepository extends CrudRepository<Notifications, Long> {
    @Query("SELECT MAX(id) FROM notifications")
    Long getLastNotificationId();



}
