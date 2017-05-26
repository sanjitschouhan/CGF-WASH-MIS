package in.collectivegood.dbsibycgf.discussion;

import android.app.Service;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by sanjit on 26/5/17.
 */

public class CgfWashMisInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
    }
}
