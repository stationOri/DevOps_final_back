package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Login;
import org.example.oristationbackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface messageRepository extends JpaRepository<Message, Integer> {
}
