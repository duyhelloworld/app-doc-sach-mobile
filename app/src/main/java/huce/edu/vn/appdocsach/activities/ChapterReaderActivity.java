package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;

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
    ToggleButton btnChapterReaderToggleComment;
    Spinner spChapterReaderTitle;
    ChapterService chapterService = ChapterService.chapterService;
    ChapterReaderAdapter chapterReaderAdapter;
    List<OnlyNameChapterModel> chapterModels;
    SpinnerAdapter spinnerAdapter;
    FrameLayout flChapterReaderComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_reader);

        rvChapterReaderImage = findViewById(R.id.rvChapterReaderImage);
        btnChapterReaderPrevChapter = findViewById(R.id.btnChapterReaderPrevChapter);
        btnChapterReaderNextChapter = findViewById(R.id.btnChapterReaderNextChapter);
        spChapterReaderTitle = findViewById(R.id.spChapterReaderTitle);
        flChapterReaderComment = findViewById(R.id.flChapterReaderComment);
        btnChapterReaderToggleComment = findViewById(R.id.btnChapterReaderToggleComment);

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

        Bundle bundle = new Bundle();
        bundle.putInt(BundleKey.CHAPTER_ID, chapterModels.get(position).getId());
        CommentFragment commentFragment = new CommentFragment();
        commentFragment.setArguments(bundle);
        btnChapterReaderToggleComment.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                addCommentView(commentFragment);
            } else {
                removeCommentView(commentFragment);
            }
        });
        renderChapter(position, size);
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

        OnlyNameChapterModel chapterModel = chapterModels.get(position);
        chapterService.read(chapterModel.getId()).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (chapterReaderAdapter == null) {
                    chapterReaderAdapter = new ChapterReaderAdapter(ChapterReaderActivity.this, response.body(), () -> {
                        Toast.makeText(ChapterReaderActivity.this, chapterModel.getTitle(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    chapterReaderAdapter.setData(response.body());
                }
                rvChapterReaderImage.setAdapter(chapterReaderAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable throwable) {
                DialogUtils.unknownError(ChapterReaderActivity.this, "Lỗi load chương truyện : ", throwable);
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

    private void addCommentView(CommentFragment commentFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.flChapterReaderComment, commentFragment)
                .commit();
    }

    private void removeCommentView(CommentFragment commentFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(commentFragment)
                .commit();
    }
}