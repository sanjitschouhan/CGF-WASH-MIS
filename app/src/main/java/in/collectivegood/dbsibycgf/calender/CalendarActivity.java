package in.collectivegood.dbsibycgf.calender;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
        T=(TextView)findViewById(R.id.Event);
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.setText("Event Changed"+calendarView.getDate());
            }
        });

    }
}
