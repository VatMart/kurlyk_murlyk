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
@Table(name = "likes")
public class Like {
    public Like() {
    }

    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private long like_id;
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


    public long getLike_id() {
        return like_id;
    }

    public void setLike_id(long like_id) {
        this.like_id = like_id;
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
        result = (int) (prime * result + like_id);
        result = prime * result + ((post == null) ? 0 : post.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        Like objLike = (Like) obj;
        if (objLike.getUser().equals(user) && objLike.getPost().equals(post)
                && (objLike.getLike_id()) == (like_id)
                && objLike.getPuting_date() ==(puting_date))
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
        // return "{\"user_ID\":\"" + user_ID + "\", \"login\":\""+login+"\",
        // \"password\":\""+password+"\", \"nickname\":\"" + nickname + "\",
        // \"registrationDate\":\"" + registrationDate + "\",
        // \"user_role\":\""+user_role+"\"}";
        return null;
    }
}
