package com.Rest.forum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rest.forum.entity.Post;
import com.Rest.forum.repository.CommentRepository;
import com.Rest.forum.repository.LikeCRUDRepository;
import com.Rest.forum.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeCRUDRepository likeCRUDRepository;
    
    @Autowired
    private CommentRepository commentRepository;

    public List<Post> getMyPosts(Integer userid) {
        return postRepository.findByAuthorId(userid);
    }

    @Transactional
    public void deletePostById(Integer id) {
        Post postToDelete = postRepository.findById(id).orElse(null);

        if (postToDelete != null) {
            postToDelete.setAuthor(null);

            postRepository.delete(postToDelete);
        }
    }

    @Transactional
    public void deleteLikeAndComment(Integer id) {
        likeCRUDRepository.deleteByPostId(id);
        commentRepository.deleteByPostId(id);
    }

    public void editPost(Integer id) {

    }

}
