package com.example.hp.eggs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Main3Activity extends AppCompatActivity {

    DocumentReference mDocumentReference;

    Button logout;
    Button b11,b12,b6,b7,b2;
private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mFirestore=FirebaseFirestore.getInstance();

mDocumentReference=mFirestore.collection("user").document();
        logout=findViewById(R.id.button15);
        b11=findViewById(R.id.button11);
        b12=findViewById(R.id.button12);
        b6=findViewById(R.id.button6);
        b7=findViewById(R.id.button7);
        b2=findViewById(R.id.button2);

        Toast.makeText(this,"mobile "+FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),Toast.LENGTH_LONG).show();
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload u1=new upload(false,FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString());

                Toast.makeText(Main3Activity.this, "please wait ", Toast.LENGTH_SHORT).show();

                mDocumentReference.set(u1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Main3Activity.this, "Order Placed SuccessFully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Main3Activity.this, "Sorry Order Could not be placed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                //             orderDetails.put("name", name.getText().toString().trim());
        //           orderDetails.put("Address", address.getText().toString());
          //          orderDetails.put("email", email.getText().toString().trim());

            }
        });

        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main3Activity.this,Scheme.class);
                startActivity(i);
            }
        });
        b12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main3Activity.this,Feedback.class);
                startActivity(i);
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main3Activity.this,History.class);
                startActivity(i);
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main3Activity.this,Contacts.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Main3Activity.this,MainActivity.class));
            }
        });
         }
}
