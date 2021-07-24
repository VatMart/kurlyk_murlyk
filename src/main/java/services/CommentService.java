package services;

import dao.*;
import entities.Comment;
import entities.response_messages.CommentResponse;
import entities.response_messages.CommentsResponse;

import java.util.List;

public class CommentService {
    private CommentDAO commentDAO = new CommentDAOImpl();

    public Comment findById(long id) {
        return commentDAO.findById(id);
    }

    public CommentResponse findByIdForResponse(long id) {
        return commentDAO.findByIdForResponse(id);
    }

    public List<Comment> findByUser(long user_id) {
        return commentDAO.findByUser(user_id);
    }

    public List<Comment> findByUser(long user_id, int limitNumber, OrderType orderType) {
        return commentDAO.findByUser(user_id, limitNumber, orderType);
    }

    public List<Comment> findByPost(long post_id) {
        return commentDAO.findByPost(post_id);
    }

    public List<Comment> findByPost(long post_id, int limitNumber, OrderType orderType) {
        return commentDAO.findByPost(post_id, limitNumber, orderType);
    }

    public List<CommentResponse> getByOrder(long post_id, int from, int to, CommentsResponse.SortingType order) {
        return commentDAO.getByOrder(post_id, from, to, order);
    }

    public void save(Comment comment) {
        commentDAO.save(comment);
    }

    public void update(Comment comment) {
        commentDAO.update(comment);
    }

    public void delete(Comment comment) {
        commentDAO.delete(comment);
    }

    public List<Comment> findAll() {
        return commentDAO.findAll();
    }
}
