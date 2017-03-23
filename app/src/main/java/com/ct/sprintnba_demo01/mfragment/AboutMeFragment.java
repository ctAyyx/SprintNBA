package com.ct.sprintnba_demo01.mfragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.ct.sprintnba_demo01.BuildConfig;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.mutils.Permission;

/**
 * Created by Administrator on 2017/3/2.
 */

public class AboutMeFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Permission.PermissionResult {

    Preference version;//APP版本
    Preference updata; //APP更新
    Preference share;//APP分享
    Preference star;//APP地址
    Preference email;
    Preference telphone;//电话
    Preference github;//github

    private String[] permissions = {Manifest.permission.CALL_PHONE};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about_me);

        version = findPreference("version");
        updata = findPreference("updata");
        share = findPreference("share");
        star = findPreference("star");
        email = findPreference("email");
        telphone = findPreference("telphone");
        github = findPreference("github");

        version.setSummary("v" + BuildConfig.VERSION_NAME);

        updata.setOnPreferenceClickListener(this);
        share.setOnPreferenceClickListener(this);
        star.setOnPreferenceClickListener(this);
        email.setOnPreferenceClickListener(this);
        telphone.setOnPreferenceClickListener(this);
        github.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == updata)
            updata();
        if (preference == share)
            share();
        if (preference == star)
            star();
        if (preference == email)
            sendEmail();
        if (preference == telphone)
            telphone();
        if (preference == github)
            github();

        return false;
    }


    /**
     * 检查更新
     */
    private void updata() {
    }

    /**
     * 分享
     */
    private void share() {


    }

    /**
     * 前往GitHub点赞
     */
    private void star() {

    }

    /**
     * 给作者打电话
     */
    private void telphone() {
        Permission.init(this).onCallBack(this).reqPermission(100, permissions);

    }


    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "18080947055"));
        startActivity(intent);
    }

    /**
     * 前往作者的github
     */
    private void github() {
        goToUrl(github.getSummary().toString());
    }

    /**
     * 发送邮件
     */
    private void sendEmail() {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailTo:" + email.getSummary()));
        data.putExtra(Intent.EXTRA_SUBJECT, "小伙子，APP还行哈");
        data.putExtra(Intent.EXTRA_TEXT, "9999999这是6翻的节奏");
        startActivity(Intent.createChooser(data, "Choose Email Client"));
    }

    /**
     * 前往指定网址
     */
    private void goToUrl(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    /**********************************************************************
     * 权限请求
     **********************************************************************/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permission.onRequestPermissionResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onGranted(int code, String[] permissions, int result) {
        if (code == 100)
            call();
    }

    @Override
    public void onDecline(int code, String[] permissions, int result) {

    }
}
