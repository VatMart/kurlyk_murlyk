package dao;

import entities.Image;

import java.util.List;

public interface ImageDAO {
    public Image findById(long id);

    public void save(Image image);

    public void update(Image image);

    public void delete(Image image);

    public List<Image> findAll();
}
