package in.collectivegood.dbsibycgf.profiles;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.activities_dashboard.Dashboard_layout;
import in.collectivegood.dbsibycgf.calender.CalendarActivity;
import in.collectivegood.dbsibycgf.database.CCDbHelper;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;
import in.collectivegood.dbsibycgf.gallery.GalleryMainActivity;

public class CCProfileActivity extends AppCompatActivity {

    private String ccUID;
    private TextView nameView;
    private TextView phoneView;
    private TextView emailView;
    private CCDbHelper ccDbHelper;
    private SchoolDbHelper schoolDbHelper;
    private TextView reportingManagerView;
    private TextView noOfSchoolsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cc_profile);

        nameView = (TextView) findViewById(R.id.cc_name);
        phoneView = (TextView) findViewById(R.id.cc_phone);
        emailView = (TextView) findViewById(R.id.cc_email);
        reportingManagerView = (TextView) findViewById(R.id.cc_reporting_manager);
        noOfSchoolsView = (TextView) findViewById(R.id.cc_no_of_schools);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser.getEmail();

        ccDbHelper = new CCDbHelper(new DbHelper(this));
        schoolDbHelper = new SchoolDbHelper(new DbHelper(this));
        Cursor read = ccDbHelper.read(Schemas.CCDatabaseEntry.EMAIL, email);
        read.moveToNext();
        String name = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.NAME));
        String reportingManager = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.PROJECT_COORDINATOR));
        ccUID = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.UID));
        String phone = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.PHONE));

        emailView.setText(email);
        nameView.setText(name);
        phoneView.setText(phone);
        reportingManagerView.setText(reportingManager);

        Cursor read1 = schoolDbHelper.read(Schemas.SchoolDatabaseEntry.UID_OF_CC, ccUID);
        int noOfSchools = read1.getCount();

        noOfSchoolsView.setText(String.valueOf(noOfSchools));
    }

    public void openGallery(View view) {
        Intent intent = new Intent(this, GalleryMainActivity.class);
        startActivity(intent);
    }

    public void openOtherCC(View view) {
        Intent intent = new Intent(this, CCOtherActivity.class);
        intent.putExtra("cc_uid", ccUID);
        startActivity(intent);
    }

    public void openCalendar(View view) {
        Intent intent= new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void activityscreenopen(View view) {
        Intent intent=new Intent(this, Dashboard_layout.class);
        startActivity(intent);
    }
}
