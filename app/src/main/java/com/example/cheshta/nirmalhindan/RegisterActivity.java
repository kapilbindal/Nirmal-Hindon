package com.example.cheshta.nirmalhindan;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity { FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks; // to check whether verification has been completed or failed
    private PhoneAuthProvider.ForceResendingToken mResendToken; // to re verify the phone number
    EditText edNewMobNum,edCode;
    Button btnCreateNew,btnVerify,btnSendOTP;
    TextView tvResendCode;
    LinearLayout registerLayout,verifyLayout,sendLayout,successLayout;
    public static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edNewMobNum = findViewById(R.id.edNewMobNum);
        edCode = findViewById(R.id.edCode);

        btnCreateNew = findViewById(R.id.btnCreateNew);
        btnVerify = findViewById(R.id.btnVerify);
        btnSendOTP = findViewById(R.id.btnSendOTP);

        tvResendCode = findViewById(R.id.tvResendCode);

        registerLayout = findViewById(R.id.registerLayout);
        verifyLayout = findViewById(R.id.verifyLayout);
        sendLayout = findViewById(R.id.sendLayout);
        successLayout = findViewById(R.id.successLayout);

        verifyLayout.setVisibility(View.GONE);
        disableView(registerLayout);
        //registerLayout.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        edNewMobNum.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        RegisterActivity.this,
                        mCallbacks
                );
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //mVerificationInProgress = false;
                Toast.makeText(RegisterActivity.this, "Verification Completed!", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(RegisterActivity.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
                if(e instanceof FirebaseAuthInvalidCredentialsException){
                    Log.d(TAG, "onVerificationFailed: " + "Invalid Credential" + e.getLocalizedMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.d(TAG, "onVerificationFailed: " + "SMS Quota exceeded");
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(RegisterActivity.this, "Verification code has been sent to your Number!", Toast.LENGTH_SHORT).show();
                mVerificationId = s;
                mResendToken = forceResendingToken;

                sendLayout.setVisibility(View.GONE);
                verifyLayout.setVisibility(View.VISIBLE);
            }
        };

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, edCode.getText().toString());
                // [END verify_with_code]
                verifyLayout.setVisibility(View.GONE);
                successLayout.setVisibility(View.VISIBLE);
                enableView(registerLayout);
                //registerLayout.setVisibility(View.VISIBLE);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void disableView(LinearLayout layout){
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(false);
        }
    }
    public void enableView(LinearLayout layout){
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(true);
        }
    }
}
