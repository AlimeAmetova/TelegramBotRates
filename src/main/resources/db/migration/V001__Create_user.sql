CREATE TABLE `users` (
  `chat_id_user` bigint NOT NULL,
  `first_name_user` varchar(255) DEFAULT NULL,
  `last_name_user` varchar(255) DEFAULT NULL,
  `registered_at_user` datetime(6) DEFAULT NULL,
  `user_name_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`chat_id_user`)
);