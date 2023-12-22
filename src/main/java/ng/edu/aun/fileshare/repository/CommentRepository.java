package ng.edu.aun.fileshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ng.edu.aun.fileshare.model.Comment;
import ng.edu.aun.fileshare.model.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByUserAndContent(User user, Comment comment);
}
