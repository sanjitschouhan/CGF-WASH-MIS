package in.collectivegood.dbsibycgf.discussion;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.DiscussionDbHelper;
import in.collectivegood.dbsibycgf.database.DiscussionRecord;


public class CgfWashMisDiscussionService extends FirebaseMessagingService {
    private static final String TAG = "DiscussionService";
    private DiscussionDbHelper discussionDbHelper;

    public CgfWashMisDiscussionService() {
        super();
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (discussionDbHelper == null) {
            DbHelper dbHelper = new DbHelper(getApplicationContext());
            discussionDbHelper = new DiscussionDbHelper(dbHelper);
        }
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Log.d(TAG, "onMessageReceived: " + notification.getTitle());
        Log.d(TAG, "onMessageReceived: " + notification.getBody());
        DiscussionRecord record = new DiscussionRecord(System.currentTimeMillis(), notification.getTitle(), notification.getBody());
        discussionDbHelper.insert(record);
        super.onMessageReceived(remoteMessage);
    }
}
