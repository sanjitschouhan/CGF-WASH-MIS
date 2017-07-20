package in.collectivegood.dbsibycgf.calender;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TextView;

import in.collectivegood.dbsibycgf.R;

public class CalendarActivity extends AppCompatActivity {
CalendarView calendarView;
    TextView T;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView=(CalendarView)findViewById(R.id.calenderview);
        T=(TextView)findViewById(R.id.Eventname);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                T.setText("Event Changed"+year+month);
            }
        });

    }
}
