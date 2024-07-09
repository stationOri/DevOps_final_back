package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.ChatRoom;
import org.example.oristationbackend.entity.Empty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface emptyRepository extends JpaRepository<Empty, Integer> {
}
