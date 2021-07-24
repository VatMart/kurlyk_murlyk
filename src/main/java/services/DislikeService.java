package services;

import dao.DislikeDAO;
import dao.DislikeDAOImpl;
import entities.Dislike;
import java.util.List;

public class DislikeService {
    DislikeDAO dislikeDAO = new DislikeDAOImpl();

    public Dislike findById(long id) {
        return dislikeDAO.findById(id);
    }

    public List<Dislike> findByUser(long user_id) {
        return dislikeDAO.findByUser(user_id);
    }

    public List<Dislike> findByPost(long post_id) {
        return dislikeDAO.findByPost(post_id);
    }

    public Dislike findByUserAndPost(long user_id, long post_id) {
        return dislikeDAO.findByUserAndPost(user_id, post_id);
    }

    public void save(Dislike dislike) {
        dislikeDAO.save(dislike);
    }

    public void update(Dislike dislike) {
        dislikeDAO.update(dislike);
    }

    public void delete(Dislike dislike) {
        dislikeDAO.delete(dislike);
    }

    public List<Dislike> findAll() {
        return dislikeDAO.findAll();
    }
}
