package in.collectivegood.dbsibycgf.calender;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.CCDbHelper;
import in.collectivegood.dbsibycgf.database.CCRecord;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.support.InfoProvider;
import in.collectivegood.dbsibycgf.support.UserTypes;

public class CalenderProfileActivity extends AppCompatActivity {
    private ArrayAdapter<CCRecord> ccRecordArrayAdapter;
    private ArrayList<CCRecord> ccList;
    private ListView listView;
    private String selectedState;
    private String storagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_profile);
        String email = InfoProvider.getCCData(this, Schemas.CCDatabaseEntry.EMAIL);
        final DatabaseReference user_type = FirebaseDatabase.getInstance()
                .getReference("user_types")
                .child(email.replaceAll("\\.", "(dot)"));
        user_type.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value.equals(UserTypes.USER_TYPE_CC)) {
                    openCalendar(InfoProvider.getCCState(CalenderProfileActivity.this), InfoProvider.getCcUID(CalenderProfileActivity.this));
                    finish();
                } else {
                    admin();
                    findViewById(R.id.add_event).setVisibility(View.VISIBLE);
                    findViewById(R.id.loading).setVisibility(View.GONE);
                    findViewById(R.id.spinnerstates).setVisibility(View.VISIBLE);
                    if (value.equals(UserTypes.USER_TYPE_ADMIN_AP)) {
                        storagePath = "tl/all";
                        findViewById(R.id.ap_button).setVisibility(View.GONE);
                    } else if (value.equals(UserTypes.USER_TYPE_ADMIN_TL)) {
                        storagePath = "ap/all";
                        findViewById(R.id.tl_button).setVisibility(View.GONE);
                    } else {
                        storagePath = "all";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void admin() {
        ccList = new ArrayList<>();
        ccRecordArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ccList);
        listView = (ListView) findViewById(R.id.cclist);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerstates);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    listView.setVisibility(View.GONE);
                    findViewById(R.id.common).setVisibility(View.GONE);
                    return;
                } else if (position == 1) {
                    selectedState = "TL";
                    listView.setVisibility(View.VISIBLE);
                    findViewById(R.id.common).setVisibility(View.GONE);
                } else if (position == 2) {
                    selectedState = "AP";
                    listView.setVisibility(View.VISIBLE);
                    findViewById(R.id.common).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.common).setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    return;
                }
                CCDbHelper dbHelper = new CCDbHelper(new DbHelper(CalenderProfileActivity.this));
                Cursor read = dbHelper.read(null, null);
                ccList.clear();
                while (read.moveToNext()) {
                    String Uid = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.UID));
                    String Name = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.NAME));
                    String Phone = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.PHONE));
                    String Email = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.EMAIL));
                    String ProjectCoordinator = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.PROJECT_COORDINATOR));
                    String ccState = InfoProvider.getCCState(CalenderProfileActivity.this, Uid);
                    if (ccState.equalsIgnoreCase(selectedState)) {
                        CCRecord ccRecord = new CCRecord(Uid, Name, Phone, Email, ProjectCoordinator);
                        ccList.add(ccRecord);
                    }
                }
                read.close();
                ccRecordArrayAdapter.notifyDataSetChanged();
                listView.setAdapter(ccRecordArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uid = ccList.get(position).getUid();
                openCalendar(selectedState, uid);
            }
        });

    }

    private void openCalendar(String state, String Uid) {
        Intent intent = new Intent(CalenderProfileActivity.this, CalendarActivity.class);
        intent.putExtra("state", state);
        intent.putExtra("uid", Uid);
        startActivity(intent);
    }

    public void openTelanganaEvents(View view) {
        openCalendar("tl", "all");
    }

    public void openAPEvents(View view) {
        openCalendar("ap", "all");
    }

    public void openCommonEvents(View view) {
        openCalendar("both", "all");
    }

    public void addEvent(View view) {

    }
}
