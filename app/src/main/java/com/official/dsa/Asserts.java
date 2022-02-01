package com.official.dsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.official.dsa.lists.assets_list;

import java.util.ArrayList;
import java.util.List;

public class Asserts extends AppCompatActivity {


    Spinner spinner_Category;
    EditText assetname;
    Button assetdone;
    List<String> asset_categories;
    asset_adapter asset_adapter;
    DatabaseReference databaseReference;
    RecyclerView asset_rv;
    List<assets_list> assetslist;
    String names=null;
    LinearLayout progressbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asserts);

        progressbar=findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);

        asset_rv=findViewById(R.id.asset_rv);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(Asserts.this,RecyclerView.VERTICAL,false);
        asset_rv.setLayoutManager(linearLayoutManager);

        assetslist=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("assets");

        Toolbar toolbar=findViewById(R.id.toolbar_asserts);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.blue));
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        asset_categories=new ArrayList<>();
        asset_categories.add("Choose Asset Category");
        asset_categories.add("School");
        asset_categories.add("Primary Health Centre");
        asset_categories.add("Religious Places");
        asset_categories.add("Police Stations");
        asset_categories.add("Gym");
        asset_categories.add("NGO");
        asset_categories.add("Library");
        asset_categories.add("Anganwadi");
        asset_categories.add("Playground");
        asset_categories.add("Water Tank");
        asset_categories.add("Others");



        spinner_Category = (Spinner) findViewById(R.id.assetcategory);

        assetname=findViewById(R.id.assertname);
        assetdone=findViewById(R.id.assetdone);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,asset_categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Category.setAdapter(adapter);

        assetdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                uploadasset();
            }
        });

      spinner_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                  ((TextView) parent.getChildAt(0)).setTextColor(getColor(R.color.grey));
                  ((TextView) parent.getChildAt(0)).setTextAppearance(R.style.collappsed_subtitle_text_appearence);
              }
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                assetslist.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    assets_list assets_list=dataSnapshot1.getValue(com.official.dsa.lists.assets_list.class);
                    assetslist.add(0,assets_list);

                }

                List<String> a=new ArrayList<>();
                a=asset_categories.subList(1,asset_categories.size());


                asset_adapter=new asset_adapter(Asserts.this,assetslist,a);
                asset_rv.setAdapter(asset_adapter);
                progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void uploadasset(){


        if(TextUtils.isEmpty(assetname.getText().toString())){
            progressbar.setVisibility(View.GONE);
            Toast.makeText(this, "Asset Name is Required", Toast.LENGTH_SHORT).show();
        }else if(spinner_Category.getSelectedItem().toString().equals("Choose Asset Category")){
            progressbar.setVisibility(View.GONE);
            Toast.makeText(this, "Select Asset's Category ", Toast.LENGTH_SHORT).show();
        }else{

        String key=databaseReference.push().getKey();
            assets_list list_item=new assets_list(assetname.getText().toString(),key,spinner_Category.getSelectedItem().toString());
        databaseReference.child(key).setValue(list_item);

        Toast.makeText(this, "Asset added successfully!!!", Toast.LENGTH_SHORT).show();

        assetname.setText("");
        spinner_Category.setSelection(0);

            progressbar.setVisibility(View.GONE);

    }}
}
