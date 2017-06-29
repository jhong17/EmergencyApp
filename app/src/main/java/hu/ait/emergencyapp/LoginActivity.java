package hu.ait.emergencyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSignIn)
    public void signInClick() {

        if (!isFormValid()) {
            return;
        }

        showProgressDialog();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                etEmail.getText().toString(), etPassword.getText().toString()

        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
                if (task.isSuccessful()) {

                    Intent intentMainActivity = new Intent();
                    intentMainActivity.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intentMainActivity);

                } else {

//                    Toast.makeText(LoginActivity.this, "Not successs: "+
//                            task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick(R.id.btnSignUp)
    public void signUpClick() {

        //stupid change for committ
        int asdkjands = 0;
        int asasd = 0;

        if (!isFormValid()) {
            return;
        }

        showProgressDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                etEmail.getText().toString(), etPassword.getText().toString()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();

                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "User created", Toast.LENGTH_SHORT).show();

                    FirebaseUser fireBaseUser = task.getResult().getUser();

                    fireBaseUser.updateProfile(new UserProfileChangeRequest.Builder().
                            setDisplayName(getUserNameFromEmail(fireBaseUser.getEmail())).build());
                } else {
//                    Toast.makeText(LoginActivity.this, "Not successs: "+
//                            task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();

                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        hideProgressDialog();
        super.onStop();
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Wait for it...");
        }

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public boolean isFormValid() {
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError(getString(R.string.empty_string));
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError(getString(R.string.empty_string));
            return false;
        }

        return true;
    }

    private String getUserNameFromEmail(String email){
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

}
