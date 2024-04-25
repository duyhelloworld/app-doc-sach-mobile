package huce.edu.vn.appdocsach.models.chapter;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import huce.edu.vn.appdocsach.models.BaseModel;

public class OnlyNameChapterModel extends BaseModel implements Parcelable {
    private String title;

    public OnlyNameChapterModel(int id, String title) {
        setId(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean compareTo(Object o) {
        OnlyNameChapterModel c = (OnlyNameChapterModel) o;
        return getId() == c.getId() && c.title.equals(title);
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getTitle());
    }

    public static final Creator<OnlyNameChapterModel> CREATOR = new Creator<OnlyNameChapterModel>() {
        @Override
        public OnlyNameChapterModel createFromParcel(Parcel in) {
            int id = in.readInt();
            String title = in.readString();
            return new OnlyNameChapterModel(id, title);
        }

        @Override
        public OnlyNameChapterModel[] newArray(int size) {
            return new OnlyNameChapterModel[size];
        }
    };
}
