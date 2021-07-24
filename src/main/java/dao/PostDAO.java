package dao;

import entities.Post;
import entities.response_messages.PostResponse;
import entities.response_messages.PostsResponse;

import java.util.List;

public interface PostDAO {
	public Post findById(long id);

	public PostResponse findByIdForResponse(long id);

	public Post findByTitle(String title);

	public PostResponse findByTitleForResponse(String title);

	public List<PostResponse> getByOrder(int from, int to, PostsResponse.SortingType order);

	public List<Post> findByTags(String... tags);

	public void save(Post post);

	public void update(Post post);

	public void delete(Post post);

	public List<Post> findAll();
}
