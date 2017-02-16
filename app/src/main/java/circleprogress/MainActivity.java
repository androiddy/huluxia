package circleprogress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.hlxyjqd.yjqd.R;
import com.hlxyjqd.yjqd.updata.manager.UpdateManager;

public class MainActivity extends AppCompatActivity {
    private CircleProgress mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);
        UpdateManager updateManager = new UpdateManager(this);
        updateManager.checkUpdate(this,false);
        mProgressView = (CircleProgress) findViewById(R.id.progress);
        mProgressView.startAnim();
       // onSuccess(new File(getExternalCacheDir().getAbsolutePath(),"123.apk").getAbsolutePath());
    }

    public void huluxia(View v){
        Intent intent = new Intent(this, com.hlxyjqd.yjqd.View.Impl.MainActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("PATH", "/data/data/com.huluxia.gametools/shared_prefs/config.xml");
        bundle.putString("INDEX", "hlxvar");
        intent.putExtras(bundle);
        startActivity(intent);
       /* PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage("com.huluxia.gametools");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }
    public void huluxia3(View v){
        Intent intent = new Intent(this, com.hlxyjqd.yjqd.View.Impl.MainActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("PATH", "/data/data/com.huati/shared_prefs/config.xml");
        bundle.putString("INDEX", "hlx3var");
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
