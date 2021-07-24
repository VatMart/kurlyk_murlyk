package entities.response_messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CommentsResponse {
    public static enum SortingType {
        date_asc,
        date_desc
    }

    public CommentsResponse() {}

    public CommentsResponse(List<CommentResponse> comments, CommentsResponse.SortingType sortingType, int from, int to) {
        this.comments = comments;
        this.sortingType = sortingType;
        this.from = from;
        this.to = to;
    }

    private List<CommentResponse> comments;
    private CommentsResponse.SortingType sortingType;
    private int from;
    private int to;

    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
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
