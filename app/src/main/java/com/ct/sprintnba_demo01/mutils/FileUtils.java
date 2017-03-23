package com.ct.sprintnba_demo01.mutils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.mentity.Music;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ct on 2017/2/15.
 */

public class FileUtils {
    private static String getAppDir() {
        return Environment.getExternalStorageDirectory() + "/SprintMusic";
    }

    public static String getMusicDir() {
        String dir = getAppDir() + "/Music/";
        return mkdirs(dir);
    }

    public static String getLrcDir() {
        String dir = getAppDir() + "/Lyric/";
        return mkdirs(dir);
    }

    public static String getLogDir() {
        String dir = getAppDir() + "/Log/";
        return mkdirs(dir);
    }

    public static String getSplashDir(Context context) {
        String dir = context.getFilesDir() + "/splash/";
        return mkdirs(dir);
    }

    public static String getRelativeMusicDir() {
        String dir = "SprintMusic/Music/";
        return mkdirs(dir);
    }

    public static String getTempDir() {
        String dir = getAppDir() + "/photo/";
        return mkdirs(dir);
    }


    /**
     * 获取歌词路径
     * 先从已下载文件夹中查找，如果不存在，则从歌曲文件所在文件夹查找
     */
    public static String getLrcFilePath(Music music) {
        String lrcFilePath = getLrcDir() + music.fileName.replace(OwnConstant.FILENAME_MP3, OwnConstant.FILENAME_LRC);
        if (!new File(lrcFilePath).exists()) {
            lrcFilePath = music.uri.replace(OwnConstant.FILENAME_MP3, OwnConstant.FILENAME_LRC);
        }
        return lrcFilePath;
    }

    private static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    public static String getMp3FileName(String artist, String title) {
        return getFileName(artist, title) + OwnConstant.FILENAME_MP3;
    }

    public static String getLrcFileName(String artist, String title) {
        return getFileName(artist, title) + OwnConstant.FILENAME_LRC;
    }

    public static String getFileName(String artist, String title) {
        artist = stringFilter(artist);
        title = stringFilter(title);
        if (TextUtils.isEmpty(artist)) {
            artist = "未知";
        }
        if (TextUtils.isEmpty(title)) {
            title = "未知";
        }
        return artist + " - " + title;
    }

    public static String getArtistAndAlbum(String artist, String album) {
        if (TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return "";
        } else if (!TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return artist;
        } else if (TextUtils.isEmpty(artist) && !TextUtils.isEmpty(album)) {
            return album;
        } else {
            return artist + " - " + album;
        }
    }

    /**
     * 过滤特殊字符(\/:*?"<>|)
     */
    private static String stringFilter(String str) {
        if (str == null) {
            return null;
        }
        String regEx = "[\\/:*?\"<>|]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static float b2mb(int b) {
        String mb = String.format(Locale.getDefault(), "%.2f", (float) b / 1024 / 1024);
        return Float.valueOf(mb);
    }

    public static void saveLrcFile(String path, String content) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描歌曲
     */
    public static void scanMusic(Context context, List<Music> musicList) {
        if (musicList == null)
            return;

        musicList.clear();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null)
            return;

        while (cursor.moveToNext()) {
            //判断是否为音乐
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic == 0)
                continue;

            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String unknown = "未知";
            artist = artist.equals("<unknown>") ? unknown : artist;
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String uri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            String coverUri = getCoverUri(context, albumId);
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String year = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR));

            //判断文件大小 小于300K 不录入
            if (fileSize < 1024 * 300)
                continue;


            Music music = new Music();
            music.id = id;
            music.type = Music.Type.LOCAL;
            music.title = title;
            music.artist = artist;
            music.album = album;
            music.duration = duration;
            music.uri = uri;
            music.coverUri = coverUri;
            music.fileName = fileName;
            music.fileSize = fileSize;
            music.year = year;
            musicList.add(music);
            Log.i("TAG", "本地音乐" + music.toString());
        }
        cursor.close();
    }


    private static String getCoverUri(Context context, long albumId) {
        String uri = "";
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://media/external/audio/albums/" + albumId), new String[]{"album_art"}, null, null, null);
        if (cursor != null) {
            cursor.moveToNext();
            uri = cursor.getString(0);
            cursor.close();
        }
        return uri;

    }

    /**
     * 获取缓存大小
     */
    public static String getCacheSize(Context context) {
        long fileSize = 0;
        String cacheSize = "0KB";

        File fileDir = context.getFilesDir();
        File cacheDir = context.getCacheDir();
        File externalCacheDir = context.getExternalCacheDir();

        fileSize += getDirSize(fileDir);
        fileSize += getDirSize(cacheDir);
        fileSize += getDirSize(externalCacheDir);
        if (fileSize > 0)
            cacheSize = formatFileSize(fileSize);
        return cacheSize;

    }

    /**
     * 计算目录文件大小
     */
    public static long getDirSize(File dir) {
        if (dir == null)
            return 0;
        if (!dir.isDirectory())
            return 0;

        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile())
                dirSize += file.length();
            else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }

    /**
     * 转换文件大小
     */
    public static String formatFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileSize < 1024)
            fileSizeString = df.format((double) fileSize) + "B";
        else if (fileSize < 1024 * 1024)
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        else if (fileSize < 1024 * 1024 * 1024)
            fileSizeString = df.format((double) fileSize / (1024 * 1024)) + "MB";
        else
            fileSizeString = df.format((double) fileSize / (1024 * 1024 * 1024)) + "G";
        return fileSizeString;
    }

    /**
     * 清空应用缓存
     */
    public static void cleanAppCache(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanFiles(context);
    }

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     */
    public static void deleteFilesByDirectory(File fileDir) {
        if (fileDir != null && fileDir.exists() && fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            for (File file : files) {
                if (file.isDirectory())
                    deleteFilesByDirectory(file);
                file.delete();
            }
            fileDir.delete();
        }
    }

}
