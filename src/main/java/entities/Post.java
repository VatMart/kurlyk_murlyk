package entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

@JsonAutoDetect
@Entity
@Table(name = "posts")
@TypeDefs({ @TypeDef(name = "string-array", typeClass = StringArrayType.class) })
public class Post {

	public Post() {
	}

	public Post(String title, User user, String text_content, String[] tags) {
		this.title = title;
		this.user = user;
		this.text_content = text_content;
		this.tags = tags;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private long post_id;
	@Column(name = "title", unique = true)
	private String title;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@Column(name = "text_content")
	private String text_content;
	@Column(name = "date_writing")
	@ColumnDefault(value = "CURRENT_TIMESTAMP")
	@Generated(GenerationTime.INSERT)
	@Convert(converter = utils.TimestampToLongConverter.class)
	private long date_writing;
	@Column(name = "date_editing")
	@Convert(converter = utils.TimestampToLongConverter.class)
	private long date_editing;
	@Column(name = "like_sum")
	private int like_sum;
	@Column(name = "dislike_sum")
	private int dislike_sum;
	@Column(name = "views_number")
	private int views_number;
	@Type(type = "string-array")
	@Column(name = "tags", columnDefinition = "character varying[]")
	private String[] tags;

	public long getPost_id() {
		return post_id;
	}

	public void setPost_id(long post_id) {
		this.post_id = post_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getText_content() {
		return text_content;
	}

	public void setText_content(String text_content) {
		this.text_content = text_content;
	}

	public long getDate_writing() {
		return date_writing;
	}

	public void setDate_writing(long date_writing) {
		this.date_writing = date_writing;
	}

	public long getDate_editing() {
		return date_editing;
	}

	public void setDate_editing(long date_editing) {
		this.date_editing = date_editing;
	}

	public int getLike_sum() {
		return like_sum;
	}

	public void setLike_sum(int like_sum) {
		this.like_sum = like_sum;
	}

	public int getDislike_sum() {
		return dislike_sum;
	}

	public void setDislike_sum(int dislike_sum) {
		this.dislike_sum = dislike_sum;
	}

	public int getViews_number() {
		return views_number;
	}

	public void setViews_number(int views_number) {
		this.views_number = views_number;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "{\"user_ID\":\"" + user_ID + "\", \"login\":\""+login+"\",
		// \"password\":\""+password+"\", \"nickname\":\"" + nickname + "\",
		// \"registrationDate\":\"" + registrationDate + "\",
		// \"user_role\":\""+user_role+"\"}";
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 30;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = (int) (prime * result + post_id);
		result = prime * result + ((text_content == null) ? 0 : text_content.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		Post objPost = (Post) obj;
		if (objPost.getTitle().equals(title) && objPost.getUser().equals(user)
				&& Long.valueOf(objPost.getPost_id()).equals(Long.valueOf(post_id))
				&& objPost.getText_content().equals(text_content))
			return true;
		else
			return false;
	}

}
