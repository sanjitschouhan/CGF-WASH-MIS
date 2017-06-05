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
    }

}
