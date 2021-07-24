package dao;

import entities.Comment;
import entities.response_messages.CommentResponse;
import entities.response_messages.CommentsResponse;

import java.util.List;

public interface CommentDAO {
    public CommentResponse findByIdForResponse(long id);

    public Comment findById(long id);

    public List<Comment> findByUser(long user_id);

    /**
     *
     * @param user_id
     * @param limitNumber максимальное количество возвращаемых комментариев
     * @param orderType тип сортировки по дате
     * @return
     */
    public List<Comment> findByUser(long user_id, int limitNumber, OrderType orderType);

    public List<Comment> findByPost(long post_id);
    /**
     *
     * @param post_id
     * @param limitNumber максимальное количество возвращаемых комментариев
     * @param orderType тип сортировки по дате
     * @return
     */
    public List<Comment> findByPost(long post_id, int limitNumber, OrderType orderType);

    public List<CommentResponse> getByOrder(long post_id, int from, int to, CommentsResponse.SortingType order);

    public void save(Comment comment);

    public void update(Comment comment);

    public void delete(Comment comment);

    public List<Comment> findAll();
}
