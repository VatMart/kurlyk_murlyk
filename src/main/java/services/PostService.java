package services;

import dao.PostDAO;
import dao.PostDAOImpl;
import entities.Post;
import entities.response_messages.PostResponse;
import entities.response_messages.PostsResponse;

import java.util.List;

public class PostService {

	private PostDAO postDAO = new PostDAOImpl();

	public Post findById(long id) {
		return postDAO.findById(id);
	}

	public PostResponse findByIdForResponse(long id) {
		return postDAO.findByIdForResponse(id);
	}

	public Post findByTitle(String title) {
		return postDAO.findByTitle(title);
	}

	public PostResponse findByTitleForResponse(String title) {
		return postDAO.findByTitleForResponse(title);
	}

	public List<Post> findByTags(String... tags) {
		return postDAO.findByTags(tags);
	}

	public List<PostResponse> getByOrder(int from, int to, PostsResponse.SortingType order) {
		return postDAO.getByOrder(from, to, order);
	}

	public void save(Post post) {
		postDAO.save(post);
	}

	public void update(Post post) {
		postDAO.update(post);
	}

	public void delete(Post post) {
		postDAO.delete(post);
	}

	public List<Post> findAll() {
		return postDAO.findAll();
	}
}
