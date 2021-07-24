package services;

import dao.ImageDAO;
import dao.ImageDAOImpl;
import entities.Image;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class ImageService {
    ImageDAO imageDAO = new ImageDAOImpl();
    public Image findById(long id) {
        return imageDAO.findById(id);
    }

    public void save(Image image) {
        imageDAO.save(image);
    }

    public void update(Image image) {
        imageDAO.update(image);
    }

    public void delete(Image image) {
        imageDAO.delete(image);
    }

    public List<Image> findAll() {
        return imageDAO.findAll();
    }
}
