package huce.edu.vn.appdocsach.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.adapters.CommentAdapter;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.apiservices.CommentService;
import huce.edu.vn.appdocsach.callbacks.CallBack;
import huce.edu.vn.appdocsach.callbacks.OnLoadMore;
import huce.edu.vn.appdocsach.callbacks.OnTouchViewItem;
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

public class CommentFragment extends Fragment implements OnLoadMore, OnTouchViewItem {
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
    ImageLoader imageLoader = ImageLoader.getInstance();
    InputMethodManager inputMethodManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        context = getContext();
        pbComment = view.findViewById(R.id.pbComment);
        rvBookDetailListComment = view.findViewById(R.id.rvBookDetailListComment);
        edtCommentReaderContent = view.findViewById(R.id.edtCommentReaderContent);
        ivCommentReaderAvatar = view.findViewById(R.id.ivCommentReaderAvatar);

        assert getActivity() != null;
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthInfoModel> call, @NonNull Response<AuthInfoModel> response) {
                AuthInfoModel model = response.body();
                if (!response.isSuccessful() || model == null) {
                    ivCommentReaderAvatar.setImageResource(R.drawable.default_avatar);
                    return;
                }
                ivCommentReaderAvatar.setContentDescription(model.getFullname());
                imageLoader.showWithoutCache(model.getAvatar(), ivCommentReaderAvatar);
            }

            @Override
            public void onFailure(@NonNull Call<AuthInfoModel> call, @NonNull Throwable throwable) {
                DialogUtils.errorUserSee(context, R.string.error_load_user_info);
                AppLogger.getInstance().error(throwable);
            }
        });

        new Handler().postDelayed(() -> {
            // Load first page comments
            Bundle bundle = getArguments();
            assert bundle != null;
            findCommentModel = new FindCommentModel(bundle.getInt(IntentKey.CHAPTER_ID, 0), 4);
            pbComment.setVisibility(View.VISIBLE);
            fetchComment(() -> {
                commentAdapter = new CommentAdapter(commentModels, rvBookDetailListComment, this, this);
                rvBookDetailListComment.setAdapter(commentAdapter);
                pbComment.setVisibility(View.GONE);
            });
        }, 500);


        edtCommentReaderContent.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                makeWriteCommentRequest(v);
                return true;
            }
            return false;
        });

        return view;
    }

    private void makeWriteCommentRequest(View v) {
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
                    Toast.makeText(context, getString(R.string.comment_add_success_message), Toast.LENGTH_SHORT).show();
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
    }

    private void fetchComment(CallBack callBack) {
        commentService.getComments(findCommentModel.getRetrofitQuery()).enqueue(new Callback<PagingResponse<SimpleCommentModel>>() {
            @Override
            public void onResponse(@NonNull Call<PagingResponse<SimpleCommentModel>> call, @NonNull Response<PagingResponse<SimpleCommentModel>> response) {
                PagingResponse<SimpleCommentModel> data = response.body();
                assert data != null;
                commentModels = data.getValues();
                totalPage = data.getTotalPage();
                callBack.callBack();
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
            new Handler().postDelayed(() -> {
                pbComment.setVisibility(View.VISIBLE);
                findCommentModel.incrementPageNumber();
                fetchComment(() -> {
                    commentAdapter.append(commentModels);
                    commentAdapter.setLoaded();
                    pbComment.setVisibility(View.GONE);
                });
            }, 1000);
        }
    }

    @Override
    public void onTouch(int position, View v) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_comment, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.mnFragCommentDelete) {
                Integer commentId = commentAdapter.getCommentId(position);
                commentService.deleteComment(commentId).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            commentModels.remove(position);
                            commentAdapter.remove(position);
                            Toast.makeText(context, R.string.comment_delete_success_message, Toast.LENGTH_SHORT).show();
                        } else {
                            DialogUtils.errorUserSee(context, HttpErrorSerializer.extractErrorMessage(response.errorBody()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                        appLogger.error(throwable);
                    }
                });
                return true;
            }

            DialogUtils.infoUserSee(context, R.string.feature_not_supported);
            return true;
        });
    }
}