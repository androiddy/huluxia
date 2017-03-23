package com.hlxyjqd.yjqd.View.Impl;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.hlxyjqd.yjqd.MyService;
import com.hlxyjqd.yjqd.Presenter.VirtualPresenter;
import com.hlxyjqd.yjqd.R;
import com.hlxyjqd.yjqd.Utils.SPUtils;
import com.hlxyjqd.yjqd.Utils.VUiKit;
import com.hlxyjqd.yjqd.View.InstallRunView;
import com.hlxyjqd.yjqd.ViewUtils.AlertDialog;
import com.hlxyjqd.yjqd.ViewUtils.TwoGearsView;
import com.hlxyjqd.yjqd.api.AppVirtualService;
import com.hlxyjqd.yjqd.api.VirtualApi;
import com.hlxyjqd.yjqd.updata.Utils.Toasts;
import com.hlxyjqd.yjqd.updata.manager.UpdateManager;
import com.lody.virtual.remote.InstalledAppInfo;

import java.io.File;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * 作者：zhangzhongping on 17/3/12 17:56
 * 邮箱：android_dy@163.com
 */
public class MainRunActvivty extends BaseActivity<InstallRunView, VirtualPresenter> implements InstallRunView {
    Button button;
    SmoothProgressBar progressBar;
    TwoGearsView mLoadingView;
    TextView textView;

    @Override
    protected void onResume() {
        super.onResume();
        if (((int) SPUtils.get(getApplicationContext(), "index", -1)) == VUiKit.getVersion()) {
            textView.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.VISIBLE);
            mLoadingView.startAnim();
            VerifyApp();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_loading;
    }

    @Override
    protected void InitView() {
        TestApi();
        ImageView app_icon = (ImageView) findViewById(R.id.app_icon);
        textView = (TextView) findViewById(R.id.textView3);
        textView.setVisibility(View.INVISIBLE);
        app_icon.setImageResource(R.mipmap.ic_root_run);
        mLoadingView = (TwoGearsView) findViewById(R.id.pb_loading_app);
        button = (Button) findViewById(R.id.button2);
        progressBar = (SmoothProgressBar) findViewById(R.id.smoothProgressBar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog actionSheetDialog = new AlertDialog(MainRunActvivty.this);
                actionSheetDialog.builder().setCancelable(false).setTitle("使用说明").setMsg("1:确保手机已经安装QQ\r\n\r\n2:进入葫芦侠后需要重新的登录\r\n\r\n3:如果QQ登录时提示非正版应用，多登录几次即可！！\r\n\r\n4:" +
                        "如果登陆后还是无法使用请退出重新进入即可！\r\n\r\n5:点击状态栏提示框即可跳转签到界面\r\n\r\n6:如果无法正常显示状态栏请手动在设置中打开状态栏权限！\r\n\r\n7:首次使用初始化时间会略长！请耐心等待！\r\n\r\n8:黑屏等待即可！").setPositiveButton("开启", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setVisibility(View.VISIBLE);
                        button.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        mLoadingView.setVisibility(View.VISIBLE);
                        mLoadingView.startAnim();
                        CopyApp();
                    }
                }).show();
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
        mLoadingView.stopAnim();
    }

    @Override
    protected VirtualPresenter createPresenter() {
        return new VirtualPresenter(VirtualApi.getInstance(new AppVirtualService()));
    }

    @Override
    public void CopyComplete(File file) {
        addVirtualApp(file);
    }

    @Override
    public void CopyError(String msg) {
        ShowAlertDialog(msg);
    }

    @Override
    public void InstallComplete(InstalledAppInfo appData) {
        mPresenter.launchApp(appData, 0);
    }

    @Override
    public void InstallError(String msg) {
        ShowAlertDialog(msg);
    }

    @Override
    public void RunComplete(String msg) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getApplicationContext().startService(new Intent(getApplicationContext(), MyService.class));
                SPUtils.put(getApplicationContext(), "index", VUiKit.getVersion());
            }
        }, 4000);
    }

    @Override
    public void RunError(String msg) {
        Toasts.ToastInfo(getApplicationContext(), msg);
    }

    public void TestApi() {
        UpdateManager updateManager = new UpdateManager(this);
        updateManager.checkUpdate(this, true);
        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {

                            }
                        }, R.string.btn_show,
                Manifest.permission.ACCESS_FINE_LOCATION);
       /* Intent prepare = VpnService.prepare(this);
        if (prepare != null) {
            startActivityForResult(prepare, 0);
            return;
        }
        onActivityResult(0, RESULT_OK, null);*/
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent prepare = new Intent(this, PacketCaptureService.class);
            String m = CaptureData.getDumpFileName();
            String a = CaptureData.getDumpDirName(this);
            prepare.putExtra(getPackageName() + "intent_extra_key_string_capture_file_name", a + "/" + m);
            startService(prepare);
        }
    }*/

    public void VerifyApp() {
        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                mPresenter.VerifyApp();
                            }
                        }, R.string.btn_show,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void CopyApp() {
        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                mPresenter.CopyApp();
                            }
                        }, R.string.btn_show,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void addVirtualApp(final File f) {
        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                mPresenter.addVirtualApp(f);
                            }
                        }, R.string.btn_show,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void ShowAlertDialog(String msg) {
        AlertDialog actionSheetDialog = new AlertDialog(MainRunActvivty.this);
        actionSheetDialog.builder().setCancelable(false).setTitle("提示信息").setMsg(msg).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).show();
    }
}
