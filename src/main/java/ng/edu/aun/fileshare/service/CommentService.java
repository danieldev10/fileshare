package ng.edu.aun.fileshare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ng.edu.aun.fileshare.model.Comment;
import ng.edu.aun.fileshare.model.User;
import ng.edu.aun.fileshare.repository.CommentRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long id, Comment updatedComment) {
        if (commentRepository.existsById(id)) {
            updatedComment.setId(id);
            return commentRepository.save(updatedComment);
        }
        return null;
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment findByUserAndQuestion(User user, Comment comment) {
        return commentRepository.findByUserAndContent(user, comment);
    }
}
