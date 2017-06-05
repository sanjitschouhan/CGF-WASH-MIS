package in.collectivegood.dbsibycgf.profiles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.CheckInDbHelper;
import in.collectivegood.dbsibycgf.database.CheckInRecord;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;
import in.collectivegood.dbsibycgf.discussion.DiscussionActivity;

import static in.collectivegood.dbsibycgf.sync.SyncFunctions.Sync;

public class CCOtherActivity extends AppCompatActivity {
    private String ccUid;
    private CardView checkInCard;
    private CardView checkOutCard;
    private SharedPreferences preferences;
    private SchoolDbHelper schoolDbHelper;
    private CheckInDbHelper checkInDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccother);

        ccUid = getIntent().getExtras().getString("cc_uid");
        schoolDbHelper = new SchoolDbHelper(new DbHelper(this));
        checkInDbHelper = new CheckInDbHelper(new DbHelper(this));

        preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        checkInCard = (CardView) findViewById(R.id.cc_checkin);
        checkOutCard = (CardView) findViewById(R.id.cc_checkout);
        updateCheckInOptions();
    }

    void updateCheckInOptions() {
        String school_code = preferences.getString("school_code", null);
        if (school_code == null) {
            checkInCard.setVisibility(View.VISIBLE);
            checkOutCard.setVisibility(View.GONE);
        } else {
            checkInCard.setVisibility(View.GONE);
            checkOutCard.setVisibility(View.VISIBLE);
        }
    }

    public void openChat(View view) {
        Intent intent = new Intent(this, DiscussionActivity.class);
        startActivity(intent);
    }

    public void CheckIn(View view) {
        Cursor read = schoolDbHelper.read(Schemas.SchoolDatabaseEntry.UID_OF_CC, ccUid);
        if (read.getCount() > 0) {
            final ArrayList<String> schoolCodes = new ArrayList<>();
            final ArrayList<String> schoolNames = new ArrayList<>();
            while (read.moveToNext()) {
                String code = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.CODE));
                String name = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.NAME));

                schoolCodes.add(code);
                schoolNames.add(name);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select School To Check In")
                    .setItems(schoolNames.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("school_code", schoolCodes.get(which));
                            editor.putString("school_name", schoolNames.get(which));
                            editor.putLong("start_time", System.currentTimeMillis());
                            editor.apply();
                            updateCheckInOptions();
                            Toast.makeText(CCOtherActivity.this, "Checked in to " + schoolNames.get(which),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            builder.show();
        }
    }

    public void CheckOut(View view) {
        String schoolCode = preferences.getString("school_code", null);
        String schoolName = preferences.getString("school_name", null);
        long start_time = preferences.getLong("start_time", System.currentTimeMillis());
        long end_time = System.currentTimeMillis();
        CheckInRecord checkInRecord = new CheckInRecord(ccUid, schoolCode, start_time, end_time);
        checkInDbHelper.insert(checkInRecord);

        Sync();

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("school_code");
        editor.remove("school_name");
        editor.remove("start_time");
        editor.apply();
        updateCheckInOptions();
        Toast.makeText(this, "Checked out of " + schoolName, Toast.LENGTH_SHORT).show();

    }
}
