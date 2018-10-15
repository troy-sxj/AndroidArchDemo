package com.mika.lib.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Author: mika
 * @Time: 2018/10/15 上午10:54
 * @Description:
 */
public abstract class BaseFragment extends Fragment {

    private boolean hasAttach = false;

    private boolean isVisible = false;

    public BaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = getInflateView();
        if (rootView == null) {
            rootView = LayoutInflater.from(getContext()).inflate(getLayout(), null);
        }
        this.initView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hasAttach = true;
        initListener();
        onPreInitData();
        initData(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        changeUiVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeUiVisible(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        changeUiVisible(!hidden);
    }

    private void changeUiVisible(boolean visible){
        if(this.isVisible != visible){
            onUiVisibleChange(visible);
        }
        this.isVisible = visible;
    }

    /**
     * 获取Fragment可见状态
     * @return  true： 可见， false：不可见
     */
    protected boolean isVisibleToUser(){
        return this.isVisible;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isVisible = false;
        hasAttach = false;
    }

    /**
     * 自定义InflateView
     *
     * @return
     */
    protected View getInflateView() {
        return null;
    }

    /**
     * 监听Fragment UI可见状态变换
     * @param visible true: 可见， false: 不可见
     */
    protected void onUiVisibleChange(boolean visible){

    }

    protected void onPreInitData(){

    }

    protected abstract void initView(View rootView);

    protected abstract void initListener();

    protected abstract void initData(Bundle bundle);

    protected abstract int getLayout();
}
