package in.collectivegood.dbsibycgf.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CGFWASHSyncService extends Service {
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    // Storage for an instance of the sync adapter
    private static CheckInSyncAdapter sCheckInSyncAdapter = null;

    @Override
    public IBinder onBind(Intent intent) {
        return sCheckInSyncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sCheckInSyncAdapter == null) {
                sCheckInSyncAdapter = new CheckInSyncAdapter(getApplicationContext(), true);
            }
        }
    }
}
