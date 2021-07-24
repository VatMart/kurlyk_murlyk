package entities.received_messages;

public class CommentReceived {

    public CommentReceived() {}
    private Long parent_comment_id;
    private String text_content;

    public Long getParent_comment_id() {
        return parent_comment_id;
    }


    public String getText_content() {
        return text_content;
    }
}
