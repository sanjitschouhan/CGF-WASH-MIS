package in.collectivegood.dbsibycgf.gis;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;
import in.collectivegood.dbsibycgf.database.SchoolRecord;
import in.collectivegood.dbsibycgf.heps.SchoolListGridAdapter;
import in.collectivegood.dbsibycgf.support.InfoProvider;

public class GISActivity extends AppCompatActivity {
    private EditText searchQueryInputEditText;
    private ListView schoolListView;
    private SchoolListGridAdapter adapter;
    private ArrayList<SchoolRecord> schoolList;
    private SchoolDbHelper schoolDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gis);

        initialiseReferences();
        updateSchoolList(null);
        addListeners();
    }

    private void initialiseReferences() {

        searchQueryInputEditText = (EditText) findViewById(R.id.search_query_input);
        schoolListView = (ListView) findViewById(R.id.school_list);

        schoolList = new ArrayList<>();
        adapter = new SchoolListGridAdapter(this, schoolList);

        schoolListView.setAdapter(adapter);

        schoolDbHelper = new SchoolDbHelper(new DbHelper(this));
    }

    private void addListeners() {
        searchQueryInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSchoolList(s.toString().trim().replaceAll(" ", "%"));
            }
        });

        schoolListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolRecord schoolRecord = schoolList.get(position);
                Intent intent = new Intent(GISActivity.this, MapActivity.class);
                intent.putExtra("code", schoolRecord.getCode());
                startActivity(intent);
            }
        });
    }

    private void updateSchoolList(String query) {
        schoolList.clear();

        Cursor read = schoolDbHelper.search(query, InfoProvider.getCcUID(this));

        while (read.moveToNext()) {
            String name = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.NAME));
            String code = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.CODE));
            String block = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.BLOCK));
            String village = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.VILLAGE));
            String email = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.EMAIL));
            String district = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.DISTRICT));
            String state = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.STATE));

            schoolList.add(new SchoolRecord(
                    code,
                    block,
                    village,
                    name,
                    email,
                    state,
                    district,
                    InfoProvider.getCcUID(this))
            );
        }

        read.close();

        adapter.notifyDataSetChanged();
    }
}

