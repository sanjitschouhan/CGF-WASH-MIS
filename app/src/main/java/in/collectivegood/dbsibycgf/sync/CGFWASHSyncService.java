package in.collectivegood.dbsibycgf.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CGFWASHSyncService extends Service {
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    // Storage for an instance of the sync adapter
    private static HEPSDataSyncAdapter hepsDataSyncAdapter = null;

    @Override
    public IBinder onBind(Intent intent) {
        return hepsDataSyncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (hepsDataSyncAdapter == null) {
                hepsDataSyncAdapter = new HEPSDataSyncAdapter(getApplicationContext(), true);
            }
        }
    }
}
