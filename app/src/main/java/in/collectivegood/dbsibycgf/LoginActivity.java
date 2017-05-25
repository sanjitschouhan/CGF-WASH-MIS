package in.collectivegood.dbsibycgf;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "login_activity";
    String emailPattern = "[a-zA-Z0-9_\\.]+@[a-zA-Z0-9_]+\\.[a-zA-Z0-9_\\.]+";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String displayName = user.getDisplayName();
                    startActivity(new Intent(LoginActivity.this, InitializingActivity.class));
                    finish();
                }
            }
        };
    }


    public void forgotPassword(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText emailEditText = new EditText(this);
        emailEditText.setHint(getString(R.string.email));
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setPadding(30, 30, 30, 30);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(emailEditText);
        builder.setTitle(getString(R.string.reset_password))
                .setMessage(getString(R.string.enter_email_id))
                .setView(linearLayout)
                .setPositiveButton(getString(R.string.reset), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNeutralButton(getString(R.string.cancel), null);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                if (!email.matches(emailPattern)) {
                    emailEditText.setError(getString(R.string.invalid_email));
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email);
                    dialog.dismiss();
                }
            }
        });
    }

    public void SignIn(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (!email.matches(emailPattern)) {
            emailEditText.setError(getString(R.string.invalid_email));
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError(getString(R.string.min_pass_length_req));
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(true);
        dialog.setProgressNumberFormat(null);
        dialog.setProgressPercentFormat(null);
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle(getString(R.string.unable_to_sign_in))
                                .setMessage(e.getMessage())
                                .setPositiveButton(getString(R.string.close), null);
                        builder.show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
