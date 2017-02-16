package com.hlxyjqd.yjqd.Utils;

import android.content.Context;
import android.util.Xml;

import com.alibaba.fastjson.JSON;
import com.hlxyjqd.yjqd.Bean.CloudidInfo;
import com.hlxyjqd.yjqd.Bean.UserInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
        CloudidInfo cloudidInfo = null;
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
                        if ("map".equals(tagName)) {

                            id = parser.getAttributeValue(null, "name");
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


}
