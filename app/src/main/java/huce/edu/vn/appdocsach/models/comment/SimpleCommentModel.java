package huce.edu.vn.appdocsach.models.comment;

import com.google.gson.annotations.JsonAdapter;

import java.time.LocalDateTime;

import huce.edu.vn.appdocsach.models.BaseModel;
import huce.edu.vn.appdocsach.utils.serializers.LocalDateTimeAdapter;

public class SimpleCommentModel extends BaseModel {
    private String username;

    private String content;

    private String userAvatar;

    @JsonAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime commentAt;

    private Boolean isEdited;

    public SimpleCommentModel(int id, String username, String content, String userAvatar, LocalDateTime commentAt, Boolean isEdited) {
        setId(id);
        this.username = username;
        this.content = content;
        this.userAvatar = userAvatar;
        this.commentAt = commentAt;
        this.isEdited = isEdited;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public LocalDateTime getCommentAt() {
        return commentAt;
    }

    public void setCommentAt(LocalDateTime commentAt) {
        this.commentAt = commentAt;
    }

    public Boolean getEdited() {
        return isEdited;
    }

    public void setEdited(Boolean edited) {
        isEdited = edited;
    }

    @Override
    public boolean compareTo(Object o) {
        SimpleCommentModel c = (SimpleCommentModel) o;
        return c.getId() == getId() && c.username.equals(username) && c.commentAt.equals(commentAt);
    }
}
