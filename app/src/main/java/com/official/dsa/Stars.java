package com.official.dsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.official.dsa.lists.stars_list;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Stars extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 123;
    RoundedImageView profilepic;
    Uri filePath;
    LinearLayout progressBar;

    StorageReference storageReference;
    TextView textname,textdesignation,textdesc;

    RecyclerView recyclerView;
    EditText starname,stardesignation,stardesc;
    Button done,chooser;
    String key;
    List<stars_list> list;
    star_adapter star_adapter;
    DatabaseReference databaseReference;
    FloatingActionButton addstar;
    ScrollView newstar;
    boolean isaddnewvisible=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars);
        chooser=findViewById(R.id.chooser);
        list=new ArrayList<>();
        addstar=findViewById(R.id.addnew);
        newstar=findViewById(R.id.newstar);

        textdesc=findViewById(R.id.star_desc);
        stardesc=findViewById(R.id.stardesc);
        databaseReference= FirebaseDatabase.getInstance().getReference("stars");
        storageReference = FirebaseStorage.getInstance().getReference("Stars/");
        profilepic=findViewById(R.id.star_url);
        textname=findViewById(R.id.star_name);
        progressBar=findViewById(R.id.progressbar);
        textdesignation=findViewById(R.id.star_designation);
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar=findViewById(R.id.toolbarstars);

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



        recyclerView=findViewById(R.id.star_rv);
        starname=findViewById(R.id.starname);
        stardesignation=findViewById(R.id.designation);
        done=findViewById(R.id.stardone);

        starname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              textname.setText(starname.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        stardesignation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textdesignation.setText(stardesignation.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isaddnewvisible=!isaddnewvisible;

                if(isaddnewvisible==true){
                    addstar.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                    newstar.setVisibility(View.VISIBLE);
                }else{
                    addstar.setImageResource(R.drawable.ic_add_black_24dp);
                    newstar.setVisibility(View.GONE);
                }

            }
        });

        stardesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textdesc.setText(stardesc.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(Stars.this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    stars_list starsList=dataSnapshot1.getValue(stars_list.class);
                    list.add(starsList);
                }
                star_adapter=new star_adapter(Stars.this,list);
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
                chooseimage();
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
        if (filePath != null) {
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference fileReference = storageReference.child(starname.getText().toString()+System.currentTimeMillis()
                    + "." + getFileExtension(filePath));


            fileReference.putFile(filePath)
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
                                    key=databaseReference.push().getKey();
                                    stars_list star=new stars_list(starname.getText().toString(),stardesignation.getText().toString(),downloadurl,key,stardesc.getText().toString());


                                    databaseReference.child(key).setValue(star);

                                    starname.setText("");
                                    stardesignation.setText("");
                                    stardesc.setText("");
                                    profilepic.setImageResource(R.drawable.dummyimg);
                                    textname.setText("Star Name");
                                    textdesignation.setText("Designation");
                                    textdesc.setText("Description");
                                }
                            });


                            Toast.makeText(Stars.this, "Star Uploaded", Toast.LENGTH_LONG).show();

                            progressBar.setVisibility(View.GONE);

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Stars.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST
                    && data != null && data.getData() != null) {
                filePath = data.getData();
                Picasso.with(this).load(filePath).into(profilepic);

            }

        }

    }


    private void upload(){
        if(TextUtils.isEmpty(starname.getText().toString())){
            Toast.makeText(this, "Name is Required", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(stardesignation.getText().toString())){
            Toast.makeText(this, "Designation is Required", Toast.LENGTH_SHORT).show();

        }else{


           uploadimage();


        }
    }
}
