package dao;


import entities.Comment;
import entities.Like;

import java.util.List;

public interface LikeDAO {

    public Like findById(long id);

    public List<Like> findByUser(long user_id);

    public List<Like> findByPost(long post_id);

    public Like findByUserAndPost(long user_id, long post_id);

    public void save(Like like);

    public void update(Like like);

    public void delete(Like like);

    public List<Like> findAll();
}
