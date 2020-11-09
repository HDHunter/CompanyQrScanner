package com.example.log;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yodo1.log.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    public static String readFilesFromSDCard(String fileName) {
        // 定义文件内容字符串
        String content = null;
        // 文件输入流
        FileInputStream fileInputStream;
        // 判断SD卡是否存在，并且本程序是否拥有SD卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 获得SD卡的根目录
            File sdCardPath = Environment.getExternalStorageDirectory();
            /*
             * 文件输出操作
             * */
            try {
                File testFile = new File(sdCardPath, fileName);
                // 打开文件输入流
                fileInputStream = new FileInputStream(testFile);
                // 将文件输入流存放在ByteArrayOutputStream
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                // 定义每次读取一个字节
                byte[] buffer = new byte[1024];
                // 定义每次读取的字节长度
                int len;
                // 读取文件输入流的内容，并存入ByteArrayOutputStream中
                while ((len = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                // 将文件输入流数据以字符串的形式存放
                content = outputStream.toString();
                // 关闭文件输入流
                fileInputStream.close();
                // 关闭ByteArrayOutputStream
                outputStream.close();
            } catch (Exception e) {
                Log.e("yodo1 appInfo", "yodo1 缺少SD卡权限  读取文件失败");
            }
        }

        // 返回文件内容
        return content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView open = findViewById(R.id.openlog);
        TextView close = findViewById(R.id.closelog);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 55);
                        return;
                    }
                    // 定义文件内容字符串
                    final String adsFlag = ".yodo1ads";
                    final String adsFlag1 = ".yodo1";
                    String str = readFilesFromSDCard(adsFlag);
                    String str1 = readFilesFromSDCard(adsFlag1);
                    if (!TextUtils.isEmpty(str)) {
                        Log.e("：", str);
                    }
                    if (!TextUtils.isEmpty(str1)) {
                        Log.e("：", str1);
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(".yodo1文件内容");
                    try {
                        builder.setMessage("" + str + "\nID:" + str1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    builder.setPositiveButton("全部删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                try {
                                    File testFile = new File(Environment.getExternalStorageDirectory(), adsFlag);
                                    if (testFile.exists()) {
                                        boolean delete = testFile.delete();
                                        Log.e("zzzzzzzzz", adsFlag + "    删除" + (delete ? "成功" : "失败"));
                                    }
                                    File testFile1 = new File(Environment.getExternalStorageDirectory(), adsFlag1);
                                    if (testFile1.exists()) {
                                        boolean delete = testFile1.delete();
                                        Log.e("zzzzzzzzz", adsFlag1 + "    删除" + (delete ? "成功" : "失败"));
                                    }
                                } catch (Exception e) {
                                    Log.d("zzzzzz", "yodo1 缺少SD卡权限  读取文件失败");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                                    }
                                }
                            }

                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 55);
                        return;
                    }
                    // 定义文件内容字符串
                    boolean isDebugLogEnabled = false;
                    String adsFlag = ".yodo1ads";
                    String str = readFilesFromSDCard(adsFlag);
                    if (!TextUtils.isEmpty(str) && str.contains("openYodo1Log")) {
                        isDebugLogEnabled = true;
                    }
                    if (!TextUtils.isEmpty(str)) {
                        Log.e("文件.yodo1ads内容：", str);
                    }
                    if (str == null || !str.contains("openYodo1Log")) {
                        Toast.makeText(MainActivity.this, "openYodo1Log不存在，开始创建", Toast.LENGTH_SHORT).show();
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            try {
                                File testFile = new File(Environment.getExternalStorageDirectory(), adsFlag);
                                if (!testFile.exists()) {
                                    testFile.createNewFile();
                                }
                                FileOutputStream fileOutputStream = new FileOutputStream(testFile);
                                if (TextUtils.isEmpty(str)) {
                                    str = "openYodo1Log";
                                } else {
                                    str += "\nopenYodo1Log";
                                }
                                fileOutputStream.write(str.getBytes());
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            } catch (FileNotFoundException e) {
                                Log.d("zzzzz", "yodo1 缺少SD卡权限  读取文件失败");
                            } catch (Exception e) {
                                Log.d("zzzzzz", "yodo1 缺少SD卡权限  读取文件失败");

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                                }
                            }
                        }
                    }
                    String s = str.contains("openYodo1Log") ? "全局debugLog标记成功" : "全局debugLog标记开启失败";
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}