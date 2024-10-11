package ru.ametova.bot_exchange.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Data
public class Users {
    @Id
    private Long chatIdUser;
    private String userNameUser;
    private String firstNameUser;
    private String lastNameUser;
    private Timestamp registeredAtUser;

}