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
import in.collectivegood.dbsibycgf.dashboard.DashboardActivity;
import in.collectivegood.dbsibycgf.database.CCDbHelper;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;
import in.collectivegood.dbsibycgf.support.JavaMail;

public class CCProfileActivity extends AppCompatActivity {

    private static String ccUID;
    private int noOfSchools;
    private String mandal;
    private String district;
    private String email;
    private String name;
    private String reportingManager;
    private String phone;
    private TextView nameView;
    private TextView phoneView;
    private TextView emailView;
    private TextView reportingManagerView;
    private TextView noOfSchoolsView;
    private TextView mandalView;
    private TextView districtView;
    private TextView uidView;

    public static String getCcUID() {
        return ccUID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cc_profile);

        getSupportActionBar().setElevation(0);

        init();
        getCCData();
        getSchoolData();

        emailView.setText(email);
        nameView.setText(name);
        phoneView.setText(phone);
        reportingManagerView.setText(reportingManager);
        uidView.setText(ccUID);

        noOfSchoolsView.setText(String.valueOf(noOfSchools));
        mandalView.setText(mandal);
        districtView.setText(district);
    }

    /**
     * Get all schools information for current Cluster Coordinator
     */
    private void getSchoolData() {
        SchoolDbHelper schoolDbHelper = new SchoolDbHelper(new DbHelper(this));
        Cursor read = schoolDbHelper.read(Schemas.SchoolDatabaseEntry.UID_OF_CC, ccUID);
        noOfSchools = read.getCount();
        read.moveToNext();
        mandal = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.BLOCK));
        district = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.DISTRICT));
        read.close();
    }

    /**
     * Get information about current cluster coordinator
     */
    private void getCCData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        email = currentUser.getEmail();

//        JavaMail.mail(this, email);

        CCDbHelper ccDbHelper = new CCDbHelper(new DbHelper(this));
        Cursor read = ccDbHelper.read(Schemas.CCDatabaseEntry.EMAIL, email);
        read.moveToNext();
        name = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.NAME));
        reportingManager = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.PROJECT_COORDINATOR));
        ccUID = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.UID));
        phone = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.PHONE));
        read.close();
    }

    /**
     * Initialise the references to views
     */
    private void init() {
        nameView = (TextView) findViewById(R.id.cc_name);
        phoneView = (TextView) findViewById(R.id.cc_phone);
        emailView = (TextView) findViewById(R.id.cc_email);
        reportingManagerView = (TextView) findViewById(R.id.cc_reporting_manager);
        noOfSchoolsView = (TextView) findViewById(R.id.cc_no_of_schools);
        mandalView = (TextView) findViewById(R.id.cc_mandal);
        districtView = (TextView) findViewById(R.id.cc_district);
        uidView = (TextView) findViewById(R.id.cc_uid);
    }

    /**
     * Open the activity screen
     */
    public void OpenActivityScreen(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
