package kr.adapterz.springboot.repository;

import kr.adapterz.springboot.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepository {
    private final Map<Integer, Post> posts = new HashMap<>();
    private int sequence = 1;

    public int nextPostId() {
        return sequence++;
    }

    public Post save(Post post) {
        posts.put(post.getPostId(), post);
        return post;
    }

    public List<Post> findAll() {
        List<Post> findPosts = new ArrayList<>(posts.values());
        return findPosts;
    }
    public Post findPostById(int postId) {
        return posts.get(postId);
    }

    public void deletePost(int postId) {
        posts.remove(postId);
    }

}