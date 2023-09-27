package com.Rest.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.Rest.forum.entity.Post;

import jakarta.transaction.Transactional;

public interface PostRepository extends CrudRepository<Post, Integer> {

    List<Post> findByAuthorId(Integer userId);

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.content = ?2 WHERE p.id = ?1")
    void updatePost(Integer postId, String content);
}
