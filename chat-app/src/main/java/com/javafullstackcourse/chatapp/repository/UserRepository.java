package com.javafullstackcourse.chatapp.repository;

import com.javafullstackcourse.chatapp.models.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ChatUser, Integer> {

    ChatUser getById(int id);
}
