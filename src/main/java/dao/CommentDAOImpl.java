package dao;

import entities.Comment;
import entities.response_messages.CommentResponse;
import entities.response_messages.CommentsResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactoryUtil;

import java.util.List;
import java.util.stream.Collectors;

public class CommentDAOImpl implements CommentDAO {

    @Override
    public CommentResponse findByIdForResponse(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Comment comment = session.get(Comment.class, id);
        if (comment == null) {
            session.close();
            return null;
        }
        CommentResponse commentResponse = new CommentResponse(comment);
        session.close();
        return commentResponse;
    }

    @Override
    public Comment findById(long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Comment.class, id);
    }

    @Override
    public List<Comment> findByUser(long user_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Comment> query = session.createQuery("from Comment where user.id = :user_id");
        query.setParameter("user_id", user_id);
        return getComments(session, query);
    }

    @Override
    public List<Comment> findByUser(long user_id, int limitNumber, OrderType orderType) {
        if (limitNumber < 0) limitNumber = 0;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Comment> query = session.createQuery("from Comment where user.id = :user_id order by date_writing "+orderType.toString());
        query.setParameter("user_id", user_id);
        query.setMaxResults(limitNumber);
        return getComments(session, query);
    }

    @Override
    public List<Comment> findByPost(long post_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Comment> query = session.createQuery("from Comment where post.id = :post_id");
        query.setParameter("post_id", post_id);
        return getComments(session, query);
    }

    @Override
    public List<Comment> findByPost(long post_id, int limitNumber, OrderType orderType) {
        if (limitNumber < 0) limitNumber = 0;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Comment> query = session.createQuery("from Comment where post.post_id = :post_id order by date_writing "+orderType.toString());
        query.setParameter("post_id", post_id);
        query.setMaxResults(limitNumber);
        return getComments(session, query);
    }

    @Override
    public List<CommentResponse> getByOrder(long post_id, int from, int to, CommentsResponse.SortingType order) {
        if (to < from) {
            int temp = to;
            to = from;
            from = temp;
        }
        int maxRes = (to - from)+1;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String orderHQL = "";
        switch (order) {
            case date_asc: orderHQL = "order by date_writing asc"; break;
            case date_desc: orderHQL = "order by date_writing desc"; break;
        }
        Query<Comment> query = session.createQuery("from Comment where post.post_id = :post_id "+orderHQL);
        query.setParameter("post_id", post_id);
        //System.out.println("from"+(from));
        //System.out.println("to"+(to));
        query.setFirstResult(from);
        query.setMaxResults(to);
        List<Comment> comments;

        try {
            comments = query.getResultList();
        } catch (javax.persistence.NoResultException e) {
            System.out.println(e.getMessage());
            session.close();
            return null;
        }
        List<CommentResponse> commentResponses = comments.stream().map((comment) -> new CommentResponse(comment)).collect(Collectors.toList());
        session.close();
        return commentResponses;
    }

    @Override
    public void save(Comment comment) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(comment);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Comment comment) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(comment);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(Comment comment) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(comment);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Comment> findAll() {
        return (List<Comment>) HibernateSessionFactoryUtil.getSessionFactory().openSession()
                .createQuery("From Comment").list();
    }

    private List<Comment> getComments(Session session, Query<Comment> query) {
        List<Comment> comments;
        try {
            comments = query.getResultList();
        } catch (javax.persistence.NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            session.close();
        }
        return comments;
    }
}
