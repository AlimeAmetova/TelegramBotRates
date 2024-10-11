package ru.ametova.bot_exchange.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "notifications")
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    @Column(name = "notice", length = 2000)
    private String notice;
    
}
