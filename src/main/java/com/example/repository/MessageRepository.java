package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

//new commit to see if my passing tests register
@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {
    Message findById(int id);

    @Query("SELECT m FROM Message m INNER JOIN Account a ON a.accountId = m.postedBy WHERE m.id = :id")
    List<Message> findAllMessagesByAccountId(int id);
}