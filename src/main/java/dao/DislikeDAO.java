package dao;

import entities.Dislike;
import entities.Like;

import java.util.List;

public interface DislikeDAO {

    public Dislike findById(long id);

    public List<Dislike> findByUser(long user_id);

    public List<Dislike> findByPost(long post_id);

    public Dislike findByUserAndPost(long user_id, long post_id);

    public void save(Dislike like);

    public void update(Dislike like);

    public void delete(Dislike like);

    public List<Dislike> findAll();
}
