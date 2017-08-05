package in.collectivegood.dbsibycgf.calender;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

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

    public void addccevent(View view) {

    }
}
