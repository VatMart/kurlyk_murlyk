package entities.response_messages;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
@JsonAutoDetect
public class PostsResponse {
    public static enum SortingType {
        date_asc,
        date_desc,
        views,
        likes,
        dislikes
    }

    public PostsResponse() {
    }

    public PostsResponse(List<PostResponse> posts, SortingType sortingType, int from, int to) {
        this.posts = posts;
        this.sortingType = sortingType;
        this.from = from;
        this.to = to;
    }

    private List<PostResponse> posts;
    private SortingType sortingType;
    private int from;
    private int to;

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }

    public SortingType getSortingType() {
        return sortingType;
    }

    public void setSortingType(SortingType sortingType) {
        this.sortingType = sortingType;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
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
