package in.collectivegood.dbsibycgf.heps;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.database.HEPSDataRecord;

public class MailHEPSDataActivity extends AppCompatActivity {
    private static MimeMessage message;
    private static String csvFileContent = HEPSDataRecord.header();
    private static String filename;
    private String email;
    private ProgressDialog progressDialog;

    private void createFileAndSendMail(final String userMail) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("heps_data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot state : dataSnapshot.getChildren()) {
                    for (DataSnapshot cc : state.getChildren()) {
                        for (DataSnapshot hepsRecord : cc.getChildren()) {
                            HEPSDataRecord value = hepsRecord.getValue(HEPSDataRecord.class);
                            csvFileContent = csvFileContent.concat(value.toString());
                        }
                    }
                }

                try {
                    FileWriter fileWriter = new FileWriter(filename);
                    fileWriter.write(csvFileContent);
                    fileWriter.close();
                    sendMail(userMail, "HEPS Data");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendMail(String to, String subject) {

        // Sender's email ID needs to be mentioned
        String from = "sanjitsinghchouhan@gmail.com";//change accordingly
        final String username = "sanjitsinghchouhan";//change accordingly
        final String password = "tbwtlqmjchyrglwf";//change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please Check Attachment for file");
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();

            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        SendMail sendMail = new SendMail();
        progressDialog.show();
        sendMail.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_hepsdata);
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        ((TextView) findViewById(R.id.email)).setText(email);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.sending_mail));
    }

    public void sendMail(View view) {
        mail(email);
    }

    public void mail(String userMail) {
        filename = Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/heps_data.csv";
        createFileAndSendMail(userMail);
    }

    void exit() {
        progressDialog.dismiss();
        finish();
    }

    private class SendMail extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.e("doInBackground: ", "Start of sending mail");
                Transport.send(message);
                Log.e("doInBackground: ", "End of sending mail");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            exit();
        }
    }
}
