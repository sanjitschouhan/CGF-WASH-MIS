package in.collectivegood.dbsibycgf.calender;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TextView;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;
import in.collectivegood.dbsibycgf.profiles.CCProfileActivity;

public class CalendarActivity extends AppCompatActivity {
    CalendarView calendarView;
    TextView T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = (CalendarView) findViewById(R.id.calenderview);
        T = (TextView) findViewById(R.id.Eventname);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                T.setText("Event Changed" + year + month);
            }
        });


        /*
          TODO: Create a CalendarItem class with required data members
          TODO: Create constructor and getters using Alt+Insert in that class
          TODO: 3/8/17 Retrieve calendar data from /calendar/<state>/<ccuid> for all cc,
          TODO:      ccuid can be found by CCProfileActivity.getCcUID()
          TODO:     state can be retrieved from InfoProvider.getCCState(CCProfileActivity.getCcUID()) function below
          TODO: 3/8/17 Retrive calendar data from /calendar/all for all cc
          */

    }
}
