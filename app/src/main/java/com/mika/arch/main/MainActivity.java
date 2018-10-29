package com.mika.arch.main;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mika.arch.R;
import com.mika.lib.mvp.MvpActivity;

public class MainActivity extends MvpActivity<MainPresenter> implements MainView {

    private TextView tvContent;
    private Button btn;
    private Button btnLoad;
    private ImageView imageView;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void initView() {
        tvContent = findViewById(R.id.tv);
        btn = findViewById(R.id.btn);
        btnLoad = findViewById(R.id.btn_load);
        imageView = findViewById(R.id.image_view);
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
