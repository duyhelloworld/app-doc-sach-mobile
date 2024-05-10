package huce.edu.vn.appdocsach.models.paging;

import androidx.annotation.NonNull;

import java.util.List;

import huce.edu.vn.appdocsach.utils.serializers.GsonCustom;

public class PagingResponse<T> {
    private int totalPage;

    private List<T> values;

    public PagingResponse(int totalPage, List<T> values) {
        this.totalPage = totalPage;
        this.values = values;
    }

    public PagingResponse() {
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    @NonNull
    @Override
    public String toString() {
        return "PagingResponse {" +
                "totalPage=" + totalPage +
                ", values=" + GsonCustom.getInstance().toJson(values) +
                '}';
    }
}
