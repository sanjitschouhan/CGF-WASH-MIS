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
        public static final String UID = "uid";
        public static final String PROJECT_COORDINATOR = "project_coordinator";
    }

}
