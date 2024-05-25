package huce.edu.vn.appdocsach.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.CommentAdapter;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.apiservices.CommentService;
import huce.edu.vn.appdocsach.callbacks.OnLoadMore;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import huce.edu.vn.appdocsach.models.comment.FindCommentModel;
import huce.edu.vn.appdocsach.models.comment.SimpleCommentModel;
import huce.edu.vn.appdocsach.models.comment.WriteCommentModel;
import huce.edu.vn.appdocsach.models.paging.PagingResponse;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import huce.edu.vn.appdocsach.utils.StringUtils;
import huce.edu.vn.appdocsach.utils.serializers.HttpErrorSerializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment implements OnLoadMore {
    CommentService commentService = CommentService.commentService;
    AuthService authService = AuthService.authService;
    List<SimpleCommentModel> commentModels = new ArrayList<>();
    AppLogger appLogger = AppLogger.getInstance();
    FindCommentModel findCommentModel;
    CommentAdapter commentAdapter;

    EditText edtCommentReaderContent;
    ImageView ivCommentReaderAvatar;
    ProgressBar pbComment;
    RecyclerView rvBookDetailListComment;
    long totalPage = 0;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        context = getContext();
        pbComment = view.findViewById(R.id.pbComment);
        rvBookDetailListComment = view.findViewById(R.id.rvBookDetailListComment);
        edtCommentReaderContent = view.findViewById(R.id.edtCommentReaderContent);
        ivCommentReaderAvatar = view.findViewById(R.id.ivCommentReaderAvatar);

        Bundle bundle = getArguments();
        assert bundle != null;
        findCommentModel = new FindCommentModel(bundle.getInt(IntentKey.CHAPTER_ID, 0), 4);

        authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthInfoModel> call, @NonNull Response<AuthInfoModel> response) {
                AuthInfoModel model = response.body();
                if (!response.isSuccessful() || model == null) {
                    ivCommentReaderAvatar.setImageResource(R.drawable.default_avatar);
                    return;
                }
                ivCommentReaderAvatar.setContentDescription(model.getFullname());
                ImageLoader.getInstance().renderWithCache(model.getAvatar(), ivCommentReaderAvatar);
            }

            @Override
            public void onFailure(@NonNull Call<AuthInfoModel> call, @NonNull Throwable throwable) {
                DialogUtils.errorUserSee(context, R.string.error_load_user_info);
                AppLogger.getInstance().error(throwable);
            }
        });
        callApiAndSaveCommentModel();
        new Handler().postDelayed(() -> {
            // Load first page comments
            pbComment.setVisibility(View.VISIBLE);
            assert commentModels != null;
            commentAdapter = new CommentAdapter(commentModels, rvBookDetailListComment, this, position -> {
            });
            rvBookDetailListComment.setAdapter(commentAdapter);
            pbComment.setVisibility(View.GONE);
        }, 500);

        assert getActivity() != null;
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        edtCommentReaderContent.setOnClickListener(v -> {
            Editable commentContent = edtCommentReaderContent.getText();
            if (StringUtils.isNullOrEmpty(commentContent)) {
                DialogUtils.infoUserSee(context, R.string.missing_comment_content);
                return;
            }
            WriteCommentModel writeCommentModel = new WriteCommentModel(commentContent.toString(), findCommentModel.getChapterId());
            commentService.writeComment(writeCommentModel).enqueue(new Callback<SimpleCommentModel>() {
                @Override
                public void onResponse(@NonNull Call<SimpleCommentModel> call, @NonNull Response<SimpleCommentModel> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        Toast.makeText(context, getString(R.string.comment_success_message), Toast.LENGTH_SHORT).show();
                        commentAdapter.append(response.body());
                        edtCommentReaderContent.setText(null);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        edtCommentReaderContent.clearFocus();
                        return;
                    }
                    DialogUtils.errorUserSee(context, HttpErrorSerializer.extractErrorMessage(response.errorBody()));
                }

                @Override
                public void onFailure(@NonNull Call<SimpleCommentModel> call, @NonNull Throwable throwable) {
                    DialogUtils.errorUserSee(context, R.string.error_write_comment);
                    appLogger.error(throwable);
                }
            });
        });
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
                DialogUtils.errorUserSee(context, R.string.error_load_comment);
                appLogger.error(throwable);
            }
        });
    }

    @Override
    public void loadMore() {
        // Chưa load hết dữ liệu
        if (findCommentModel.getPageNumber() <= totalPage) {
            pbComment.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                findCommentModel.incrementPageNumber();
                callApiAndSaveCommentModel();
                commentAdapter.add(commentModels);
                commentAdapter.setLoaded();
                pbComment.setVisibility(View.GONE);
            }, 2000);
        } else {
            Toast.makeText(context, getString(R.string.loaded_final_book), Toast.LENGTH_SHORT).show();
        }
    }
}