package in.collectivegood.dbsibycgf.gallery;

import android.content.Context;

import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.support.InfoProvider;

class CCListItem {
    private String Uid;
    private String Name;

    CCListItem(String Uid, Context context) {
        this.Uid = Uid;
        Name = InfoProvider.getCCData(context, Uid, Schemas.CCDatabaseEntry.NAME);
    }

    public String getUid() {
        return Uid;
    }

    public String getName() {
        return Name;
    }

    @Override
    public String toString() {
        return Name;
    }
}
