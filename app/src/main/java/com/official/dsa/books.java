package com.official.dsa;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class books extends Fragment {


    private static final int PICKFILE_REQUEST_CODE = 124;
    RecyclerView recyclerView;
    official_forms_adapter official_forms_adapter;
    List<official_form_list> list;
    DatabaseReference databaseReference;
    LinearLayout progressbar;

    FloatingActionButton addform;
    LinearLayout formeditor;

    Button browse,done;
    String key=null;
    String download_url=null;
    boolean isedtiorvisible=false;
    EditText formname,formdesc;
    StorageReference storageReference;
    EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_forms, container, false);


        databaseReference= FirebaseDatabase.getInstance().getReference("books");
        storageReference= FirebaseStorage.getInstance().getReference("books/");
        recyclerView=v.findViewById(R.id.forms_rv);
        progressbar=v.findViewById(R.id.progressbar);
        search=v.findViewById(R.id.formsearch);
        browse=v.findViewById(R.id.browse);
        formname=v.findViewById(R.id.fromname);
        formdesc=v.findViewById(R.id.formdescription);

        done=v.findViewById(R.id.fromdone);
        addform=v.findViewById(R.id.addform);
        list=new ArrayList<>();
        formeditor=v.findViewById(R.id.formeditor);
        progressbar.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        key=databaseReference.push().getKey();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    official_form_list listitem=dataSnapshot1.getValue(official_form_list.class);
                    list.add(listitem);
                }

                official_forms_adapter=new official_forms_adapter(getContext(),list);
                recyclerView.setAdapter(official_forms_adapter);

                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isedtiorvisible=!isedtiorvisible;

                if(isedtiorvisible==false){
                    formeditor.setVisibility(View.GONE);
                    addform.setImageResource(R.drawable.ic_add_black_24dp);
                }else{
                    formeditor.setVisibility(View.VISIBLE);
                    addform.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                }


            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(formname.getText().toString())){
                    Toast.makeText(getContext(), "Enter file name", Toast.LENGTH_SHORT).show();
                }else  if(TextUtils.isEmpty(formdesc.getText().toString())){
                    Toast.makeText(getContext(), "Enter file description", Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(download_url)){
                    Toast.makeText(getContext(), "first upload a file", Toast.LENGTH_SHORT).show();

                }else {
                    upload();
                }
            }

        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating an intent for file chooser
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select pdf"), PICKFILE_REQUEST_CODE);

            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                official_forms_adapter=new official_forms_adapter(getContext(),list);

                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(getContext(), "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }



    private void uploadFile(final Uri data) {


        progressbar.setVisibility(View.VISIBLE);
        if(getFileExtension(data).equals("pdf")){
            final StorageReference sRef = storageReference.child(key);

            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            sRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    download_url=task.getResult().toString();
                                    progressbar.setVisibility(View.GONE);


                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }else{
            progressbar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Please upload book in pdf format only", Toast.LENGTH_SHORT).show();
        }


    }

    public void upload(){

        Calendar calendar=Calendar.getInstance();
        Date date=calendar.getTime();

        if(download_url!=null) {

            official_form_list list = new official_form_list(download_url+".pdf", formname.getText().toString(), formdesc.getText().toString(), key,  date);
            databaseReference.child(key).setValue(list);
        }else{
            Toast.makeText(getContext(), "Select atleast one file", Toast.LENGTH_SHORT).show();
        }
        formname.setText("");
        formdesc.setText("");
        formeditor.setVisibility(View.GONE);
        addform.setImageResource(R.drawable.ic_add_black_24dp);
        Toast.makeText(getContext(), "Book uploaded Successfully", Toast.LENGTH_SHORT).show();
    }

    private  void filter(String text){
        List<official_form_list> filterlist=new ArrayList<>();
        for(official_form_list item:list){
            if(item.getName().toLowerCase().contains(text.toLowerCase())||item.getDescription().toLowerCase().contains(text.toLowerCase())){
                filterlist.add(item);
            }
        }
        official_forms_adapter.filterlist(filterlist);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(official_forms_adapter);


    }

}
