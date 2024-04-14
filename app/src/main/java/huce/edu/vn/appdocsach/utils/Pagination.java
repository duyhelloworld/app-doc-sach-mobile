package huce.edu.vn.appdocsach.utils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import huce.edu.vn.appdocsach.utils.serializers.GsonCustom;

public class Pagination<T> {
    private int totalPage = 0;

    private List<T> values;

    public Pagination(int totalPage, List<T> values) {
        this.totalPage = totalPage;
        this.values = values;
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
        return "Pagination{" +
                "totalPage=" + totalPage +
                ", values=" + GsonCustom.getInstance().toJson(values) +
                '}';
    }
}
