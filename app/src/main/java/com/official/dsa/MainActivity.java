package com.official.dsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    LinearLayout official_cabin,panchayat_members,stars_of_sahungra,village_asserts,post_manager,complaints,recents;
    EditText versioncode;
    Button update;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        official_cabin=findViewById(R.id.official_cabin);
        panchayat_members=findViewById(R.id.panchayat_members);
        stars_of_sahungra=findViewById(R.id.stars_of_sahungra);
        versioncode=findViewById(R.id.version_code);
        update=findViewById(R.id.update);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        village_asserts=findViewById(R.id.village_asserts);
        post_manager=findViewById(R.id.post_manager);
        complaints=findViewById(R.id.complaints);
        recents=findViewById(R.id.recents);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                versioncode.setText(dataSnapshot.child("version").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(versioncode.getText().toString())){
                databaseReference.child("version").setValue(versioncode.getText().toString());
                Toast.makeText(MainActivity.this, "version updated", Toast.LENGTH_SHORT).show();

            }else { Toast.makeText(MainActivity.this, "version box is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        official_cabin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(MainActivity.this,official_cabin.class);
                startActivity(i);


            }
        });

        recents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,recents_batch.class);
                startActivity(i);
            }
        });

        panchayat_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,panchayt_members.class);
                startActivity(i);

            }
        });

        stars_of_sahungra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Stars.class);
                startActivity(i);

            }
        });
        village_asserts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Asserts.class);
                startActivity(i);

            }
        });
        post_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Posts.class);
                startActivity(i);

            }
        });

        complaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,complaints.class);
                startActivity(i);

            }
        });





        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 1);

    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }
}
