package com.mika.arch.main;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mika.arch.R;
import com.mika.lib.image.ArchImageView;
import com.mika.lib.image.ImageLoader;
import com.mika.lib.mvp.MvpActivity;

public class MainActivity extends MvpActivity<MainPresenter> implements MainView {

    private TextView tvContent;
    private Button btn;
    private Button btnLoad;
    private ArchImageView imageView1, imageView2, imageView3, imageView4;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void initView() {
        tvContent = findViewById(R.id.tv);
        btn = findViewById(R.id.btn);
        btnLoad = findViewById(R.id.btn_load);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
    }

    @Override
    protected void initListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.request();
            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader.showImage(imageView1, "http://n.sinaimg.cn/edu/transform/20160301/LAWp-fxpwmrp0377606.jpg");
                ImageLoader.showImage(imageView2, "http://n.sinaimg.cn/edu/transform/20160301/LAWp-fxpwmrp0377606.jpg");
                ImageLoader.showImage(imageView3, "http://img.youai123.com/1507667222-3317.gif");
                ImageLoader.showImage(imageView4, "http://img.youai123.com/1507667222-3317.gif");
            }
        });
    }

    @Override
    protected void initData(Bundle bundle) {
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onGetContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }
    }
}
