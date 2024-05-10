package huce.edu.vn.appdocsach.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.CommentAdapter;
import huce.edu.vn.appdocsach.apiservices.CommentService;
import huce.edu.vn.appdocsach.callbacks.OnLoadMore;
import huce.edu.vn.appdocsach.constants.BundleKey;
import huce.edu.vn.appdocsach.constants.PaginationConstant;
import huce.edu.vn.appdocsach.models.comment.FindCommentModel;
import huce.edu.vn.appdocsach.models.comment.SimpleCommentModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment implements OnLoadMore {
    CommentService commentService = CommentService.commentService;
    EditText edtCommentReaderContent;
    ImageView ivCommentReaderAvatar;
    RecyclerView rvBookDetailListComment;
    CommentAdapter commentAdapter;
    List<SimpleCommentModel> commentModels;
    FindCommentModel findCommentModel;
    ProgressBar progressBar;
    long totalPage = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        Bundle bundle = getArguments();
        int chapterId = 0;
        if (bundle != null) {
            chapterId = bundle.getInt(BundleKey.CHAPTER_ID);
        }
        findCommentModel = new FindCommentModel(chapterId, 4);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        rvBookDetailListComment = view.findViewById(R.id.rvBookDetailListComment);
        edtCommentReaderContent = view.findViewById(R.id.edtCommentReaderContent);
        ivCommentReaderAvatar = view.findViewById(R.id.ivCommentReaderAvatar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvBookDetailListComment.setLayoutManager(layoutManager);

        // Load first page
        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.VISIBLE);
            if (commentModels == null || rvBookDetailListComment.getAdapter() == null) {
                callApiAndSaveCommentModel();
                commentAdapter = new CommentAdapter(getContext(), commentModels, rvBookDetailListComment, this, position -> {
                    DialogUtils.notifyInfo(getContext(), "Click comment at " + position);
                });
                rvBookDetailListComment.setAdapter(commentAdapter);
            } else {
                commentAdapter.setData(commentModels);
            }
            progressBar.setVisibility(View.GONE);
        }, 2000);
        return view;
    }

    private void callApiAndSaveCommentModel() {
        commentService.getComments(findCommentModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleCommentModel>>() {
            @Override
            public void onResponse(@NonNull Call<PagingResponse<SimpleCommentModel>> call, @NonNull Response<PagingResponse<SimpleCommentModel>> response) {
                PagingResponse<SimpleCommentModel> data = response.body();
                assert data != null;
                commentModels = data.getValues();
                totalPage = data.getTotalPage();
            }

            @Override
            public void onFailure(@NonNull Call<PagingResponse<SimpleCommentModel>> call, @NonNull Throwable throwable) {
                DialogUtils.unknownError(getContext(), "load comment lỗi : ", throwable);
            }
        });
    }

    @Override
    public void loadMore() {
        // Chưa load hết dữ liệu
        if (findCommentModel.getPageNumber() <= totalPage) {
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                findCommentModel.incrementPageNumber();
                callApiAndSaveCommentModel();
                commentAdapter.append(commentModels);
                commentAdapter.setLoaded();
                progressBar.setVisibility(View.GONE);
            }, 2000);
        } else {
            Toast.makeText(getContext(), "Đã load tới comment cuối cùng", Toast.LENGTH_SHORT).show();
        }
    }
}