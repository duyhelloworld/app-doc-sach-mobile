package huce.edu.vn.appdocsach.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.CommentAdapter;
import huce.edu.vn.appdocsach.apiservices.CommentService;
import huce.edu.vn.appdocsach.constants.BundleKey;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.models.comment.FindCommentModel;
import huce.edu.vn.appdocsach.models.comment.SimpleCommentModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment {
    CommentService commentService = CommentService.commentService;
    RecyclerView rvBookDetailListComment;
    CommentAdapter commentAdapter;
    FindCommentModel findCommentModel;
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Integer chapterId = bundle.getInt(BundleKey.CHAPTER_ID);
        rvBookDetailListComment = view.findViewById(R.id.rvBookDetailListComment);
        findCommentModel = new FindCommentModel(chapterId);
        commentService.getComments(findCommentModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleCommentModel>>() {
            @Override
            public void onResponse(@NonNull Call<PagingResponse<SimpleCommentModel>> call, @NonNull Response<PagingResponse<SimpleCommentModel>> response) {
                assert response.body() != null;
                commentAdapter = new CommentAdapter(getContext(), response.body().getValues(), position -> {
                    DialogUtils.info(getContext(), "Click comment at " + position);
                });
                rvBookDetailListComment.setAdapter(commentAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<PagingResponse<SimpleCommentModel>> call, @NonNull Throwable throwable) {
                DialogUtils.error(getContext(), "load comment lỗi : ", throwable);
            }
        });
    }
}