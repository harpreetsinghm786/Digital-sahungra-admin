package com.official.dsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.official.dsa.lists.attachment_list;
import com.official.dsa.lists.complaint_list;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;


public class complaints_details extends AppCompatActivity {

    Toolbar toolbar;
    TextView title,name,msg;
    RecyclerView rv;
    LinearLayout progressbar,attanchment_container;
    DatabaseReference databaseReference;
    String key=null;
    List<attachment_list> list;
    attachment_adapter attachment_adapter;
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_details);
        key=getIntent().getStringExtra("key");
        title=findViewById(R.id.complainttitle);
        name=findViewById(R.id.sendername);
        date=findViewById(R.id.date);
        attanchment_container=findViewById(R.id.attachment_container);
        msg=findViewById(R.id.etmsg);
        list=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(complaints_details.this,RecyclerView.VERTICAL,false);
        databaseReference= FirebaseDatabase.getInstance().getReference("complaints").child(key);
        rv=findViewById(R.id.attachment_rv);
        progressbar=findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        toolbar=findViewById(R.id.complaints_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rv.setLayoutManager(linearLayoutManager);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.color2));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                complaint_list complaint_list=dataSnapshot.getValue(com.official.dsa.lists.complaint_list.class);
                name.setText("From : "+complaint_list.getName());
                msg.setText(complaint_list.getMessage());
                title.setText(complaint_list.getTitle());
                 date.setText(DateFormat.getDateInstance(DateFormat.LONG).format(complaint_list.getDate()));


                if(dataSnapshot.hasChild("attachment_list")) {

                    list.add(complaint_list.getAttachment_list());
                    attachment_adapter = new attachment_adapter(complaints_details.this, list);
                    rv.setAdapter(attachment_adapter);
                    attanchment_container.setVisibility(View.VISIBLE);

                }
                progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
