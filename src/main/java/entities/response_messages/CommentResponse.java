package entities.response_messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Comment;

public class CommentResponse {

    public CommentResponse() { }

    public CommentResponse(long comment_id, long post_id, long user_id, Long parent_comment_id, String nickname, String text_content, long date_writing) {
        this.comment_id = comment_id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.parent_comment_id = parent_comment_id;
        this.nickname = nickname;
        this.text_content = text_content;
        this.date_writing = date_writing;
    }

    public CommentResponse(Comment comment) {
        this.comment_id = comment.getComment_id();
        this.post_id = comment.getPost().getPost_id();
        this.user_id = comment.getUser().getUser_ID();
        this.parent_comment_id = comment.getParent_comment_id();
        this.nickname = comment.getUser().getNickname();
        this.text_content = comment.getText_content();
        this.date_writing = comment.getDate_writing();
    }

    private long comment_id;
    private long post_id;
    private long user_id;
    private Long parent_comment_id;
    private String nickname;
    private String text_content;

    public long getComment_id() {
        return comment_id;
    }

    public void setComment_id(long comment_id) {
        this.comment_id = comment_id;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public Long getParent_comment_id() {
        return parent_comment_id;
    }

    public void setParent_comment_id(Long parent_comment_id) {
        this.parent_comment_id = parent_comment_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    private long date_writing;

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
