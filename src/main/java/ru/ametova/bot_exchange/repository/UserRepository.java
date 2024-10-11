package ru.ametova.bot_exchange.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ametova.bot_exchange.model.Users;



public interface UserRepository extends CrudRepository<Users, Long> {

}
