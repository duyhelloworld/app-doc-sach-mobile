package huce.edu.vn.appdocsach.models.comment;

import java.util.HashMap;
import java.util.Map;

import huce.edu.vn.appdocsach.models.paging.PagingRequest;

public class FindCommentModel extends PagingRequest {
    private Integer chapterId;

    @Override
    public Map<String, Object> getRetrofitQuery() {
        Map<String, Object> queryMap = super.getRetrofitQuery();
        queryMap.put("chapterId", chapterId);
        return queryMap;
    }

    public FindCommentModel(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }
}
