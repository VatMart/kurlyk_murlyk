package entities.response_messages;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Post;

@JsonAutoDetect
public class PostResponse {
    public PostResponse() {
    }

    public PostResponse(long post_id, String title, long user_id, String nickname, String text_content, long date_writing, long date_editing, int like_sum, int dislike_sum, int views_number, String[] tags) {
        this.post_id = post_id;
        this.title = title;
        this.user_id = user_id;
        this.nickname = nickname;
        this.text_content = text_content;
        this.date_writing = date_writing;
        this.date_editing = date_editing;
        this.like_sum = like_sum;
        this.dislike_sum = dislike_sum;
        this.views_number = views_number;
        this.tags = tags;
    }

    public PostResponse(Post post) {
        this.post_id = post.getPost_id();
        this.title = post.getTitle();
        this.user_id = post.getUser().getUser_ID();
        this.nickname = post.getUser().getNickname();
        this.text_content = post.getText_content();
        this.date_writing = post.getDate_writing();
        this.date_editing = post.getDate_editing();
        this.like_sum = post.getLike_sum();
        this.dislike_sum = post.getDislike_sum();
        this.views_number = post.getViews_number();
        this.tags = post.getTags();
    }

    private long post_id;
    private String title;
    private long user_id;
    private String nickname;
    private String text_content;
    private long date_writing;
    private long date_editing;
    private int like_sum;
    private int dislike_sum;
    private int views_number;
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

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
        return null;
    }
}
