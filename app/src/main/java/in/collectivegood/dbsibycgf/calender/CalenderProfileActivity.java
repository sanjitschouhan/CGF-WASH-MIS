package in.collectivegood.dbsibycgf.calender;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.CCDbHelper;
import in.collectivegood.dbsibycgf.database.CCRecord;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.support.InfoProvider;

public class CalenderProfileActivity extends AppCompatActivity {
    ArrayList<CCRecord> cclist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_profile);
        cclist = new ArrayList<>();
        Spinner spinner = (Spinner) findViewById(R.id.spinnerstates);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedstate = null;
                if (position == 1) {
                    selectedstate = "TL";
                } else if (position == 2) {
                    selectedstate = "AP";
                } else {
                    return ;
                }
                CCDbHelper dbHelper = new CCDbHelper(new DbHelper(CalenderProfileActivity.this));
                Cursor read = dbHelper.read(null, null);
                cclist.clear();
                while (read.moveToNext()) {
                    String Uid = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.UID));
                    String Name = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.NAME));
                    String Phone = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.PHONE));
                    String Email = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.EMAIL));
                    String ProjectCoordinator = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.PROJECT_COORDINATOR));
                    String ccState = InfoProvider.getCCState(CalenderProfileActivity.this, Uid);
                    if(ccState.equalsIgnoreCase(selectedstate)){
                        CCRecord ccRecord=new CCRecord(Uid,Name,Phone,Email,ProjectCoordinator);
                        cclist.add(ccRecord);
                    }
                }
                read.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
