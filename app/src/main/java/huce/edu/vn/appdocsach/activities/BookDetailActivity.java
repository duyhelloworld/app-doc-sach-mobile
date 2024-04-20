package huce.edu.vn.appdocsach.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import huce.edu.vn.appdocsach.R;

public class BookDetailActivity extends AppCompatActivity {

    TextView tvBookDetailTitle, tvBookDetailAuthorV,tvBookDetailDescriptionV;
    RecyclerView rvBookDetailChapterList, rvBookDetailCategories;
    RatingBar rbBookDetailRating;
    ImageView ivBookDetailCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        tvBookDetailAuthorV = findViewById(R.id.tvBookDetailAuthorV);
        tvBookDetailTitle = findViewById(R.id.tvBookDetailTitle);
        tvBookDetailDescriptionV = findViewById(R.id.tvBookDetailDescriptionV);

        rvBookDetailChapterList = findViewById(R.id.rvBookDetailChapterList);
        rvBookDetailCategories = findViewById(R.id.rvBookDetailCategories);

        rbBookDetailRating = findViewById(R.id.rbBookDetailRating);
        ivBookDetailCover = findViewById(R.id.ivBookDetailCover);

        Intent intent = getIntent();
        Integer bookId = intent.getIntExtra("id", 0);

    }
}