package entities;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    public Comment() {
    }

    public Comment(Post post, User user, Long parent_comment_id, String text_content) {
        this.post = post;
        this.user = user;
        this.parent_comment_id = parent_comment_id;
        this.text_content = text_content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long comment_id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "parent_comment_id")
    private Long parent_comment_id;
    @Column(name = "text_content")
    private String text_content;
    @Column(name = "date_writing")
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Generated(GenerationTime.INSERT)
    @Convert(converter = utils.TimestampToLongConverter.class)
    private long date_writing;

    public long getComment_id() {
        return comment_id;
    }

    public void setComment_id(long comment_id) {
        this.comment_id = comment_id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getParent_comment_id() {
        return parent_comment_id;
    }

    public void setParent_comment_id(Long parent_comment_id) {
        this.parent_comment_id = parent_comment_id;
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

}
