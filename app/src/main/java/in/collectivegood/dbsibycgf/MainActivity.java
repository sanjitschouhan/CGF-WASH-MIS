package in.collectivegood.dbsibycgf;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public void getStarted(View view) {
        Resources res2 = getApplicationContext().getResources();
        DisplayMetrics dm2 = res2.getDisplayMetrics();
        android.content.res.Configuration conf2 = res2.getConfiguration();
        conf2.locale = new Locale("en");
        res2.updateConfiguration(conf2, dm2);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void getStartedTelugu(View view) {
        Resources res2 = getApplicationContext().getResources();
        DisplayMetrics dm2 = res2.getDisplayMetrics();
        android.content.res.Configuration conf2 = res2.getConfiguration();
        conf2.locale = new Locale("te");
        res2.updateConfiguration(conf2, dm2);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
