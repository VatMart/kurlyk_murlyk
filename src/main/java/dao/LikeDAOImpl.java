package dao;

import entities.Comment;
import entities.Like;
import entities.Post;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class LikeDAOImpl implements LikeDAO {
    @Override
    public Like findById(long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Like.class, id);
    }

    @Override
    public List<Like> findByUser(long user_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Like> query = session.createQuery("from Like where user.user_ID = :user_id");
        query.setParameter("user_id", user_id);
        return getLikes(session, query);
    }

    @Override
    public List<Like> findByPost(long post_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Like> query = session.createQuery("from Like where post.post_id = :post_id");
        query.setParameter("post_id", post_id);
        return getLikes(session, query);
    }

    @Override
    public Like findByUserAndPost(long user_id, long post_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Like> query = session.createQuery("from Like where post.post_id = :post_id and user.user_ID = :user_id");
        query.setParameter("post_id", post_id);
        query.setParameter("user_id", user_id);
        Like like;
        try {
            like = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            session.close();
        }
        return like;
    }

    private List<Like> getLikes(Session session, Query<Like> query) {
        List<Like> likes;
        try {
            likes = query.getResultList();
        } catch (javax.persistence.NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            session.close();
        }
        return likes;
    }

    @Override
    public void save(Like like) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(like);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Like like) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(like);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(Like like) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Post post = session.get(Post.class, like.getPost().getPost_id());
        session.delete(like);
        post.setLike_sum(post.getLike_sum()-1);
        session.update(post);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Like> findAll() {
        return (List<Like>) HibernateSessionFactoryUtil.getSessionFactory().openSession()
                .createQuery("From Like").list();
    }
}
