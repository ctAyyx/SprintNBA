package com.ct.sprintnba_demo01.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ct.sprintnba_demo01.base.IView;

import java.lang.reflect.Field;

/**
 * Created by ct on 2017/1/13.
 * ============================
 * Fragment基类
 * ============================
 */

public class BaseFragment<T> extends Fragment implements IView<T> {
    protected LayoutInflater inflater;
    private View contentView;
    private Context mContext;
    private ViewGroup container;
    public Activity mActivity;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;
        onCreateView(savedInstanceState);
        if (contentView == null)
            return super.onCreateView(inflater, container, savedInstanceState);
        return contentView;
    }

    protected void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contentView = null;
        container = null;
        inflater = null;
    }

    public Context getApplicationContext() {
        return mContext;
    }

    public void setContentView(int layoutId) {

        setContentView(inflater.inflate(layoutId, container, false));
    }

    public void setContentView(View view) {
        contentView = view;
    }

    public View getContentView() {
        return contentView;
    }

    public View findViewById(int id) {
        if (contentView != null)
            return contentView.findViewById(id);
        return null;
    }

    @Override
    public void onDestroyView() {
        contentView=null;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            //利用反射，当前Fragment销毁时，移除FragmentManagerImpl
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateView(int reqId, T response) {

    }
}
