package services;

import dao.LikeDAO;
import dao.LikeDAOImpl;
import entities.Like;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class LikeService {
    LikeDAO likeDAO = new LikeDAOImpl();

    public Like findById(long id) {
        return likeDAO.findById(id);
    }

    public List<Like> findByUser(long user_id) {
        return likeDAO.findByUser(user_id);
    }

    public List<Like> findByPost(long post_id) {
        return likeDAO.findByPost(post_id);
    }

    public Like findByUserAndPost(long user_id, long post_id) {
        return likeDAO.findByUserAndPost(user_id, post_id);
    }

    public void save(Like like) {
        likeDAO.save(like);
    }

    public void update(Like like) {
        likeDAO.update(like);
    }

    public void delete(Like like) {
        likeDAO.delete(like);
    }

    public List<Like> findAll() {
        return likeDAO.findAll();
    }
}
