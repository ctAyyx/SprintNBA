package com.ct.sprintnba_demo01.mutils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.ct.sprintnba_demo01.madapter.NewsListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by ct on 2017/3/2.
 */

public class Permission {

    private Object obj;
    private static Object currentObj;
    private List<String> mDecline;
    private List<String> mDeclinAgain;
    private PermissionResult result;

    private static Map<Object, PermissionResult> map = new HashMap<>();


    private Permission(Object obj) {
        this.obj = obj;
    }

    public static Permission init(Activity activity) {
        return new Permission(activity);
    }

    public static Permission init(Fragment fragment) {
        return new Permission(fragment);
    }

    public static Permission init(android.app.Fragment fragment) {
        return new Permission(fragment);
    }

    public Permission onCallBack(PermissionResult result) {
        this.result = result;
        return this;
    }

    public void reqPermission(int code, String[] permissions) {
        //判断申请权限数组是否为空
        if (permissions == null || permissions.length == 0)
            return;
        //判断调用者是否是Activity或Fragment
        Activity activity = getActivity(obj);
        if (activity == null)
            throw new IllegalArgumentException("caller should is activity or fragment");
        //获取其中未授权权限
        mDecline = checkDeclinePermission(activity, permissions);
        //判断是否存在未授权权限
        if (mDecline == null || mDecline.size() == 0) {
            //没有拒绝的权限
            if (result != null)
                result.onGranted(code, permissions, PackageManager.PERMISSION_GRANTED);
            return;
        }
        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
        // mDeclinAgain = checkReqPermissionAgain(activity, mDecline);
        String[] declinePermissions = null;
        if (!mDecline.isEmpty())
            declinePermissions = mDecline.toArray(new String[mDecline.size()]);

        //请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && declinePermissions != null && declinePermissions.length != 0) {
            if (obj instanceof Activity)
                ((Activity) obj).requestPermissions(declinePermissions, code);
            if (obj instanceof Fragment)
                ((Fragment) obj).requestPermissions(declinePermissions, code);
            if (obj instanceof android.app.Fragment)
                ((android.app.Fragment) obj).requestPermissions(declinePermissions, code);
            map.put(obj, result);
            currentObj = obj;
        }
        //显示已被禁止的权限，并提示用户手动开启权限
        if (mDeclinAgain != null && mDeclinAgain.size() != 0)
            showDialogTipUserGoToAppSettting(activity);

    }

    // 提示用户去应用设置界面手动开启权限
    private void showDialogTipUserGoToAppSettting(final Context context) {
        new AlertDialog.Builder(context).setTitle("应用权限申请").setMessage("部分权限被拒绝，请到设置中手动开启权限").setPositiveButton("开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToAppSetting(context);
            }
        }).setNegativeButton("取消", null).create().show();

    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting(Context context) {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            PermissionResult result = map.get(currentObj);
            if (result != null)
                result.onGranted(requestCode, permissions, PackageManager.PERMISSION_GRANTED);
        } else {
            // checkReqPermissionAgain()

            PermissionResult result = map.get(currentObj);
            if (result != null)
                result.onDecline(requestCode, permissions, PackageManager.PERMISSION_DENIED);
        }

    }

    /**
     * 检查为授权权限
     */
    private List<String> checkDeclinePermission(Context context, String[] permissions) {
        if (permissions == null)
            throw new NullPointerException("请求权限集合为空");
        List<String> decline = new ArrayList<>();
        for (String permission : permissions) {
            int i = ContextCompat.checkSelfPermission(context, permission);
            if (i != PackageManager.PERMISSION_GRANTED)
                decline.add(permission);
        }
        return decline;
    }

    /**
     * 用于验证调用者是否为Activity或Fragment
     */
    private Activity getActivity(Object obj) {
        if (obj instanceof Activity)
            return (Activity) obj;
        if (obj instanceof Fragment)
            return ((Fragment) obj).getActivity();
        if (obj instanceof android.app.Fragment)
            return ((android.app.Fragment) obj).getActivity();

        return null;
    }

    /**
     * 验证权限是否被拒绝
     */
    private static List<String> checkReqPermissionAgain(Activity activity, List<String> permissions) {
        if (permissions == null || permissions.size() == 0)
            return null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return null;

        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            boolean can = activity.shouldShowRequestPermissionRationale(permission);
            if (!can) {
                //将此权限添加到集合
                list.add(permission);
                permissions.remove(permission);
            }

        }
        return list;
    }

    public interface PermissionResult {
        void onGranted(int code, String[] permissions, int result);

        void onDecline(int code, String[] permissions, int result);
    }


}
