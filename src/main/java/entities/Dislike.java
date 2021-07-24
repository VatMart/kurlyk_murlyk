package entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;

@JsonAutoDetect
@Entity
@Table(name = "dislikes")
public class Dislike {
    public Dislike() {
    }

    public Dislike(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dislike_id")
    private long dislike_id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @Column(name = "puting_date")
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Generated(GenerationTime.INSERT)
    @Convert(converter = utils.TimestampToLongConverter.class)
    private long puting_date;
    public long getDislike_id() {
        return dislike_id;
    }

    public void setDislike_id(long dislike_id) {
        this.dislike_id = dislike_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public long getPuting_date() {
        return puting_date;
    }

    public void setPuting_date(long puting_date) {
        this.puting_date = puting_date;
    }
    @Override
    public int hashCode() {
        final int prime = 35;
        int result = 2;
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = (int) (prime * result + dislike_id);
        result = prime * result + ((post == null) ? 0 : post.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        Dislike objDislike = (Dislike) obj;
        if (objDislike.getUser().equals(user) && objDislike.getPost().equals(post)
                && (objDislike.getDislike_id()) == (dislike_id)
                && objDislike.getPuting_date() ==(puting_date))
            return true;
        else
            return false;
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
