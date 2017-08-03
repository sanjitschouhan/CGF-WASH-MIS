package in.collectivegood.dbsibycgf.heps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.HEPSDataDbHelper;
import in.collectivegood.dbsibycgf.database.HEPSDataRecord;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;
import in.collectivegood.dbsibycgf.profiles.CCProfileActivity;

import static in.collectivegood.dbsibycgf.sync.SyncFunctions.SyncHEPSData;

public class HEPSDataFormActivity extends AppCompatActivity {

    private String schoolCode;
    private String schoolName;
    private String schoolAddress;
    private int waterSupply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hepsdata_form);

        Runtime.getRuntime().gc();

        schoolCode = getIntent().getExtras().getString("code");
        EditText maleTeachersEditText = (EditText) findViewById(R.id.male_teachers);

        waterSupply = 0;

        findViewById(R.id.toilets_layout).setVisibility(View.GONE);
        findViewById(R.id.toilets_separate_layout).setVisibility(View.GONE);
        findViewById(R.id.toilets_combined_layout).setVisibility(View.GONE);

        findViewById(R.id.urinals_layout).setVisibility(View.GONE);
        findViewById(R.id.urinals_separate_layout).setVisibility(View.GONE);
        findViewById(R.id.urinals_combined_layout).setVisibility(View.GONE);

        findViewById(R.id.tap_layout).setVisibility(View.GONE);

        setSchoolDetails();

        addListeners();

        loadPreviousData();

        maleTeachersEditText.requestFocus();
    }

    private void loadPreviousData() {
        final HEPSDataDbHelper dbHelper = new HEPSDataDbHelper(new DbHelper(this));

        Cursor cursor = dbHelper.read(Schemas.HEPSFormEntry.SCHOOL_CODE, schoolCode);

        cursor.moveToNext();
        setValueToIdFromDatabase(R.id.male_teachers, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.MALE_TEACHERS))));
        setValueToIdFromDatabase(R.id.female_teachers, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.FEMALE_TEACHERS))));

        setValueToIdFromDatabase(R.id.class_1_boys, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_1_BOYS))));
        setValueToIdFromDatabase(R.id.class_2_boys, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_2_BOYS))));
        setValueToIdFromDatabase(R.id.class_3_boys, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_3_BOYS))));
        setValueToIdFromDatabase(R.id.class_4_boys, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_4_BOYS))));
        setValueToIdFromDatabase(R.id.class_5_boys, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_5_BOYS))));

        setValueToIdFromDatabase(R.id.class_1_girls, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_1_GIRLS))));
        setValueToIdFromDatabase(R.id.class_2_girls, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_2_GIRLS))));
        setValueToIdFromDatabase(R.id.class_3_girls, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_3_GIRLS))));
        setValueToIdFromDatabase(R.id.class_4_girls, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_4_GIRLS))));
        setValueToIdFromDatabase(R.id.class_5_girls, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_5_GIRLS))));


        long boysToilet = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_BOYS));
        long boysToiletFunctioning = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_BOYS_FUNCTIONING));
        long girlsToilet = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_GIRLS));
        long girlsToiletFunctioning = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_GIRLS_FUNCTIONING));

        long totalToilets = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_TOTAL));
        long totalToiletsFunctioning = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_TOTAL_FUNCTIONING));

        if (boysToilet + girlsToilet + totalToilets == 0) {
            ((RadioButton) findViewById(R.id.radio_toilets_no)).toggle();
            hideToiletsLayout(new View(this));
        } else {
            ((RadioButton) findViewById(R.id.radio_toilets_yes)).toggle();
            showToiletsLayout(new View(this));

            if (boysToilet + girlsToilet == 0) {
                ((RadioButton) findViewById(R.id.radio_toilets_separate_no)).toggle();
                showCombinedLayout(new View(this));
                setValueToIdFromDatabase(R.id.toilets_total, String.valueOf(totalToilets));
                setValueToIdFromDatabase(R.id.toilets_total_functioning, String.valueOf(totalToiletsFunctioning));
            } else {
                ((RadioButton) findViewById(R.id.radio_toilets_separate_yes)).toggle();
                showSeparateLayout(new View(this));
                setValueToIdFromDatabase(R.id.toilets_separate_boys, String.valueOf(boysToilet));
                setValueToIdFromDatabase(R.id.toilets_separate_boys_functioning, String.valueOf(boysToiletFunctioning));
                setValueToIdFromDatabase(R.id.toilets_separate_girls, String.valueOf(girlsToilet));
                setValueToIdFromDatabase(R.id.toilets_separate_girls_functioning, String.valueOf(girlsToiletFunctioning));
            }
        }
        long boysUrinal = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_BOYS));
        long boysUrinalFunctioning = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_BOYS_FUNCTIONING));
        long girlsUrinal = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_GIRLS));
        long girlsUrinalFunctioning = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_GIRLS_FUNCTIONING));

        long totalUrinals = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_TOTAL));
        long totalUrinalsFunctioning = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_TOTAL_FUNCTIONING));

        if (boysUrinal + girlsUrinal + totalUrinals == 0) {
            ((RadioButton) findViewById(R.id.radio_urinals_no)).toggle();
            hideUrinalsLayout(new View(this));
        } else {
            ((RadioButton) findViewById(R.id.radio_urinals_yes)).toggle();
            showUrinalsLayout(new View(this));

            if (boysUrinal + girlsUrinal == 0) {
                ((RadioButton) findViewById(R.id.radio_urinals_separate_no)).toggle();
                showUrinalsCombinedLayout(new View(this));
                setValueToIdFromDatabase(R.id.urinals_total, String.valueOf(totalUrinals));
                setValueToIdFromDatabase(R.id.urinals_total_functioning, String.valueOf(totalUrinalsFunctioning));
            } else {
                ((RadioButton) findViewById(R.id.radio_urinals_separate_yes)).toggle();
                showUrinalsSeparateLayout(new View(this));
                setValueToIdFromDatabase(R.id.urinals_separate_boys, String.valueOf(boysUrinal));
                setValueToIdFromDatabase(R.id.urinals_separate_boys_functioning, String.valueOf(boysUrinalFunctioning));
                setValueToIdFromDatabase(R.id.urinals_separate_girls, String.valueOf(girlsUrinal));
                setValueToIdFromDatabase(R.id.urinals_separate_girls_functioning, String.valueOf(girlsUrinalFunctioning));
            }
        }


        waterSupply = cursor.getInt(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.WATER_SOURCE));
        if ((waterSupply & 1) != 0) {
            ((CheckBox) findViewById(R.id.check_water_borewell)).setChecked(true);
        }
        if ((waterSupply & 10) != 0) {
            ((CheckBox) findViewById(R.id.check_water_govt_supply)).setChecked(true);
        }

        long taps = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.NO_OF_TAPS));
        if (taps == 0) {
            ((RadioButton) findViewById(R.id.radio_water_active_no)).toggle();
            hideTapLayout(new View(this));
        } else {
            ((RadioButton) findViewById(R.id.radio_water_active_yes)).toggle();
            showTapLayout(new View(this));
            setValueToIdFromDatabase(R.id.taps, String.valueOf(taps));
        }

        cursor.close();
    }

    private void setValueToIdFromDatabase(int id, String value) {
        ((EditText) findViewById(id)).setText(value);
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

        addWatcherToIds(new int[]{R.id.urinals_separate_boys}, R.id.urinals_separate_boys_functioning);
        addWatcherToIds(new int[]{R.id.urinals_separate_girls}, R.id.urinals_separate_girls_functioning);
        addWatcherToIds(new int[]{R.id.urinals_total}, R.id.urinals_total_functioning);
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
        schoolName = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.NAME));
        schoolAddress = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.VILLAGE)) + ", " +
                read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.DISTRICT)) + ", " +
                read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.STATE));

        read.close();

        ((TextView) findViewById(R.id.school_name)).setText(schoolName);
        ((TextView) findViewById(R.id.school_code)).setText(schoolCode);
        ((TextView) findViewById(R.id.school_address)).setText(schoolAddress);
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
            ((EditText) findViewById(id)).setText("");
        }

    }

    public void showUrinalsLayout(View view) {
        findViewById(R.id.urinals_layout).setVisibility(View.VISIBLE);
    }

    public void hideUrinalsLayout(View view) {
        findViewById(R.id.urinals_layout).setVisibility(View.GONE);
        reset(new int[]{
                R.id.urinals_separate_boys,
                R.id.urinals_separate_boys_functioning,
                R.id.urinals_separate_girls,
                R.id.urinals_separate_girls_functioning,
                R.id.urinals_total,
                R.id.urinals_total_functioning
        });
    }

    public void showUrinalsSeparateLayout(View view) {
        findViewById(R.id.urinals_separate_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.urinals_combined_layout).setVisibility(View.GONE);
        reset(new int[]{
                R.id.urinals_total,
                R.id.urinals_total_functioning
        });
    }

    public void showUrinalsCombinedLayout(View view) {
        findViewById(R.id.urinals_combined_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.urinals_separate_layout).setVisibility(View.GONE);
        reset(new int[]{
                R.id.urinals_separate_boys,
                R.id.urinals_separate_boys_functioning,
                R.id.urinals_separate_girls,
                R.id.urinals_separate_girls_functioning
        });
    }

    public void toggleWaterSupplyBorewell(View view) {
        waterSupply ^= 1;
    }

    public void toggleWaterSupplyGovtSupply(View view) {
        waterSupply ^= 10;
    }

    public void showTapLayout(View view) {
        findViewById(R.id.tap_layout).setVisibility(View.VISIBLE);
    }

    public void hideTapLayout(View view) {
        findViewById(R.id.tap_layout).setVisibility(View.GONE);
        reset(new int[]{R.id.taps});
    }

    public void saveForm(View view) {
        HEPSDataRecord record = new HEPSDataRecord(
                CCProfileActivity.getCcUID(),
                schoolCode, schoolName, schoolAddress,
                getValue(R.id.male_teachers),
                getValue(R.id.female_teachers),
                new long[]{
                        getValue(R.id.class_1_boys),
                        getValue(R.id.class_2_boys),
                        getValue(R.id.class_3_boys),
                        getValue(R.id.class_4_boys),
                        getValue(R.id.class_5_boys)
                },
                new long[]{
                        getValue(R.id.class_1_girls),
                        getValue(R.id.class_2_girls),
                        getValue(R.id.class_3_girls),
                        getValue(R.id.class_4_girls),
                        getValue(R.id.class_5_girls)
                },
                getValue(R.id.toilets_separate_boys),
                getValue(R.id.toilets_separate_boys_functioning),
                getValue(R.id.toilets_separate_girls),
                getValue(R.id.toilets_separate_girls_functioning),
                getValue(R.id.toilets_total),
                getValue(R.id.toilets_total_functioning),
                getValue(R.id.urinals_separate_boys),
                getValue(R.id.urinals_separate_boys_functioning),
                getValue(R.id.urinals_separate_girls),
                getValue(R.id.urinals_separate_girls_functioning),
                getValue(R.id.urinals_total),
                getValue(R.id.urinals_total_functioning),
                waterSupply,
                getValue(R.id.taps)
        );

        HEPSDataDbHelper dbHelper = new HEPSDataDbHelper(new DbHelper(this));
        dbHelper.insert(record);

        SyncHEPSData(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_successful)
                .setMessage(R.string.save_successful_msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false);
        builder.show();
    }

    public long getValue(int id) {
        EditText editText = (EditText) findViewById(id);
        String valueString = editText.getText().toString().trim();
        long value = 0;
        try {
            value = Long.parseLong(valueString);
        } catch (Exception ignored) {
        }

        return value;
    }
}
