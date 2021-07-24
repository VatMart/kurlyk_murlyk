package dao;

import entities.Image;
import entities.Like;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class ImageDAOImpl implements ImageDAO {
    @Override
    public Image findById(long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Image.class, id);
    }

    @Override
    public void save(Image image) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(image);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Image image) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(image);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(Image image) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(image);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Image> findAll() {
        return (List<Image>) HibernateSessionFactoryUtil.getSessionFactory().openSession()
                .createQuery("From Image").list();
    }
}
