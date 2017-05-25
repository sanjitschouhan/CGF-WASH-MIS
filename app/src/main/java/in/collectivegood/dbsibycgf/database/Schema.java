package in.collectivegood.dbsibycgf.database;

import android.provider.BaseColumns;

/**
 * Created by sanjit on 13/5/17.
 */

public final class Schema {

    private Schema() {
    }

    public static class SchoolDatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "schooldatabase";
        public static final String EMAIL_OF_CC = "email_of_cc";
        public static final String NAME_OF_CC = "name_of_cc";
        public static final String UID_OF_CC = "uid_of_cc";
        public static final String NAME_OF_BLOCK = "name_of_block";
        public static final String SCHOOL_CODE = "school_code";
        public static final String NAME_OF_SCHOOL = "name_of_school";
        public static final String STATE = "state";
        public static final String DISTRICT = "district";
        public static final String NAME_OF_ZONAL_COORDINATOR = "name_of_zonal_coordinator";
        public static final String EMAIL_OF_SCHOOL = "email_of_school";
    }

}
