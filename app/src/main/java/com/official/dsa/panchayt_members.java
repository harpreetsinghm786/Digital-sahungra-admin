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
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.official.dsa.lists.member_list;
import com.official.dsa.lists.stars_list;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

public class panchayt_members extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 123;

    RoundedImageView profilepic;
    Uri filePath;
    LinearLayout progressBar;


    StorageReference storageReference;
    boolean aBoolean=false;
    FloatingActionButton addnew;

    RecyclerView recyclerView;
    EditText name,designation,education,currently_doing,hobbies,phone,business;

    LinearLayout addnewmember;

    Button done;
    ImageButton chooser;
    DatePicker datePicker;
    Uri resulturi;
    String key;
    List<member_list> list;
    members_adapter star_adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panchayt_members);
        chooser=findViewById(R.id.chooser);
        business=findViewById(R.id.business);
        addnewmember=findViewById(R.id.addnewmember);
        addnew=findViewById(R.id.addnew);
        Toolbar toolbar=findViewById(R.id.toolbar_members);

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


        list=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("panchayat_members");
        storageReference = FirebaseStorage.getInstance().getReference("panchayat_members/");
        profilepic=findViewById(R.id.member_profile_pic);
        progressBar=findViewById(R.id.progressbar);
        datePicker=findViewById(R.id.bday_date_picker);

        progressBar.setVisibility(View.VISIBLE);


        recyclerView=findViewById(R.id.star_rv);
        name=findViewById(R.id.membername);
        designation=findViewById(R.id.memberdesignation);
        education=findViewById(R.id.membereducation);
        currently_doing=findViewById(R.id.currentlydoing);
        hobbies=findViewById(R.id.memberhobby);
        phone=findViewById(R.id.memberphone);

        done=findViewById(R.id.newmemberdone);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(panchayt_members.this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    member_list starsList=dataSnapshot1.getValue(member_list.class);
                    list.add(starsList);
                }
                star_adapter=new members_adapter(panchayt_members.this,list);
                recyclerView.setAdapter(star_adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chooseimage();
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(panchayt_members.this);

            }
        });

        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aBoolean=!aBoolean;

                if(aBoolean==true){
                    addnewmember.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    addnew.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                }else{
                    addnewmember.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    addnew.setImageResource(R.drawable.ic_add_black_24dp);
                }

            }
        });




    }


    private void chooseimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadimage() {
        if (resulturi != null) {
            progressBar.setVisibility(View.VISIBLE);
            key=databaseReference.push().getKey();
            final StorageReference fileReference = storageReference.child(key
                    + "." + getFileExtension(resulturi));


            fileReference.putFile(resulturi)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 500);

                            fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri dowload = task.getResult();
                                    String downloadurl = dowload.toString();

                                    member_list member_list=new member_list(education.getText().toString(),currently_doing.getText().toString(),hobbies.getText().toString(),datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getYear(),phone.getText().toString(),name.getText().toString(),designation.getText().toString(),downloadurl,key,business.getText().toString());


                                    databaseReference.child(key).setValue(member_list);

                                    name.setText("");
                                    designation.setText("");
                                    hobbies.setText("");
                                    education.setText("");
                                    currently_doing.setText("");
                                    phone.setText("");
                                    business.setText("");
                                    profilepic.setImageResource(R.drawable.dummyimg);

                                }
                            });


                            Toast.makeText(panchayt_members.this, "Uploading Complete", Toast.LENGTH_LONG).show();

                            progressBar.setVisibility(View.GONE);

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(panchayt_members.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });


        } else {
            Toast.makeText(this, "Select profile pic", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST
                    && data != null && data.getData() != null) {
                filePath = data.getData();

                   }
                if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                    CropImage.ActivityResult result=CropImage.getActivityResult(data);
                    if(resultCode==RESULT_OK){
                         resulturi= result.getUri();
                        Picasso.with(this).load(resulturi).into(profilepic);
                    }

                }

            }


    private void upload(){
        if(TextUtils.isEmpty(business.getText().toString())){
            Toast.makeText(this, "Business is Required", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(currently_doing.getText().toString())){
            Toast.makeText(this, "Current Doing is Required", Toast.LENGTH_SHORT).show();
        }else  if(TextUtils.isEmpty(hobbies.getText().toString())){
            Toast.makeText(this, "hobbies is Required", Toast.LENGTH_SHORT).show();
        }else  if(TextUtils.isEmpty(currently_doing.getText().toString())){
            Toast.makeText(this, "Current Doing is Required", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone.getText().toString())){
            Toast.makeText(this, "Phone Number is Required", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(this, "Name is Required", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(designation.getText().toString())){
            Toast.makeText(this, "Designation is Required", Toast.LENGTH_SHORT).show();

        }else {


            uploadimage();


        }
    }
}
