package com.official.dsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.official.dsa.lists.attachment_list;
import com.official.dsa.lists.complaint_list;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class complaints extends AppCompatActivity {
    private static final int PICKFILE_REQUEST_CODE = 124;
    RecyclerView complaints_rv;
    DatabaseReference databaseReference;
    List<complaint_list> list;
    LinearLayout progressbar;


    complaints_adapter complaints_adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        databaseReference= FirebaseDatabase.getInstance().getReference("complaints");

        complaints_rv=findViewById(R.id.complaints_rv);
        progressbar=findViewById(R.id.progressbar);
        toolbar=findViewById(R.id.complaints_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.color2));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        progressbar.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(complaints.this,RecyclerView.VERTICAL,false);
        complaints_rv.setLayoutManager(linearLayoutManager);
        list=new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    complaint_list item=dataSnapshot1.getValue(complaint_list.class);
                    list.add(item);
                }
                complaints_adapter=new complaints_adapter(complaints.this,list);
                complaints_rv.setAdapter(complaints_adapter);
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }







}
