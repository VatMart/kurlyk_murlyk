package dao;

import entities.Post;
import entities.response_messages.PostResponse;
import entities.response_messages.PostsResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactoryUtil;

import java.util.List;
import java.util.stream.Collectors;

public class PostDAOImpl implements PostDAO {

	@Override
	public Post findById(long id) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Post post = session.get(Post.class, id);
		session.close();
		return post;
	}

	@Override
	public PostResponse findByIdForResponse(long id) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Post post = session.get(Post.class, id);
		if (post == null) {
			session.close();
			return null;
		}
		post.setViews_number(post.getViews_number()+1);
		Transaction tx1 = session.beginTransaction();
		session.update(post);
		tx1.commit();
		PostResponse postResponse = new PostResponse(post);
		session.close();
		return postResponse;
	}

	@Override
	public Post findByTitle(String title) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Query<Post> query = session.createQuery("from Post where title = :title");
		query.setParameter("title", title);
		Post post;
		try {
			post = query.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			session.close();
		}
		return post;
	}

	@Override
	public PostResponse findByTitleForResponse(String title) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Query<Post> query = session.createQuery("from Post where title = :title");
		query.setParameter("title", title);
		Post post;
		try {
			post = query.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			System.out.println(e.getMessage());
			return null;
		}
		post.setViews_number(post.getViews_number()+1);
		Transaction tx1 = session.beginTransaction();
		session.update(post);
		tx1.commit();
		PostResponse postResponse = new PostResponse(post);
		session.close();
		return postResponse;
	}

	@Override
	public List<PostResponse> getByOrder(int from, int to, PostsResponse.SortingType order) {
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
			case likes: orderHQL = "order by like_sum desc, date_writing desc"; break;
			case dislikes: orderHQL = "order by dislike_sum desc, date_writing desc"; break;
			case views: orderHQL = "order by views_number desc, date_writing desc"; break;
		}
		Query<Post> query = session.createQuery("from Post "+orderHQL);
		//System.out.println("from"+(from));
		//System.out.println("to"+(to));
		query.setFirstResult(from);
		query.setMaxResults(to);
		List<Post> posts;

		try {
			posts = query.getResultList();
		} catch (javax.persistence.NoResultException e) {
			System.out.println(e.getMessage());
			session.close();
			return null;
		}
		List<PostResponse> postsResponses = posts.stream().map((post) -> new PostResponse(post)).collect(Collectors.toList());
		session.close();
		return postsResponses;
	}

	private List<Post> getPosts(Session session, Query<Post> query) {
		List<Post> posts;
		try {
			posts = query.getResultList();
		} catch (javax.persistence.NoResultException e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			session.close();
		}
		return posts;
	}

	@Override
	public List<Post> findByTags(String... tags) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Query<Post> query = session.createQuery("from Post where tags = :tags");
		query.setParameter("tags", tags);
		return getPosts(session, query);
	}

	@Override
	public void save(Post post) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Transaction tx1 = session.beginTransaction();
		session.save(post);
		tx1.commit();
		session.close();
	}

	@Override
	public void update(Post post) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Transaction tx1 = session.beginTransaction();
		session.update(post);
		tx1.commit();
		session.close();
	}

	@Override
	public void delete(Post post) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Transaction tx1 = session.beginTransaction();
		session.delete(post);
		tx1.commit();
		session.close();
	}

	@Override
	public List<Post> findAll() {
		List<Post> posts = (List<Post>) HibernateSessionFactoryUtil.getSessionFactory().openSession()
				.createQuery("From Post").list();
		return posts;
	}

}
