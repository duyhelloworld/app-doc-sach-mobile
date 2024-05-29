package huce.edu.vn.appdocsach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.HistoryAdapter;
import huce.edu.vn.appdocsach.configurations.SearchHistoryManager;
import huce.edu.vn.appdocsach.constants.IntentKey;

public class BookSearchActivity extends AppCompatActivity {

    SearchView svMainBookSearchBox;
    TextView tvEmptySearchHistory;
    RecyclerView rvBookSearchHistory;
    HistoryAdapter historyAdapter;
    Button btnClearHistory;
    SearchHistoryManager historyManager = SearchHistoryManager.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        svMainBookSearchBox = findViewById(R.id.svMainBookSearchBox);
        rvBookSearchHistory = findViewById(R.id.rvBookSearchHistory);
        tvEmptySearchHistory = findViewById(R.id.tvEmptySearchHistory);

        List<String> history = historyManager.getHistory();
        if (history.isEmpty()) {
            tvEmptySearchHistory.setVisibility(View.VISIBLE);
        } else {
            tvEmptySearchHistory.setVisibility(View.GONE);
            historyAdapter = new HistoryAdapter(historyManager.getHistory(), position -> {
                svMainBookSearchBox.setQueryHint(historyAdapter.getHistory(position));
                svMainBookSearchBox.requestFocus();
            });
        }

        svMainBookSearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                historyManager.addSearchTerm(query);
                Intent intent = new Intent(BookSearchActivity.this, MainActivity.class);
                intent.putExtra(IntentKey.KEYWORD, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btnClearHistory.setOnClickListener(v -> {
            historyManager.clearHistory();
            historyAdapter.clear();
            tvEmptySearchHistory.setVisibility(View.VISIBLE);
        });
    }
}
