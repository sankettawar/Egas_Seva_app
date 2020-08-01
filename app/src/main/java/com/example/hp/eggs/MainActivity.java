package com.example.hp.eggs;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    EditText mNumner,afterotpEditText;
    TextView resendotp,afterotpsendtext;
    Button verify;
    String no,mVerificationcode;
    PhoneAuthProvider.ForceResendingToken mForceResendingToken;
    FirebaseAuth mAuth;
    String Firebasecode;

    ProgressDialog pd;

    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        mNumner=findViewById(R.id.mobileno);
        verify=findViewById(R.id.verifyButton);
        afterotpsendtext=findViewById(R.id.textViewAfterOTPTxet);

        afterotpEditText=findViewById(R.id.verifyReceivedOTPEditText);

        mAuth=FirebaseAuth.getInstance();

        resendotp=findViewById(R.id.textViewResendOTP);

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Resend OTP
                Toast.makeText(MainActivity.this, "Verification Code is Resent To You", Toast.LENGTH_SHORT).show();
                sendOtp();
            }
        });

        requestHint();




        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no=mNumner.getText().toString();
                if(TextUtils.isEmpty(no)||no.length()<10)
                {
                    Toast.makeText(getApplicationContext(), "enter valid number", Toast.LENGTH_SHORT).show();
                }
                else if(flag==0){

                    afterotpsendtext.setVisibility(View.VISIBLE);
                    verify.setText("Verify Number");
                    mNumner.setVisibility(View.GONE);
                    afterotpEditText.setVisibility(View.VISIBLE);
                    sendOtp();


                }
                else
                {
                    Firebasecode=afterotpEditText.getText().toString();
                    if(TextUtils.isEmpty(Firebasecode)||Firebasecode.length()<6)
                    {
                        Toast.makeText(MainActivity.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        afterotpsendtext.setVisibility(View.VISIBLE);
                        verify.setText("Verify Number");
                        mNumner.setVisibility(View.GONE);
                        afterotpEditText.setVisibility(View.VISIBLE);
                        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationcode,Firebasecode);
                        signInWithPhoneAuthCredential(credential);
                    }

                }
            }
        });
    }
    public void sendOtp()
    {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+no,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);// OnVerificationStateChangedCallbacks


    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            pd=new ProgressDialog(MainActivity.this,ProgressDialog.STYLE_SPINNER);
            pd.setTitle("Please wait Verifying You");
            pd.show();

            signInWithPhoneAuthCredential(phoneAuthCredential);

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            mVerificationcode=s;
            mForceResendingToken=forceResendingToken;

            flag=1;



        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            resendotp.setVisibility(View.VISIBLE);
        }

    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            //   pd.dismiss();
                            finish();
                            startActivity(new Intent(MainActivity.this,Main3Activity.class));

                        } else {

                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,Main3Activity.class));
        }
    }

    private void requestHint() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build()
                ;



    }
}


