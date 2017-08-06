package in.collectivegood.dbsibycgf.calender;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Date;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.support.InfoProvider;
import in.collectivegood.dbsibycgf.support.UserTypes;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private int year;
    private int month;
    private int dayOfMonth;
    private int hour;
    private int minute;
    private String months[];

    private AlertDialog addEventDailog;
    private EditText eventName;
    private EditText eventDetail;
    private Button dateButton;
    private Button timeButton;

    private String state;
    private String uid;

    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private ArrayList<CalendarItem> ccEvents;
    private ArrayList<CalendarItem> commonEvents;
    private ArrayList<CalendarItem> localEvents;

    private ListView eventListView;
    private ArrayList<CalendarItem> selectedDayEvents;
    private ArrayAdapter<CalendarItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Bundle extras = getIntent().getExtras();
        state = extras.getString("state");
        uid = extras.getString("uid");

        calendarView = (MaterialCalendarView) findViewById(R.id.calendar_view);
        calendarView.setSelectedDate(new Date());
        eventListView = (ListView) findViewById(R.id.event_list);

        ccEvents = new ArrayList<>();
        commonEvents = new ArrayList<>();
        localEvents = new ArrayList<>();
        selectedDayEvents = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedDayEvents);
        eventListView.setAdapter(adapter);
        updateEventList();

        getCalendarData(state, uid, ccEvents);
        getCalendarData(state, "all", localEvents);
        getCommonCalendarData(commonEvents);

        String email = InfoProvider.getCCData(this, Schemas.CCDatabaseEntry.EMAIL);
        final DatabaseReference user_type = FirebaseDatabase.getInstance()
                .getReference("user_types")
                .child(email.replaceAll("\\.", "(dot)"));
        user_type.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value.equals(UserTypes.USER_TYPE_CC)) {
                    (findViewById(R.id.add_cc_event)).setVisibility(View.VISIBLE);
                    generateNewEventDialog();
                    initSetListeners();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                updateEventList();
            }
        });


    }

    private void updateEventList() {
        selectedDayEvents.clear();
        Date date = calendarView.getSelectedDate().getDate();
        ArrayList<CalendarItem>[] arrayLists = new ArrayList[]{commonEvents, localEvents, ccEvents};
        for (ArrayList<CalendarItem> list : arrayLists) {
            for (CalendarItem calendarItem : list) {
                Date day = new Date(calendarItem.getDate());
                if (date.getDate() == day.getDate()) {
                    if (date.getMonth() == day.getMonth()) {
                        if (date.getYear() == day.getYear()) {
                            selectedDayEvents.add(calendarItem);
                        }
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void updateCalendarView() {
        calendarView.removeDecorators();

        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                Date date1 = day.getDate();
                for (CalendarItem calendarItem : ccEvents) {
                    Date date = new Date(calendarItem.getDate());
                    if (date.getDate() == date1.getDate()) {
                        if (date.getMonth() == date1.getMonth()) {
                            if (date.getYear() == date1.getYear()) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.calendar_cc_event));
            }
        });
        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                Date date1 = day.getDate();
                for (CalendarItem calendarItem : commonEvents) {
                    Date date = new Date(calendarItem.getDate());
                    if (date.getDate() == date1.getDate()) {
                        if (date.getMonth() == date1.getMonth()) {
                            if (date.getYear() == date1.getYear()) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.calendar_common_event));
            }
        });
        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                Date date1 = day.getDate();
                for (CalendarItem calendarItem : localEvents) {
                    Date date = new Date(calendarItem.getDate());
                    if (date.getDate() == date1.getDate()) {
                        if (date.getMonth() == date1.getMonth()) {
                            if (date.getYear() == date1.getYear()) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.calendar_local_event));
            }
        });
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
                updateCalendarView();
                updateEventList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addCCEvent(View view) {
        CalendarDay selectedDate = calendarView.getSelectedDate();
        year = selectedDate.getYear();
        month = selectedDate.getMonth();
        dayOfMonth = selectedDate.getDay();
        Date date = new Date(System.currentTimeMillis());
        hour = date.getHours();
        minute = date.getMinutes();
        dateButton.setText(String.format("%02d %s %4d", dayOfMonth, months[month - 1], year));
        timeButton.setText(String.format("%02d:%02d", hour, minute));

        addEventDailog.show();
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
                .child(state)
                .child(uid)
                .child(String.valueOf(date.getTime()))
                .setValue(calendarItem);
        ccEvents.add(calendarItem);
        updateCalendarView();
        updateEventList();
    }

    private void picDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, dayOfMonth);
        datePickerDialog.show();
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

}
