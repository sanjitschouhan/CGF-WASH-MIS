package in.collectivegood.dbsibycgf.heps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;

public class HEPSDataFormActivity extends AppCompatActivity {

    private String schoolCode;
    private EditText maleTeachersEditText;
    private EditText femaleTeachersEditText;
    private TextView totalTeachersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hepsdata_form);

        schoolCode = getIntent().getExtras().getString("code");
        maleTeachersEditText = (EditText) findViewById(R.id.male_teachers);
        femaleTeachersEditText = (EditText) findViewById(R.id.female_teachers);
        totalTeachersTextView = (TextView) findViewById(R.id.total_teachers);

        findViewById(R.id.toilets_layout).setVisibility(View.GONE);
        findViewById(R.id.toilets_separate_layout).setVisibility(View.GONE);
        findViewById(R.id.toilets_combined_layout).setVisibility(View.GONE);

        setSchoolDetails();

        addListeners();

        maleTeachersEditText.requestFocus();
    }

    private void addListeners() {
        addWatcherToIds(new int[]{R.id.male_teachers, R.id.female_teachers}, R.id.total_teachers);
        addWatcherToIds(new int[]{R.id.class_1_boys, R.id.class_1_girls}, R.id.class_1_total);
        addWatcherToIds(new int[]{R.id.class_2_boys, R.id.class_2_girls}, R.id.class_2_total);
        addWatcherToIds(new int[]{R.id.class_3_boys, R.id.class_3_girls}, R.id.class_3_total);
        addWatcherToIds(new int[]{R.id.class_4_boys, R.id.class_4_girls}, R.id.class_4_total);
        addWatcherToIds(new int[]{R.id.class_5_boys, R.id.class_5_girls}, R.id.class_5_total);
        addWatcherToIds(new int[]{
                        R.id.class_1_boys,
                        R.id.class_2_boys,
                        R.id.class_3_boys,
                        R.id.class_4_boys,
                        R.id.class_5_boys
                },
                R.id.class_total_boys);
        addWatcherToIds(new int[]{
                        R.id.class_1_girls,
                        R.id.class_2_girls,
                        R.id.class_3_girls,
                        R.id.class_4_girls,
                        R.id.class_5_girls
                },
                R.id.class_total_girls);
        addWatcherToIds(new int[]{
                        R.id.class_1_boys,
                        R.id.class_2_boys,
                        R.id.class_3_boys,
                        R.id.class_4_boys,
                        R.id.class_5_boys,
                        R.id.class_1_girls,
                        R.id.class_2_girls,
                        R.id.class_3_girls,
                        R.id.class_4_girls,
                        R.id.class_5_girls
                },
                R.id.class_total_total);
        addWatcherToIds(new int[]{R.id.toilets_separate_boys}, R.id.toilets_separate_boys_functioning);
        addWatcherToIds(new int[]{R.id.toilets_separate_girls}, R.id.toilets_separate_girls_functioning);
        addWatcherToIds(new int[]{R.id.toilets_total}, R.id.toilets_total_functioning);
    }

    private void addWatcherToIds(int[] ids, int resultId) {
        ArrayList<EditText> editTexts = new ArrayList<>();
        for (int id : ids) {
            editTexts.add((EditText) findViewById(id));
        }
        TextView resultView = (TextView) findViewById(resultId);

        addWatcher(editTexts, resultView);
    }

    private void addWatcher(final ArrayList<EditText> edits, final TextView resultView) {
        TextWatcher teachersTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                long total = 0;
                for (EditText edit : edits) {
                    String valueText = edit.getText().toString().trim();
                    try {
                        total += Long.parseLong(valueText);
                    } catch (Exception ignored) {
                    }
                }

                resultView.setText(String.valueOf(total));
                if (total == 0) {
                    resultView.setEnabled(false);
                } else {
                    resultView.setEnabled(true);
                }

            }
        };

        for (EditText edit : edits) {
            edit.addTextChangedListener(teachersTextWatcher);
        }
    }


    private void setSchoolDetails() {
        SchoolDbHelper schoolDbHelper = new SchoolDbHelper(new DbHelper(this));
        Cursor read = schoolDbHelper.read(Schemas.SchoolDatabaseEntry.CODE, schoolCode);
        read.moveToNext();
        String name = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.NAME));
        String address = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.VILLAGE)) + ", " +
                read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.DISTRICT)) + ", " +
                read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.STATE));

        read.close();

        ((TextView) findViewById(R.id.school_name)).setText(name);
        ((TextView) findViewById(R.id.school_code)).setText(schoolCode);
        ((TextView) findViewById(R.id.school_address)).setText(address);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.form_back_title)
                .setMessage(R.string.form_back_message)
                .setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Runtime.getRuntime().gc();
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    public void showToiletsLayout(View view) {
        findViewById(R.id.toilets_layout).setVisibility(View.VISIBLE);
    }

    public void hideToiletsLayout(View view) {
        findViewById(R.id.toilets_layout).setVisibility(View.GONE);
        reset(new int[]{
                R.id.toilets_separate_boys,
                R.id.toilets_separate_boys_functioning,
                R.id.toilets_separate_girls,
                R.id.toilets_separate_girls_functioning,
                R.id.toilets_total,
                R.id.toilets_total_functioning
        });
    }

    public void showSeparateLayout(View view) {
        findViewById(R.id.toilets_separate_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.toilets_combined_layout).setVisibility(View.GONE);
        reset(new int[]{
                R.id.toilets_total,
                R.id.toilets_total_functioning
        });
    }

    public void showCombinedLayout(View view) {
        findViewById(R.id.toilets_combined_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.toilets_separate_layout).setVisibility(View.GONE);
        reset(new int[]{
                R.id.toilets_separate_boys,
                R.id.toilets_separate_boys_functioning,
                R.id.toilets_separate_girls,
                R.id.toilets_separate_girls_functioning
        });
    }

    private void reset(int[] ids) {
        for (int id : ids) {
            ((EditText) findViewById(id)).setText("0");
        }
    }
}
