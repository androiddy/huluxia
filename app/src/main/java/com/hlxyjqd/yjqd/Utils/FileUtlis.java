package com.hlxyjqd.yjqd.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hlxyjqd.yjqd.Bean.CloudidInfo;
import com.hlxyjqd.yjqd.Bean.UserInfo;
import com.hlxyjqd.yjqd.MyApplication;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zhangzhongping on 17/1/1.
 */

public class FileUtlis {
    public static void CopyHlxXml(Context mContext, File file, String path) {
        File file1 = new File(mContext.getCacheDir().getAbsolutePath());
        if (!file1.exists()) {
            file1.mkdir();
        }
        Process process = null;
        BufferedReader br = null;
        DataOutputStream dataOutputStream = null;
        try {
            process = Runtime.getRuntime().exec("su");
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataOutputStream.writeBytes("/system/bin/cp " + path + " " + file.getAbsolutePath() + "\n\n");
            dataOutputStream.writeBytes("/system/bin/chmod 777 " + file.getAbsolutePath() + "\n\n");
            dataOutputStream.writeBytes("exit\n\n");
            dataOutputStream.flush();
            process.waitFor();
            dataOutputStream.close();
            br.close();
            process.destroy();
        } catch (IOException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static Object[] parserXmlFromLocal(Context mContext, File file) {
        Object[] objects = null;
        com.hlxyjqd.yjqd.Bean.UserInfo userInfo = null;
        CloudidInfo cloudidInfo = new CloudidInfo();
        FileInputStream fis = null;
        boolean str = false;
        try {
            fis = new FileInputStream(file);
            // 获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            // c指定解析的文件和编码格式
            parser.setInput(fis, "utf-8");
            int eventType = parser.getEventType();
            String id;
            String token = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        id = String.valueOf(parser.getAttributeValue(null, "name"));
                        if (id.equals("userinfo")) {
                            str = true;
                            userInfo = (UserInfo) JSON.parseObject(parser.nextText(), UserInfo.class);
                        }
                        if (id.equals("cloudidinfo")) {
                            cloudidInfo = (CloudidInfo) JSON.parseObject(parser.nextText(), CloudidInfo.class);

                        }
                        if (id.equals("token")) {
                            str = true;
                            token = parser.nextText();

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("map".equals(tagName)) {

                            if (!str) {
                                SPUtils.remove(mContext, "userinfo");
                                SPUtils.remove(mContext, "token");
                            } else {
                                userInfo.setToken(token);
                                objects = new Object[2];
                                objects[0] = userInfo;
                                objects[1] = cloudidInfo;
                            }
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return objects;
    }

    public static void copyFile(File source, File target) throws IOException {
        target.delete();
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(target);
            FileChannel iChannel = inputStream.getChannel();
            FileChannel oChannel = outputStream.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                buffer.clear();
                int r = iChannel.read(buffer);
                if (r == -1)
                    break;
                buffer.limit(buffer.position());
                buffer.position(0);
                oChannel.write(buffer);
            }
        } finally {
            closeQuietly(inputStream);
            closeQuietly(outputStream);
        }
    }

    public static Object[] LoadData(Context mContext) {
        com.hlxyjqd.yjqd.Bean.UserInfo userInfo = null;
        CloudidInfo cloudidInfo = new CloudidInfo();
        boolean str = false;
        Object[] objects = null;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("hlx", 0);
        if (sharedPreferences.contains("userinfo")) {
            str = true;
            userInfo = (UserInfo) JSON.parseObject(sharedPreferences.getString("userinfo", ""), UserInfo.class);
        }
        if (sharedPreferences.contains("cloudidinfo")) {
            cloudidInfo = (CloudidInfo) JSON.parseObject(sharedPreferences.getString("cloudidinfo", ""), CloudidInfo.class);
        }
        if (sharedPreferences.contains("token")) {
            str = true;
            if (userInfo != null) {
                userInfo.setToken(sharedPreferences.getString("token", ""));
            }
        }
        if (str) {
            objects = new Object[2];
            objects[0] = userInfo;
            objects[1] = cloudidInfo;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit().clear();
        SPUtils.SharedPreferencesCompat.apply(editor);
        return objects;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }
}
