package in.collectivegood.dbsibycgf.discussion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONObject;

import in.collectivegood.dbsibycgf.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DiscussionActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String LEGACY_SERVER_KEY = "AIzaSyAL2LYSlaYAMG8WBP5tr1fxDbPMAlJcRQU";
    private static final String TAG = "Discussion";
    ListView messageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        messageListView = (ListView) findViewById(R.id.messages);

    }

    public void sendMessage(View view) {
        sendNotification("AAAAJ_nQHys:APA91bGjoLMpVmGVMzJm9Lfrb1AfKMc24RB09KwZA8IDESRyaSFdZSAIxXK9A3GvaxvBEfDBBywI8mB99XeUmPImnSIM83tHqSjUQ3ECc5eQibYWpJqya3g8CucHByK_Il_qrRCx4yjY");
    }

    private void sendNotification(final String regToken) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", "Hi this is sent from device to device");
                    dataJson.put("title", "dummy title");
                    json.put("notification", dataJson);
                    json.put("to", regToken);
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
