package com.ct.sprintnba_demo01.music;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.ct.sprintnba_demo01.mentity.Bitrate;
import com.ct.sprintnba_demo01.minterface.MusicPlayInterface;
import com.ct.sprintnba_demo01.mutils.FileUtils;

/**
 * Created by ct on 2017/2/20.
 */

public abstract class DownMusic implements MusicPlayInterface<Bitrate> {
    protected static Context mContext;

    public DownMusic(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void execute() {
        checkNet();
    }

    /**
     * 检查网络连接
     */
    private void checkNet() {
        downloadWrapper();
    }

    private void downloadWrapper() {
        onPrepare();
        doownload();
    }

    protected abstract void doownload();

    protected static void downMusic(String url, String artist, String song) {
        String fileName = FileUtils.getMp3FileName(artist, song);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(FileUtils.getFileName(artist, song));
        request.setDescription("正在下载......");
        request.setDestinationInExternalPublicDir(FileUtils.getRelativeMusicDir(), fileName);
        request.setMimeType(MimeTypeMap.getFileExtensionFromUrl(url));
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);//不允许漫游

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        long id = downloadManager.enqueue(request);

    }
}
