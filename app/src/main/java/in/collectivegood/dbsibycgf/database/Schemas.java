package in.collectivegood.dbsibycgf.database;

import android.provider.BaseColumns;

public final class Schemas {

    private Schemas() {
    }

    public static class SchoolDatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "school_database";
        public static final String BLOCK = "block";
        public static final String VILLAGE = "village";
        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String STATE = "state";
        public static final String DISTRICT = "district";
        public static final String UID_OF_CC = "uid_of_cc";
    }

    public static class CCDatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "cc_database";
        public static final String EMAIL = "email";
        public static final String NAME = "name";
        public static final String PHONE = "phone";
        public static final String UID = "uid";
        public static final String PROJECT_COORDINATOR = "project_coordinator";
    }

    public static class DiscussionDatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "discussion_database";
        public static final String NAME = "name";
        public static final String MESSAGE = "message";
        public static final String TIME = "time";
    }

    public static class CheckInEntry implements BaseColumns {
        public static final String TABLE_NAME = "check_in_database";
        public static final String UID_OF_CC = "uid_of_cc";
        public static final String SCHOOL_CODE = "school_code";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String CHECK_IN_DISTANCE = "check_in_distance";
        public static final String CHECK_OUT_DISTANCE = "check_out_distance";
    }

    public static class HEPSFormEntry implements BaseColumns {
        public static final String TABLE_NAME = "heps_forms";

        public static final String IS_SYNCED = "is_synced";

        public static final String UID_OF_CC = "uid_of_cc";
        public static final String SCHOOL_CODE = "school_code";
        public static final String SCHOOL_NAME = "school_name";
        public static final String SCHOOL_ADDRESS = "school_address";

        public static final String MALE_TEACHERS = "male_teachers";
        public static final String FEMALE_TEACHERS = "female_teachers";
        public static final String TOTAL_TEACHERS = "total_teachers";

        public static final String CLASS_1_BOYS = "class_1_boys";
        public static final String CLASS_1_GIRLS = "class_1_girls";
        public static final String CLASS_1_TOTAL = "class_1_total";

        public static final String CLASS_2_BOYS = "class_2_boys";
        public static final String CLASS_2_GIRLS = "class_2_girls";
        public static final String CLASS_2_TOTAL = "class_2_total";

        public static final String CLASS_3_BOYS = "class_3_boys";
        public static final String CLASS_3_GIRLS = "class_3_girls";
        public static final String CLASS_3_TOTAL = "class_3_total";

        public static final String CLASS_4_BOYS = "class_4_boys";
        public static final String CLASS_4_GIRLS = "class_4_girls";
        public static final String CLASS_4_TOTAL = "class_4_total";

        public static final String CLASS_5_BOYS = "class_5_boys";
        public static final String CLASS_5_GIRLS = "class_5_girls";
        public static final String CLASS_5_TOTAL = "class_5_total";

        public static final String CLASS_TOTAL_BOYS = "class_total_boys";
        public static final String CLASS_TOTAL_GIRLS = "class_total_girls";
        public static final String CLASS_TOTAL_TOTAL = "class_total_total";

        public static final String TOILETS_BOYS = "toilets_boys";
        public static final String TOILETS_BOYS_FUNCTIONING = "toilets_boys_functioning";

        public static final String TOILETS_GIRLS = "toilets_girls";
        public static final String TOILETS_GIRLS_FUNCTIONING = "toilets_girls_functioning";

        public static final String TOILETS_TOTAL = "toilets_total";
        public static final String TOILETS_TOTAL_FUNCTIONING = "toilets_total_functioning";

        public static final String URINALS_BOYS = "urinals_boys";
        public static final String URINALS_BOYS_FUNCTIONING = "urinals_boys_functioning";

        public static final String URINALS_GIRLS = "urinals_girls";
        public static final String URINALS_GIRLS_FUNCTIONING = "urinals_girls_functioning";

        public static final String URINALS_TOTAL = "urinals_total";
        public static final String URINALS_TOTAL_FUNCTIONING = "urinals_total_functioning";

        public static final String WATER_SOURCE = "water_source";
        public static final String NO_OF_TAPS = "no_of_taps";

    }

}
