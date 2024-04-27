package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.ChapterReaderAdapter;
import huce.edu.vn.appdocsach.apiservices.ChapterService;
import huce.edu.vn.appdocsach.constants.BundleKey;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.fragments.CommentFragment;
import huce.edu.vn.appdocsach.models.chapter.OnlyNameChapterModel;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterReaderActivity extends AppCompatActivity {

    RecyclerView rvChapterReaderImage;
    ImageButton btnChapterReaderPrevChapter, btnChapterReaderNextChapter;
    Spinner spChapterReaderTitle;
    ChapterService chapterService = ChapterService.chapterService;
    ChapterReaderAdapter chapterReaderAdapter;
    List<OnlyNameChapterModel> chapterModels;
    SpinnerAdapter spinnerAdapter;
    boolean isLoadedFragment = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_reader);

        rvChapterReaderImage = findViewById(R.id.rvChapterReaderImage);
        btnChapterReaderPrevChapter = findViewById(R.id.btnChapterReaderPrevChapter);
        btnChapterReaderNextChapter = findViewById(R.id.btnChapterReaderNextChapter);
        spChapterReaderTitle = findViewById(R.id.spChapterReaderTitle);

        Intent intent = getIntent();
        int position = intent.getIntExtra(IntentKey.CHAPTER_ID_POSITION, 0);
        chapterModels = intent.getParcelableArrayListExtra(IntentKey.OTHER_CHAPTER);
        assert chapterModels != null;
        int size = chapterModels.size();
        spinnerAdapter = new ArrayAdapter<>(ChapterReaderActivity.this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                chapterModels);
        spChapterReaderTitle.setAdapter(spinnerAdapter);
        spChapterReaderTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                renderChapter(position, size);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        renderChapter(position, size);

        rvChapterReaderImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isLoadedFragment) {
                    CommentFragment commentFragment = new CommentFragment();
                    Bundle b = new Bundle();
                    b.putInt(BundleKey.CHAPTER_ID, chapterModels.get(position).getId());
                    commentFragment.setArguments(b);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flChapterReaderComment, commentFragment)
//                            .add(commentFragment, "CommentFragment")
                            .commit();
                    isLoadedFragment = true;
                }
            }
        });

    }


    private void renderChapter(int position, int size) {
        spChapterReaderTitle.setSelection(position);

        if (position == size-1) {
            disableButton(btnChapterReaderNextChapter);
            enableButton(btnChapterReaderPrevChapter, position - 1, size);
        } else if (position == 0) {
            disableButton(btnChapterReaderPrevChapter);
            enableButton(btnChapterReaderNextChapter, position + 1, size);
        } else {
            enableButton(btnChapterReaderPrevChapter, position - 1, size);
            enableButton(btnChapterReaderNextChapter, position + 1, size);
        }

        spChapterReaderTitle.setSelection(position);

        chapterService.read(chapterModels.get(position).getId()).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (chapterReaderAdapter == null) {
                    chapterReaderAdapter = new ChapterReaderAdapter(ChapterReaderActivity.this, response.body());
                } else {
                    chapterReaderAdapter.setData(response.body());
                }
                rvChapterReaderImage.setAdapter(chapterReaderAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable throwable) {
                DialogUtils.error(ChapterReaderActivity.this, "Lỗi load ảnh : ", throwable);
            }
        });
    }

    private void disableButton(ImageButton btn) {
        btn.setOnClickListener(v -> {});
        btn.setBackgroundColor(getColor(R.color.gray));
        btn.setEnabled(false);
    }

    private void enableButton(ImageButton btn, int pos, int size) {
        btn.setEnabled(true);
        btn.setBackgroundColor(getColor(R.color.green));
        btn.setOnClickListener(v -> {
            renderChapter(pos, size);
        });
    }
}