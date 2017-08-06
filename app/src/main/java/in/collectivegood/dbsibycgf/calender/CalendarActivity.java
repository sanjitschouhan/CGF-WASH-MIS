package in.collectivegood.dbsibycgf.calender;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import in.collectivegood.dbsibycgf.R;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private int year;
    private int month;
    private int dayOfMonth;
    private int hour;
    private int minute;
    private String months[];

    private LinearLayout linearLayout;
    private EditText eventName;
    private EditText eventDetail;
    private Button dateButton;
    private Button timeButton;

    private String state;
    private String uid;

    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Bundle extras = getIntent().getExtras();
        state = extras.getString("state");
        uid = extras.getString("uid");
        calendarView = (CalendarView) findViewById(R.id.calendar_view);

        generateNewEventDialogElements();
        initSetListeners();
    }


    private void getCalendarData(String state, String uid, ArrayList<CalendarItem> list) {
        getCalendarData("calendar/" + state + "/" + uid, list);
    }

    private void getCommonCalendarData(ArrayList<CalendarItem> list) {
        getCalendarData("calendar/all", list);
    }

    private void getCalendarData(String path, final ArrayList<CalendarItem> list) {
        DatabaseReference calendar = FirebaseDatabase.getInstance().getReference(path);
        calendar.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CalendarItem item = snapshot.getValue(CalendarItem.class);
                    list.add(item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addCCEvent(View view) {
        long timestamp = calendarView.getDate();
        Date date = new Date(timestamp);
        year = date.getYear() + 1900;
        month = date.getMonth();
        dayOfMonth = date.getDate();
        hour = 10;
        minute = 0;

        dateButton.setText(String.format("%02d %s %4d", dayOfMonth, months[month - 1], year));
        timeButton.setText(String.format("%02d:%02d", hour, minute));

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
            }
        });

        builder.show();
    }

    private void saveEvent(String title, String detail) {
        Date date = new Date();
        date.setYear(year);
        date.setMonth(month);
        date.setDate(dayOfMonth);
        date.setHours(hour);
        date.setMinutes(minute);
        CalendarItem calendarItem = new CalendarItem(date.getTime(), title, detail);
        FirebaseDatabase.getInstance().getReference("calendar")
                .child(state)
                .child(uid)
                .child(String.valueOf(date.getTime()))
                .setValue(calendarItem);
    }

    private void picDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void generateNewEventDialogElements() {
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

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(50, 10, 50, 10);

        linearLayout.addView(eventName);
        linearLayout.addView(eventDetail);
        linearLayout.addView(dateButton);
        linearLayout.addView(timeButton);


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

}
