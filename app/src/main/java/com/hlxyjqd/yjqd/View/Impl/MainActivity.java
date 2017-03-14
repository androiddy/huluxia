package com.hlxyjqd.yjqd.View.Impl;


import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.dalimao.library.util.FloatUtil;
import com.hlxyjqd.yjqd.Adapter.RunRankAdapter;
import com.hlxyjqd.yjqd.Bean.CloudidInfo;
import com.hlxyjqd.yjqd.Bean.SigninInfo;
import com.hlxyjqd.yjqd.Bean.UserAllInfo;
import com.hlxyjqd.yjqd.Bean.UserInfo;
import com.hlxyjqd.yjqd.Presenter.HlxPresenter;
import com.hlxyjqd.yjqd.R;
import com.hlxyjqd.yjqd.Utils.SPUtils;
import com.hlxyjqd.yjqd.Utils.VUiKit;
import com.hlxyjqd.yjqd.View.HlxXmlView;
import com.hlxyjqd.yjqd.ViewUtils.AlertDialog;
import com.hlxyjqd.yjqd.ViewUtils.CircleImageView;
import com.hlxyjqd.yjqd.ViewUtils.DialogToastUtlis;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import circleprogress.FloatBallView;
import numberprogressbar.NumberProgressBar;

public class MainActivity extends BaseActivity<HlxXmlView, HlxPresenter> implements HlxXmlView, View.OnClickListener {
    String path = "";
    private GridView grid;
    private Button btn_master_sh;
    private TextView id_username;
    private CircleImageView xcRoundImageView;
    private TextView textView5;
    private NumberProgressBar bnp;
    private AlertDialog actionSheetDialog;
    private String index = "";
    private DialogToastUtlis dialogToastUtlis = new DialogToastUtlis();
    private RunRankAdapter runRankAdapter = null;

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.Loadxml(path, getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatUtil.hideFloatView(getApplicationContext(), FloatBallView.class, false);
        Bundle bundle = this.getIntent().getExtras();
        path = bundle.getString("PATH");
        index = bundle.getString("INDEX");
        mPresenter.setHlxModel(1);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void InitView() {
        grid = (GridView) findViewById(R.id.grids);
        textView5 = (TextView) findViewById(R.id.textView5);
        bnp = (NumberProgressBar) findViewById(R.id.numberbar1);
        btn_master_sh = (Button) findViewById(R.id.btn_master_shs);
        id_username = (TextView) findViewById(R.id.id_username);
        xcRoundImageView = (CircleImageView) findViewById(R.id.XCRoundImageView);
        btn_master_sh.setOnClickListener(new WeakReference<MainActivity>(this).get());
    }

    @Override
    protected HlxPresenter createPresenter() {
        return new HlxPresenter();
    }

    @Override
    public void ShowLoading() {
        dialogToastUtlis.showProgressDialog("初始化中。。", MainActivity.this);
    }

    @Override
    public void CloseLoading() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogToastUtlis.closeProgressDialog();
            }
        });
    }

    @Override
    public void ShowLoadSuccess(final UserInfo userInfo, final CloudidInfo cloudidInfo) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userInfo.setDevicecode(cloudidInfo.getDevicecode());
                id_username.setText(userInfo.getNick());
                Picasso.with(getApplicationContext()).load(userInfo.getAvatar()).into(xcRoundImageView);
                bnp.setMax(userInfo.getNextExp());
                bnp.setProgress(userInfo.getExp());
                mPresenter.LoadInfo(userInfo);
            }
        });

    }

    @Override
    public void ShowLoadSuccess(final UserInfo userInfo) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView5.setText("经验:" + userInfo.getExp() + "/" + userInfo.getNextExp());
                bnp.setMax(userInfo.getNextExp());
                bnp.setProgress(userInfo.getExp());
            }
        });

    }

    @Override
    public void ShowLoadSuccess(final ArrayList arrayList) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runRankAdapter = new RunRankAdapter(MainActivity.this, arrayList);
                grid.setAdapter(runRankAdapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        UserAllInfo userAllInfo = (UserAllInfo) runRankAdapter.getItem(position);
                        mPresenter.SingleSign(userAllInfo.getSigninurl(), view);
                    }
                });
            }
        });

    }

    @Override
    public void ShowLoadSuccess(final SigninInfo signinInfo) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (signinInfo == null) {
                    mPresenter.Loadxml(path, getApplicationContext());
                }
            }
        });

    }

    @Override
    public void ShowLoadError(final String msg) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogToastUtlis.Toast(msg, MainActivity.this);
                dialogToastUtlis.closeProgressDialog();
                finish();
            }
        });

    }

    @Override
    public void ShowToast(final String msg) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogToastUtlis.Toast(msg, MainActivity.this);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_master_shs:
                if (runRankAdapter != null && runRankAdapter.getArrayList() != null) {
                    mPresenter.AllSign(runRankAdapter.getArrayList());
                }
                break;
        }
    }
}
