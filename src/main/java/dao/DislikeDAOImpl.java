package dao;

import entities.Dislike;
import entities.Like;
import entities.Post;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class DislikeDAOImpl implements DislikeDAO {
    @Override
    public Dislike findById(long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Dislike.class, id);
    }

    @Override
    public List<Dislike> findByUser(long user_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Dislike> query = session.createQuery("from Dislike where user.user_ID = :user_id");
        query.setParameter("user_id", user_id);
        return getDislikes(session, query);
    }

    @Override
    public List<Dislike> findByPost(long post_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Dislike> query = session.createQuery("from Dislike where post.post_id = :post_id");
        query.setParameter("post_id", post_id);
        return getDislikes(session, query);
    }

    @Override
    public Dislike findByUserAndPost(long user_id, long post_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Dislike> query = session.createQuery("from Dislike where post.post_id = :post_id and user.user_ID = :user_id");
        query.setParameter("post_id", post_id);
        query.setParameter("user_id", user_id);
        Dislike dislike;
        try {
            dislike = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            session.close();
        }
        return dislike;
    }

    private List<Dislike> getDislikes(Session session, Query<Dislike> query) {
        List<Dislike> dislikes;
        try {
            dislikes = query.getResultList();
        } catch (javax.persistence.NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            session.close();
        }
        return dislikes;
    }

    @Override
    public void save(Dislike dislike) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(dislike);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Dislike dislike) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(dislike);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(Dislike dislike) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Post post = session.get(Post.class, dislike.getPost().getPost_id());
        session.delete(dislike);
        post.setLike_sum(post.getDislike_sum()-1);
        session.update(post);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Dislike> findAll() {
        return (List<Dislike>) HibernateSessionFactoryUtil.getSessionFactory().openSession()
                .createQuery("From Dislike").list();
    }
}
