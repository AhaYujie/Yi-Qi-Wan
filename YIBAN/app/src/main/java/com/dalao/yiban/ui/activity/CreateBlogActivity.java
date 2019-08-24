package com.dalao.yiban.ui.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.CommunityConstant;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.CreateBlogGson;
import com.dalao.yiban.ui.custom.CustomProgressDialog;
import com.dalao.yiban.ui.custom.GlideSimpleLoader;
import com.dalao.yiban.util.CommonUtil;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.ImageUtils;
import com.dalao.yiban.util.JsonUtil;
import com.dalao.yiban.util.SDCardUtil;
import com.dalao.yiban.util.StringUtils;
import com.github.ielse.imagewatcher.ImageWatcherHelper;
import com.sendtion.xrichtext.RichTextEditor;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateBlogActivity extends BaseActivity {

    private String userId;

    private Button createBlogCancelButton;

    private TextView createBlogPublish;

    private EditText createBlogContentTitleEditText;

    private RichTextEditor createBlogContentEditText;

    private Button createBlogBottomNavPicButton;

    private String createBlogContentString;

    private CustomProgressDialog loadingProgressDialog;

    private CustomProgressDialog uploadingProgressDialog;

    private ImageWatcherHelper imageWatcherHelper;

    private int REQUEST_CODE_CHOOSE_IMAGE = 40;

    private final int REQUEST_PERMISSION_CODE = 41;

    private List<File> imageList;   // 博客内容中的图片文件

    public static int MAX_NUMBER_Of_IMAGES = 10; // 博客内容最大博客数;

    /**
     * 启动创建博客活动
     * @param context :
     * @param userId : 用户id
     */
    public static void actionStart(Context context, String userId) {
        Intent intent = new Intent(context, CreateBlogActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        ((MainActivity) context).startActivityForResult(intent, HomeConstant.CREATE_BLOG_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);

        // 初始化控件
        createBlogCancelButton = (Button) findViewById(R.id.create_blog_cancel_button);
        createBlogPublish = (TextView) findViewById(R.id.create_blog_publish);
        createBlogContentTitleEditText = (EditText) findViewById(R.id.create_blog_content_title_edit_text);
        createBlogContentEditText = (RichTextEditor) findViewById(R.id.create_blog_content_edit_text);
        createBlogBottomNavPicButton = (Button) findViewById(R.id.create_blog_bottom_nav_pic_button);
        loadingProgressDialog = new CustomProgressDialog(this, HintConstant.LOADING);
        imageWatcherHelper = ImageWatcherHelper.with(this, new GlideSimpleLoader());
        uploadingProgressDialog = new CustomProgressDialog(this, HintConstant.UPLOADING);

        // 设置点击事件
        createBlogCancelButton.setOnClickListener(this);
        createBlogPublish.setOnClickListener(this);
        createBlogBottomNavPicButton.setOnClickListener(this);

        // 从上个活动获取数据
        Intent intent = getIntent();
        userId = intent.getStringExtra(HomeConstant.USER_ID);

        // 设置图片删除事件
        createBlogContentEditText.setOnRtImageDeleteListener(new RichTextEditor.OnRtImageDeleteListener() {
            @Override
            public void onRtImageDelete(String imagePath) {
                if (!TextUtils.isEmpty(imagePath)) {
                    boolean isOK = SDCardUtil.deleteFile(imagePath);
                    if (isOK) {
                        Toast.makeText(CreateBlogActivity.this, HintConstant.DELETE_IMAGE_SUCCESS,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 图片点击事件
        createBlogContentEditText.setOnRtImageClickListener(new RichTextEditor.OnRtImageClickListener() {
            @Override
            public void onRtImageClick(View view, String imagePath) {
                try {
                    String myContent = getEditData();
                    if (!TextUtils.isEmpty(myContent)){
                        List<String> imageList = StringUtils.getTextFromHtml(myContent, true);
                        if (!TextUtils.isEmpty(imagePath)) {
                            int currentPosition = imageList.indexOf(imagePath);

                            List<Uri> dataList = new ArrayList<>();
                            for (int i = 0; i < imageList.size(); i++) {
                                dataList.add(ImageUtils.getUriFromPath(imageList.get(i)));
                            }
                            imageWatcherHelper.show(dataList, currentPosition);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 取消创建博客, 返回到社区界面
            case R.id.create_blog_cancel_button:
                finish();
                break;

            // 发表博客, 启动博客内容活动
            case R.id.create_blog_publish:
                createBlogContentString = getEditData();
                if(checkBlog()) {
                    uploadingProgressDialog.showProgressDialog();
                    postBlogDataToServer();
                }
                break;

            // 选择图片
            case R.id.create_blog_bottom_nav_pic_button:
                // 未授权
                if (ContextCompat.checkSelfPermission(CreateBlogActivity.this, Manifest.permission.
                        WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(CreateBlogActivity.this, Manifest.permission.
                            CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateBlogActivity.this, new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE);
                    break;
                }
                closeSoftKeyInput();//关闭软键盘
                callGallery();
                break;

            default:
                break;
        }
    }

    /**
     * 发送博客内容到服务器，请求创建博客
     */
    private void postBlogDataToServer() {
        // 传输图片
        for (File file : imageList) {
            postFileToServer(file);
        }

        FormBody formBody = new FormBody.Builder()
                .add(ServerPostDataConstant.USER_ID, userId)
                .add(ServerPostDataConstant.CREATE_BLOG_TITLE,
                        createBlogContentTitleEditText.getText().toString())
                .add(ServerPostDataConstant.CREATE_BLOG_CONTENT, createBlogContentString)
                .build();

        Call call = HttpUtil.sendHttpPost(ServerUrlConstant.CREATE_BLOG_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadingProgressDialog.closeProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    String responseText = response.body().string();
                    CreateBlogGson createBlogGson = JsonUtil.handleCreateBlogResponse(responseText);
                    if (createBlogGson.getMsg().equals(CommunityConstant.CREATE_BLOG_SUCCESS)) {
                        uploadingProgressDialog.closeProgressDialog();
                        setResult(RESULT_OK);
                        finish();
                    }
                    else if (createBlogGson.getMsg().equals(CommunityConstant.CREATE_BLOG_ERROR)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadingProgressDialog.closeProgressDialog();
                                Toast.makeText(CreateBlogActivity.this,
                                        HintConstant.CREATE_BLOG_ERROR, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uploadingProgressDialog.closeProgressDialog();
                            Toast.makeText(CreateBlogActivity.this,
                                    HintConstant.CREATE_BLOG_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        getCallList().add(call);
    }

    /**
     * 传输文件到服务器
     * @param file：文件
     */
    private void postFileToServer(final File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(ServerPostDataConstant.POST_FILE, file.getName(),
                        RequestBody.create(null, file));

        Call call = HttpUtil.sendHttpPostFile(ServerUrlConstant.POST_IMAGE_TO_SERVER_URI, builder, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            }
        });

        getCallList().add(call);
    }

    /**
     * 获取RichTextEditor中的内容和提取其中的图片
     * @return : 返回博客内容
     */
    private String getEditData() {
        StringBuilder content = new StringBuilder();
        try {
            List<RichTextEditor.EditData> editList = createBlogContentEditText.buildEditData();
            for (RichTextEditor.EditData itemData : editList) {
                if (itemData.inputStr != null) {
                    content.append(itemData.inputStr);
                } else if (itemData.imagePath != null) {
                    if (imageList == null)
                        imageList = new ArrayList<>();
                    imageList.add(new File(itemData.imagePath));
                    String imageName = getImageNameFromPath(itemData.imagePath);
                    String imageServerPath = ServerUrlConstant.IMAGE_FILES_PATH + imageName;
                    content.append("<img src=\"").append(imageServerPath).append("\"/>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private void callGallery(){
        Matisse.from(this)
                .choose(MimeType.ofImage())//照片视频全部显示MimeType.allOf()
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(MAX_NUMBER_Of_IMAGES)//最大选择数量为3
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//图像选择和预览活动所需的方向
                //.thumbnailScale(0.85f)//缩放比例
                .theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new PicassoEngine())//图片加载方式，Glide4需要自定义实现
                .capture(true) //是否提供拍照功能，兼容7.0系统需要下面的配置
                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .captureStrategy(new CaptureStrategy(true,"com.dalao.yiban.matisse.fileprovider"))//存储到哪里
                .forResult(REQUEST_CODE_CHOOSE_IMAGE);//请求码
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == REQUEST_CODE_CHOOSE_IMAGE){
                    //异步方式插入图片
                    insertImagesSync(data);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            // 更改头像请求权限
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    closeSoftKeyInput();//关闭软键盘
                    callGallery();
                }
                else {
                    Toast.makeText(this, HintConstant.PERMISSION_REFUSE, Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 异步方式插入图片
     */
    private void insertImagesSync(final Intent data){
        loadingProgressDialog.showProgressDialog();

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try{
                    createBlogContentEditText.measure(0, 0);
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    // 可以同时插入多张图片
                    for (Uri imageUri : mSelected) {
                        String imagePath = SDCardUtil.getFilePathFromUri(CreateBlogActivity.this,  imageUri);
                        int screenWidth = CommonUtil.getScreenWidth(CreateBlogActivity.this);
                        int screenHeight = CommonUtil.getScreenHeight(CreateBlogActivity.this);
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, screenWidth, screenHeight);//压缩图片
                        imagePath = SDCardUtil.saveToSdCard(bitmap);
                        emitter.onNext(imagePath);
                    }

                    emitter.onComplete();
                }catch (Exception e){
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        loadingProgressDialog.closeProgressDialog();
                        Toast.makeText(CreateBlogActivity.this, HintConstant.INSERT_IMAGE_SUCCESS,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingProgressDialog.closeProgressDialog();
                        Toast.makeText(CreateBlogActivity.this, HintConstant.INSERT_IMAGE_ERROR,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String imagePath) {
                        createBlogContentEditText.insertImage(imagePath, createBlogContentEditText.getMeasuredWidth());
                    }
                });
    }

    /**
     * 关闭软键盘
     */
    private void closeSoftKeyInput(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if (imm != null && imm.isActive() && getCurrentFocus() != null){
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 打开软键盘
     */
    private void openSoftKeyInput(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if (imm != null && !imm.isActive() && createBlogContentEditText != null){
            createBlogContentEditText.requestFocus();
            //第二个参数可设置为0
            //imm.showSoftInput(et_content, InputMethodManager.SHOW_FORCED);//强制显示
            imm.showSoftInputFromInputMethod(createBlogContentEditText.getWindowToken(),
                    InputMethodManager.SHOW_FORCED);
        }
    }

    @Override
    public void onBackPressed() {
        if (!imageWatcherHelper.handleBackPressed()) {
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        }
    }

    /**
     * 从图片路径中获取图片名称
     * @param imagePath：图片路径
     * @return ：图片名称
     */
    private String getImageNameFromPath(String imagePath) {
        String[] tmp = imagePath.split("/");
        return tmp[tmp.length - 1];
    }

    /**
     * 检查博客的合法性
     * @return ：true合法，false不合法
     */
    private boolean checkBlog() {
        // 博客标题为空
        if (createBlogContentTitleEditText.getText().toString().equals("")) {
            Toast.makeText(this, HintConstant.CREATE_BLOG_TITLE_EMPTY,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // 博客内容过少
        else if (createBlogContentString.length() < CommunityConstant.CREATE_BLOG_CONTENT_MIN_LENGTH) {
            Toast.makeText(this, HintConstant.CREATE_BLOG_CONTENT_LESS,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // 博客内容过多
        else if (createBlogContentString.length() > CommunityConstant.CREATE_BLOG_CONTENT_MAX_LENGTH) {
            Toast.makeText(this, HintConstant.CREATE_BLOG_CONTENT_MORE,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // 图片过多
        else if (imageList.size() > MAX_NUMBER_Of_IMAGES) {
            Toast.makeText(this, HintConstant.TOO_MANY_IMAGES,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
