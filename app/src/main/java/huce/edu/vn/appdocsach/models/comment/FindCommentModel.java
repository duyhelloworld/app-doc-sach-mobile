package huce.edu.vn.appdocsach.models.comment;

import java.util.Map;

import huce.edu.vn.appdocsach.models.paging.RetrofitRequest;

public class FindCommentModel extends RetrofitRequest {
    private Integer chapterId;
    @Override
    public Map<String, Object> getRetrofitQuery() {
        Map<String, Object> queryMap = super.getRetrofitQuery();
        queryMap.put("chapterId", chapterId);
        return queryMap;
    }

    public FindCommentModel(Integer chapterId, Integer pageSize) {
        super(pageSize);
        this.chapterId = chapterId;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }
}
