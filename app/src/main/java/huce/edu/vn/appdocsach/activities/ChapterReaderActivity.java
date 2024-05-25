package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.models.chapter.OnlyNameChapterModel;
import huce.edu.vn.appdocsach.utils.AppLogger;
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
    AppLogger appLogger = AppLogger.getInstance();
    int chapterModelSize;

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
        chapterModelSize = chapterModels.size();
        spinnerAdapter = new ArrayAdapter<>(ChapterReaderActivity.this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                chapterModels);
        spChapterReaderTitle.setAdapter(spinnerAdapter);

        spChapterReaderTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                renderChapter(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.CHAPTER_ID, chapterModels.get(position).getId());
        CommentFragment commentFragment = new CommentFragment();
        commentFragment.setArguments(bundle);
        btnChapterReaderToggleComment.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FragmentManager manager = getSupportFragmentManager();
            if (isChecked) {
                manager.beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.flChapterReaderComment, commentFragment)
                        .commit();
                btnChapterReaderPrevChapter.setEnabled(false);
                btnChapterReaderNextChapter.setEnabled(false);
                rvChapterReaderImage.setEnabled(false);
                spChapterReaderTitle.setEnabled(false);
                getWindow().setBackgroundDrawableResource(R.color.transparent);
            } else {
                manager.beginTransaction()
                        .remove(commentFragment)
                        .commit();
                btnChapterReaderPrevChapter.setEnabled(true);
                btnChapterReaderNextChapter.setEnabled(true);
                rvChapterReaderImage.setEnabled(true);
                spChapterReaderTitle.setEnabled(true);
                getWindow().setBackgroundDrawable(null);
            }
        });
        renderChapter(position);
    }


    private void renderChapter(int position) {
        spChapterReaderTitle.setSelection(position);

        if (position == chapterModelSize - 1) {
            disableButton(btnChapterReaderNextChapter);
            enableButton(btnChapterReaderPrevChapter, position - 1);
        } else if (position == 0) {
            disableButton(btnChapterReaderPrevChapter);
            enableButton(btnChapterReaderNextChapter, position + 1);
        } else {
            enableButton(btnChapterReaderPrevChapter, position - 1);
            enableButton(btnChapterReaderNextChapter, position + 1);
        }

        spChapterReaderTitle.setSelection(position);

        OnlyNameChapterModel chapterModel = chapterModels.get(position);
        chapterService.read(chapterModel.getId()).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (chapterReaderAdapter == null) {
                    chapterReaderAdapter = new ChapterReaderAdapter(response.body(), () ->
                            Toast.makeText(ChapterReaderActivity.this, chapterModel.getTitle(), Toast.LENGTH_SHORT).show());
                } else {
                    chapterReaderAdapter.setData(response.body());
                }
                rvChapterReaderImage.setAdapter(chapterReaderAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable throwable) {
                DialogUtils.errorUserSee(ChapterReaderActivity.this, R.string.error_load_category);
                appLogger.error(throwable);
            }
        });
    }

    private void disableButton(ImageButton btn) {
        btn.setOnClickListener(v -> {});
        btn.setBackgroundColor(getColor(R.color.gray));
        btn.setEnabled(false);
    }

    private void enableButton(ImageButton btn, int pos) {
        btn.setEnabled(true);
        btn.setBackgroundColor(getColor(R.color.green));
        btn.setOnClickListener(v ->
                renderChapter(pos));
    }
}