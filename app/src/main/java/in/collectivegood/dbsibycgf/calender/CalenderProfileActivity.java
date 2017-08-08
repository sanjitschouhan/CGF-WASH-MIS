package in.collectivegood.dbsibycgf.calender;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

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
    private int hour;
    private int minute;
    private int dayOfMonth;
    private int year;
    private int month;
    private Button dateButton;
    private Button timeButton;
    private String months[];
    private EditText eventName;
    private EditText eventDetail;
    private Dialog addEventDailog;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

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
                        storagePath = "AP/all";
                        findViewById(R.id.tl_button).setVisibility(View.GONE);
                    } else if (value.equals(UserTypes.USER_TYPE_ADMIN_TL)) {
                        storagePath = "TL/all";
                        findViewById(R.id.ap_button).setVisibility(View.GONE);
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
        generateNewEventDialog();
        initSetListeners();
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
        openCalendar("TL", "all");
    }

    public void openAPEvents(View view) {
        openCalendar("AP", "all");
    }

    public void openCommonEvents(View view) {
        openCalendar("both", "all");
    }

    public void addEvent(View view) {
        Date date = new Date(System.currentTimeMillis());
        year = date.getYear() + 1900;
        month = date.getMonth();
        dayOfMonth = date.getDate();
        hour = date.getHours();
        minute = date.getMinutes();
        dateButton.setText(String.format("%02d %s %4d", dayOfMonth, months[month - 1], year));
        timeButton.setText(String.format("%02d:%02d", hour, minute));

        addEventDailog.show();
    }

    private void generateNewEventDialog() {
        Date date = new Date(System.currentTimeMillis());
        year = date.getYear() + 1900;
        month = date.getMonth();
        dayOfMonth = date.getDate();
        hour = date.getHours();
        minute = date.getMinutes();

        months = new String[]{
                "Jan", "Feb", "Mar",
                "Apr", "May", "Jun",
                "Jul", "Aug", "Sept",
                "Oct", "Nov", "Dec"
        };

        dateButton = new Button(this);
        dateButton.setTextColor(getResources().getColor(R.color.colorAccent));
        dateButton.setBackground(getResources().getDrawable(android.R.drawable.screen_background_light_transparent));
        dateButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        timeButton = new Button(this);
        timeButton.setTextColor(getResources().getColor(R.color.colorAccent));
        timeButton.setBackground(getResources().getDrawable(android.R.drawable.screen_background_light_transparent));
        timeButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        eventName = new EditText(this);
        eventName.setHint(R.string.event_name);
        eventName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        eventName.setSingleLine(true);

        eventDetail = new EditText(this);
        eventDetail.setHint(R.string.event_detail);
        eventDetail.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        eventDetail.setSingleLine(false);
        eventDetail.setGravity(Gravity.TOP);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(50, 10, 50, 10);

        linearLayout.addView(eventName);
        linearLayout.addView(eventDetail);
        linearLayout.addView(dateButton);
        linearLayout.addView(timeButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.new_event);
        builder.setView(linearLayout);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = eventName.getText().toString().trim();
                String detail = eventDetail.getText().toString().trim();
                saveEvent(name, detail);
                eventName.setText("");
                eventDetail.setText("");
            }
        });

        addEventDailog = builder.create();


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picDate();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });
    }

    private void initSetListeners() {
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int h, int m) {
                hour = h;
                minute = m;
                timeButton.setText(String.format("%02d:%02d", hour, minute));
            }
        };
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                year = y;
                month = m;
                dayOfMonth = d;
                dateButton.setText(String.format("%02d %s %4d", dayOfMonth, months[month - 1], year));
            }
        };
    }

    private void pickTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        timePickerDialog.show();
    }

    private void picDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void saveEvent(String title, String detail) {
        Date date = new Date();
        date.setYear(year - 1900);
        date.setMonth(month);
        date.setDate(dayOfMonth);
        date.setHours(hour);
        date.setMinutes(minute);
        CalendarItem calendarItem = new CalendarItem(date.getTime(), title, detail);
        FirebaseDatabase.getInstance().getReference("calendar")
                .child(storagePath)
                .child(String.valueOf(date.getTime()))
                .setValue(calendarItem);
    }


}
