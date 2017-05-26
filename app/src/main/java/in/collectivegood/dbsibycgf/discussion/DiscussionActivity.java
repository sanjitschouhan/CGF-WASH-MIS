package in.collectivegood.dbsibycgf.discussion;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.DiscussionDbHelper;
import in.collectivegood.dbsibycgf.database.DiscussionRecord;
import in.collectivegood.dbsibycgf.database.Schemas;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DiscussionActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String LEGACY_SERVER_KEY = "AIzaSyAL2LYSlaYAMG8WBP5tr1fxDbPMAlJcRQU";
    private static final String TAG = "Discussion";
    protected ArrayList<DiscussionRecord> messages;
    private ListView messageListView;
    private FirebaseUser user;
    private ArrayAdapter<DiscussionRecord> adapter;
    private DbHelper dbHelper;
    private DiscussionDbHelper discussionDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
        user = FirebaseAuth.getInstance().getCurrentUser();

        messageListView = (ListView) findViewById(R.id.messages);
        messages = new ArrayList<>();
        adapter = new ArrayAdapter<DiscussionRecord>(this, android.R.layout.simple_list_item_2, android.R.id.text1, messages) {
            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                DiscussionRecord item = getItem(position);
                if (item != null) {
                    text1.setText(item.getName());
                    text2.setText(item.getMessage());
                }
                return view;
            }
        };
        messageListView.setAdapter(adapter);
        dbHelper = new DbHelper(this);
        discussionDbHelper = new DiscussionDbHelper(dbHelper);

        Cursor read = discussionDbHelper.read();
        if (read.getCount() > 0) {
            while (read.moveToNext()) {
                long time = read.getLong(read.getColumnIndexOrThrow(Schemas.DiscussionDatabaseEntry.TIME));
                String name = read.getString(read.getColumnIndexOrThrow(Schemas.DiscussionDatabaseEntry.NAME));
                String msg = read.getString(read.getColumnIndexOrThrow(Schemas.DiscussionDatabaseEntry.MESSAGE));
                messages.add(new DiscussionRecord(time, name, msg));
                adapter.notifyDataSetChanged();
            }
            messageListView.smoothScrollToPosition(messages.size());
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                messages.clear();
                Cursor read = discussionDbHelper.read();
                if (read.getCount() > 0) {
                    while (read.moveToNext()) {
                        long time = read.getLong(read.getColumnIndexOrThrow(Schemas.DiscussionDatabaseEntry.TIME));
                        String name = read.getString(read.getColumnIndexOrThrow(Schemas.DiscussionDatabaseEntry.NAME));
                        String msg = read.getString(read.getColumnIndexOrThrow(Schemas.DiscussionDatabaseEntry.MESSAGE));
                        messages.add(new DiscussionRecord(time, name, msg));
                        adapter.notifyDataSetChanged();
                        messageListView.smoothScrollToPosition(messages.size());
                    }
                }
                handler.postDelayed(this, 1500);

            }
        }, 1500);

    }

    public void sendMessage(View view) {
        EditText messageEditText = (EditText) findViewById(R.id.message);
        String message = messageEditText.getText().toString().trim();
        if (message.length() > 0) {
            sendNotification(message);
        }
        messageEditText.setText("");
    }

    private void sendNotification(final String message) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("title", user.getDisplayName());
                    dataJson.put("body", message);
                    json.put("notification", dataJson);
                    json.put("to", "/topics/allDevices");
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder().
                            header("Authorization", "key=" + LEGACY_SERVER_KEY)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    Log.d(TAG, "doInBackground: " + finalResponse);
                } catch (Exception e) {
                    Log.d(TAG, e + "");
                }
                return null;
            }
        }.execute();

    }
}
