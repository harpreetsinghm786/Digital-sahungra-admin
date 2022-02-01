package com.official.dsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.firebase.auth.FirebaseAuth;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class panchayt_member_profile_editor extends AppCompatActivity {

    TextView name,education,currently_doing,hobbies,business,dob,phn;
    private static final int PICK_IMAGE_REQUEST = 123;
    String key;
    Uri resulturi;

    Uri filePath;

    DatabaseReference databaseReference;
    LinearLayout editname,editbio,editdate,editphone;
    LinearLayout progressbar;
    StorageReference storageReference;
        RoundedImageView imageView;
        ImageButton chooser;
        Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panchayt_member_profile_editor);

        databaseReference= FirebaseDatabase.getInstance().getReference("panchayat_members");
        storageReference = FirebaseStorage.getInstance().getReference("panchayat_members/");

        toolbar=findViewById(R.id.toolbar_profile_editor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        chooser=findViewById(R.id.chooser);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.color2));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        imageView=findViewById(R.id.user_profile_pic);
        name=findViewById(R.id.membername);
        editbio=findViewById(R.id.editbio);
        editdate=findViewById(R.id.editdob);
        editphone=findViewById(R.id.editphn);
        education=findViewById(R.id.education);
        currently_doing=findViewById(R.id.currently_doing);
        hobbies=findViewById(R.id.hobbies);
        editname=findViewById(R.id.editname);
        business=findViewById(R.id.business);
        progressbar=findViewById(R.id.progressbar);
        dob=findViewById(R.id.memberdob);
        phn=findViewById(R.id.memberphn);
        key=getIntent().getStringExtra("key");

        progressbar.setVisibility(View.VISIBLE);
        databaseReference= FirebaseDatabase.getInstance().getReference("panchayat_members").child(key);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   member_list member_list=dataSnapshot.getValue(com.official.dsa.lists.member_list.class);
                   name.setText(member_list.getName());
                   education.setText(member_list.getEducation());
                   currently_doing.setText(member_list.getCurrently_doing());
                   hobbies.setText(member_list.getHobbies());
                   dob.setText(member_list.getDob());
                   phn.setText(member_list.getPhone());

                   business.setText(member_list.getBusiness());
                   Picasso.with(panchayt_member_profile_editor.this).load(member_list.getUrl()).into(imageView);
                   progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(panchayt_member_profile_editor.this);

            }
        });








        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(panchayt_member_profile_editor.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.edit_profile_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout drawer = dialog.findViewById(R.id.drawer);
                final TextView heading;
                final EditText editor;
                editor = dialog.findViewById(R.id.edited_text);
                heading=dialog.findViewById(R.id.heading);
                heading.setText("Enter your name");
                editor.setText(name.getText().toString());

                Animation transintion = AnimationUtils.loadAnimation(panchayt_member_profile_editor.this, R.anim.transition_upward);
                drawer.setAnimation(transintion);
                Button save=dialog.findViewById(R.id.save);
                Button cancel=dialog.findViewById(R.id.cancel);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("panchayat_members").child(key).child("name");
                        databaseReference.setValue(editor.getText().toString());
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        editbio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(panchayt_member_profile_editor.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.edit_bio);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout drawer = dialog.findViewById(R.id.drawer);
                final TextView heading;
                final EditText editeducation,editcurrentlydoing,editbusiness,edithobbies;
                editeducation = dialog.findViewById(R.id.edited_text_education);
                editcurrentlydoing = dialog.findViewById(R.id.edited_text_currentlydoing);
                editbusiness = dialog.findViewById(R.id.edited_text_business);
                edithobbies = dialog.findViewById(R.id.edited_text_hobbies);

                heading=dialog.findViewById(R.id.heading);
                heading.setText("Edit Bio");
                editeducation.setText(education.getText().toString());
                editcurrentlydoing.setText(currently_doing.getText().toString());
                editbusiness.setText(business.getText().toString());
                edithobbies.setText(hobbies.getText().toString());

                Animation transintion = AnimationUtils.loadAnimation(panchayt_member_profile_editor.this, R.anim.transition_upward);
                drawer.setAnimation(transintion);
                Button save=dialog.findViewById(R.id.save);
                Button cancel=dialog.findViewById(R.id.cancel);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("panchayat_members").child(key);
                        databaseReference.child("education").setValue(editeducation.getText().toString());
                        databaseReference.child("hobbies").setValue(edithobbies.getText().toString());
                        databaseReference.child("business").setValue(editbusiness.getText().toString());
                        databaseReference.child("currently_doing").setValue(editcurrentlydoing.getText().toString());

                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        editphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(panchayt_member_profile_editor.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.edit_profile_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout drawer = dialog.findViewById(R.id.drawer);
                final TextView heading;
                final EditText editor;
                editor = dialog.findViewById(R.id.edited_text);
                heading=dialog.findViewById(R.id.heading);
                heading.setText("Edit Phone number");
                editor.setText(phn.getText().toString());

                Animation transintion = AnimationUtils.loadAnimation(panchayt_member_profile_editor.this, R.anim.transition_upward);
                drawer.setAnimation(transintion);
                Button save=dialog.findViewById(R.id.save);
                Button cancel=dialog.findViewById(R.id.cancel);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("panchayat_members").child(key).child("phone");
                        databaseReference.setValue(editor.getText().toString());
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        editdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(panchayt_member_profile_editor.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.edit_date_pop_up);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout drawer = dialog.findViewById(R.id.drawer);
                final DatePicker datePicker;
                datePicker=dialog.findViewById(R.id.bday_date_picker);
                Animation transintion = AnimationUtils.loadAnimation(panchayt_member_profile_editor.this, R.anim.transition_upward);
                drawer.setAnimation(transintion);

                Button save=dialog.findViewById(R.id.save);
                Button cancel=dialog.findViewById(R.id.cancel);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("panchayat_members").child(key).child("dob");

                        databaseReference.setValue(datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getYear());
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });


    }


    private void uploadimage() {
        if (resulturi != null) {
            progressbar.setVisibility(View.VISIBLE);

            final StorageReference fileReference = storageReference.child(key + "." + getFileExtension(resulturi));


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

                                    databaseReference.child("url").setValue(downloadurl);


                                }
                            });


                            Toast.makeText(panchayt_member_profile_editor.this, "Uploading Complete", Toast.LENGTH_LONG).show();

                            progressbar.setVisibility(View.GONE);

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(panchayt_member_profile_editor.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressbar.setVisibility(View.VISIBLE);
                        }
                    });


        } else {
            Toast.makeText(this, "Select profile pic", Toast.LENGTH_SHORT).show();
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
                Picasso.with(this).load(resulturi).into(imageView);
                uploadimage();
            }

        }

    }
}
