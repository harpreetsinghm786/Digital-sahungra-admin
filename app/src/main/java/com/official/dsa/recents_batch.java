package com.official.dsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class recents_batch extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 124 ;
    EditText title,button_text,link;
    Button imageupload,done;
    RoundedImageView imageView;
    String url="";
    RadioButton visibile;
    DatabaseReference databaseReference;
    boolean bool=false;
    LinearLayout progressbar;
    TextView bacthtitle,batchbutton;
    Uri filePath;
    StorageReference storageReference;
    FirebaseAuth auth;
    Uri resulturi;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recents_batch);
        done=findViewById(R.id.done);
        imageupload=findViewById(R.id.image_upload);
        auth=FirebaseAuth.getInstance();
        title=findViewById(R.id.title_text);
        progressbar=findViewById(R.id.progressbar);
        button_text=findViewById(R.id.button_text);
        storageReference = FirebaseStorage.getInstance().getReference("batch");

        link=findViewById(R.id.link);
        visibile=findViewById(R.id.radio_visibility);
        imageView=findViewById(R.id.batchimage);
        bacthtitle=findViewById(R.id.batchtext);
        batchbutton=findViewById(R.id.batchbutton);
        databaseReference= FirebaseDatabase.getInstance().getReference("batch");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                visibile.setChecked(dataSnapshot.child("visibility").getValue(Boolean.class));
                title.setText(dataSnapshot.child("title").getValue(String.class));
                button_text.setText(dataSnapshot.child("button").getValue(String.class));
                link.setText(dataSnapshot.child("url").getValue(String.class));
                Picasso.with(recents_batch.this).load(dataSnapshot.child("imageurl").getValue(String.class)).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        visibile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bool=!bool;
                if(bool==true){
                    visibile.setChecked(true);
                    databaseReference.child("visibility").setValue(true);

                }else {
                    visibile.setChecked(false);
                    databaseReference.child("visibility").setValue(false);
                }

            }
        });





        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bacthtitle.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              batchbutton.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(recents_batch.this);


            }
        });





        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(title.getText().toString())){
                    Toast.makeText(recents_batch.this, "title is required", Toast.LENGTH_SHORT).show();
                }else  if(TextUtils.isEmpty(button_text.getText().toString())){
                    Toast.makeText(recents_batch.this, "button text is required", Toast.LENGTH_SHORT).show();
                }else  if(TextUtils.isEmpty(url)){
                    Toast.makeText(recents_batch.this, "Image is required", Toast.LENGTH_SHORT).show();
                }else{
                    batches batch ;
                    if(link.getText().toString().equals("")){
                         batch = new batches(title.getText().toString(), button_text.getText().toString(), "null", url, bool);

                    }else {
                        batch = new batches(title.getText().toString(), button_text.getText().toString(), link.getText().toString(), url, bool);
                    }
                    databaseReference.setValue(batch);
                    Toast.makeText(recents_batch.this, "Batch uploaded", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }



    private void uploadimage() {
        if (resulturi != null) {
            progressbar.setVisibility(View.VISIBLE);

            final StorageReference fileReference = storageReference.child("batch." + getFileExtension(resulturi));


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

                                   url=downloadurl;



                                }
                            });


                            Toast.makeText(recents_batch.this, "Uploading Complete", Toast.LENGTH_LONG).show();

                            progressbar.setVisibility(View.GONE);

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(recents_batch.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                resulturi= result.getUri();
                 Picasso.with(this).load(resulturi).into(imageView);
                uploadimage();
            }

        }

    }
}
