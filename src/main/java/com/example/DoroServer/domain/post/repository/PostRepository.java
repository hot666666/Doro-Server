package com.example.DoroServer.domain.post.repository;

import com.example.DoroServer.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Page<Post> findByIsAnsweredFalse(Pageable pageable);
}
