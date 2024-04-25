package huce.edu.vn.appdocsach.models;

import androidx.annotation.NonNull;

public abstract class BaseModel {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return true nếu chứa nội dung giống nhau, false nếu khác nhau
     * */
    public abstract boolean compareTo(Object o);

    @NonNull
    @Override
    public String toString() {
        return "BaseModel{" +
                "id=" + id +
                '}';
    }
}
