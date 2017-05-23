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
import com.google.firebase.auth.UserProfileChangeRequest;

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
                    if (displayName == null) {
                        setNickName(user);
                    } else {
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                        finish();
                    }
                }
            }
        };
    }


    public void forgotPassword(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText emailEditText = new EditText(this);
        emailEditText.setHint("Email");
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setPadding(30, 30, 30, 30);
        linearLayout.addView(emailEditText);
        builder.setTitle("Reset Password")
                .setMessage("Enter your Email Id")
                .setView(linearLayout)
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNeutralButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                if (!email.matches(emailPattern)) {
                    emailEditText.setError("Invalid email");
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
            emailEditText.setError("Invalid Email");
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Minimum 6 characters required");
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait");
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
                        builder.setTitle("Unable to Sign In")
                                .setMessage(e.getMessage())
                                .setPositiveButton("Close", null);
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

    public void setNickName(final FirebaseUser user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(30, 30, 30, 30);
        final EditText editText = new EditText(this);
        editText.setHint("Nickname");
        linearLayout.addView(editText);
        builder.setTitle("Enter Nickname")
                .setView(linearLayout)
                .setMessage("This name will be used to save your data")
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickName = editText.getText().toString().trim().replace(" ", "_");
                        UserProfileChangeRequest userProfileChangeRequest
                                = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nickName)
                                .build();
                        user.updateProfile(userProfileChangeRequest);
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                        finish();
                    }
                });
        builder.show();
    }
}
