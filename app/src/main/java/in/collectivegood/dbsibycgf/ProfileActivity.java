package in.collectivegood.dbsibycgf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.collectivegood.dbsibycgf.gallery.GalleryMainActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        startActivity(new Intent(this,GalleryMainActivity.class));
        finish();
    }
}
