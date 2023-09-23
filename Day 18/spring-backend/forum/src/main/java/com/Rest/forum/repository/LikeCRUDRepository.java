package com.Rest.forum.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.Rest.forum.entity.LikeRecord;
import com.Rest.forum.entity.Post;

public interface LikeCRUDRepository extends CrudRepository<LikeRecord, Integer> {
    public Integer countByLikeIdPost(Post post);

    @Modifying
    @Query(value = "DELETE FROM like_record WHERE post_id = ?1", nativeQuery = true)
    void deleteByPostId(Integer postId);
}
