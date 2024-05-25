package huce.edu.vn.appdocsach.models.comment;

public class WriteCommentModel {
    private String content;

    private Integer chapterId;

    public WriteCommentModel(String content, Integer chapterId) {
        this.content = content;
        this.chapterId = chapterId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }
}
