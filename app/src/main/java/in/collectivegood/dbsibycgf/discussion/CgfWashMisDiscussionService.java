package in.collectivegood.dbsibycgf.discussion;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by sanjit on 26/5/17.
 */

public class CgfWashMisDiscussionService extends FirebaseMessagingService {
    private static final String TAG = "DiscussionService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Log.d(TAG, "onMessageReceived: " + data.get("body"));
        Log.d(TAG, "onMessageReceived: " + data.get("title"));
        Log.d(TAG, "onMessageReceived: " + notification.getTitle());
        Log.d(TAG, "onMessageReceived: " + notification.getBody());
        super.onMessageReceived(remoteMessage);
    }
}
