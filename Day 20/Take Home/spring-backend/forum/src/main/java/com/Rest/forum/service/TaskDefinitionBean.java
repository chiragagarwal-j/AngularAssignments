package com.Rest.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rest.forum.entity.Post;
import com.Rest.forum.repository.PostRepository;



@Service
public class TaskDefinitionBean implements Runnable {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    private Integer id;
    private Post post;

    public TaskDefinitionBean() {
    }

    public TaskDefinitionBean(Post post, PostRepository postRepository) {
        this.post = post;
        this.postRepository = postRepository;
    }

    public TaskDefinitionBean(Integer id, PostService postService) {
        this.id = id;
        this.postService = postService;
    }

    @Override
    public void run() {
        if (post != null) {
            postRepository.save(post);
        }

        if (id != null) {
            postService.deleteLikeAndComment(id);
            postService.deletePostById(id);
        }
    }
}
