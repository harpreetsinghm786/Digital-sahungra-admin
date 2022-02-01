package com.official.dsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.official.dsa.lists.update;
import com.squareup.picasso.Picasso;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Posts extends AppCompatActivity {

    DatabaseReference databaseReference,databaseReference2;

    EditText title,description;


    LinearLayout progressbar;
    List<String> urls;
    TextView progress_status;
    String key ;
    RoundedImageView imageView;


    private static final int RESULT_LOAD_IMAGE = 1;
    private Button chooseimages,upload;
    private RecyclerView mUploadList;
    TextView texttitle,textdescription;

    private List<String> fileNameList;
    private List<String> fileDoneList;

    private UploadListAdapter uploadListAdapter;

    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);


        progressbar=findViewById(R.id.progressbar);
        upload=findViewById(R.id.upload);
        texttitle=findViewById(R.id.texttitle);
        textdescription=findViewById(R.id.textdescription);
        title=findViewById(R.id.posttitle);
        progress_status=findViewById(R.id.progress_status);
        description=findViewById(R.id.postdescription);
        imageView=findViewById(R.id.post_image);
        urls=new ArrayList<>();

        Toolbar toolbar=findViewById(R.id.toolbarposts);

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


        mStorage = FirebaseStorage.getInstance().getReference();

        chooseimages = findViewById(R.id.chooser);
        upload=findViewById(R.id.upload);
        mUploadList =findViewById(R.id.selected_images);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();


        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList);

        //RecyclerView

        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("updates");
        key=databaseReference.push().getKey();



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploaditem();
            }
        });

        chooseimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);
            }
        });

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             texttitle.setText(title.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             textdescription.setText(description.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){

            if(data.getClipData() != null){

                int totalItemsSelected = data.getClipData().getItemCount();

                for(int i = 0; i < totalItemsSelected; i++){

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    final String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();

                    Log.d("key", "onActivityResult: "+key);

                    final StorageReference fileToUpload = mStorage.child("posts/").child(title.getText().toString()+key).child(fileName);

                    final int finalI = i;
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
                            uploadListAdapter.notifyDataSetChanged();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            fileToUpload.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri dowload= task.getResult();
                                    String downloadurl=dowload.toString();
                                    urls.add(downloadurl);
                                    Log.d("tagori", "onComplete: "+urls);
                                    progress_status.setText("Uploaded Images");
                                    Picasso.with(Posts.this).load(urls.get(0)).into(imageView);
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                           progress_status.setVisibility(View.VISIBLE);
                            int progressperc=Integer.valueOf((taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100+"");
                            progress_status.setText("Uploading..." + "("+progressperc+"% )");
                        }
                    });




                }

                //Toast.makeText(MainActivity.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();

            } else if (data.getData() != null){
                Uri fileUri = data.getData();
                final String fileName = getFileName(fileUri);
                final StorageReference fileToUpload = mStorage.child("posts/").child(title.getText().toString()+key).child(fileName);

                fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        fileDoneList.add(0, "done");
                        uploadListAdapter.notifyDataSetChanged();

                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        fileToUpload.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri dowload= task.getResult();
                                String downloadurl=dowload.toString();
                                urls.add(downloadurl);
                                progress_status.setText("Uploaded Images");
                                Picasso.with(Posts.this).load(urls.get(0)).into(imageView);
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        progress_status.setVisibility(View.VISIBLE);
                        int progressperc=Integer.valueOf((taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100+"");
                        progress_status.setText("Uploading..." + "("+progressperc+"% )");
                    }
                });



               // Toast.makeText(Posts.this, "Selected Single File", Toast.LENGTH_SHORT).show();

            }

        }

    }



    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }



    private void uploaditem(){



        if(TextUtils.isEmpty(title.getText().toString())){
            title.setError("Name is required");
        } else if(TextUtils.isEmpty(description.getText().toString())) {
            description.setError("Description is required");
        }else{

            progressbar.setVisibility(View.VISIBLE);
            update item=new update(urls,title.getText().toString(),description.getText().toString(),key);

            databaseReference.child(key).setValue(item);



            title.setText("");
            description.setText("");
            urls.clear();
            progress_status.setVisibility(View.GONE);
            texttitle.setText("Title");
            textdescription.setText("Description");
            fileNameList.clear();
            fileDoneList.clear();
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.dummyimg));
            progressbar.setVisibility(View.GONE);
            Toast.makeText(this, "Post Uploaded Successfully !", Toast.LENGTH_SHORT).show();


        }



    }
}
