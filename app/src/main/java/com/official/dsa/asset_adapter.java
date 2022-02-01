package com.official.dsa;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.official.dsa.lists.assets_list;

import java.util.ArrayList;
import java.util.List;

public class asset_adapter extends RecyclerView.Adapter<asset_adapter.ViewHolder>{

    public List<assets_list> list;
    public List<String> assetcategory;
    Context context;
    List<String> name=new ArrayList<>();

    public asset_adapter(Context context, List<assets_list> list,List<String>assetcategory){

        this.list=list;
        this.assetcategory=assetcategory;
        this.context=context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_item, parent, false);


        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        String category = assetcategory.get(position);




        name.clear();
        for(int i=0;i<list.size();i++){
            if(assetcategory.get(position).equals(list.get(i).getCategory())){
            name.add(list.get(i).getName());

            }
        }
        holder.assetcategory.setText(category+ " ("+name.size()+")");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        holder.rv.setLayoutManager(linearLayoutManager);
     assetinner_rv_Adapter assetinner_rv_adapter=new assetinner_rv_Adapter(context,name);
     holder.rv.setAdapter(assetinner_rv_adapter);

     if(category.equals("School")){
         holder.image.setImageResource(R.mipmap.school);
     }else if(category.equals("Primary Health Centre")){
         holder.image.setImageResource(R.mipmap.hospital);
     }else if(category.equals("Religious Places")){
         holder.image.setImageResource(R.mipmap.religios);
     }else if(category.equals("Police Stations")){
         holder.image.setImageResource(R.mipmap.police);
         holder.image.setPadding(30 , 0,30,0);
     }else if(category.equals("Gym")){
         holder.image.setImageResource(R.mipmap.gym);
     }else if(category.equals("NGO")){
         holder.image.setImageResource(R.mipmap.ngo);
     }else if(category.equals("Library")){
         holder.image.setImageResource(R.mipmap.lib);
     }else if(category.equals("Anganwadi")){
         holder.image.setImageResource(R.mipmap.anganwadi);
     }else if(category.equals("Playground")){
         holder.image.setImageResource(R.mipmap.ground);
     }else if(category.equals("Water Tank")){
         holder.image.setBackground(context.getResources().getDrawable(R.drawable.last_item_plate));
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             holder.image.getBackground().setTint(context.getResources().getColor(R.color.white));
         }
         holder.image.setImageResource(R.mipmap.watertank);
     }else if(category.equals("Others")){
         holder.image.setImageResource(R.mipmap.more);
         holder.image.setPadding(200 , 200,200,200);
         holder.image.setBackground(context.getResources().getDrawable(R.drawable.last_item_plate));
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             holder.image.getBackground().setTint(context.getResources().getColor(R.color.black_overlay));
         }
     }















    }

    @Override
    public int getItemCount() {
        return assetcategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView assetcategory;
        RecyclerView rv;
        RoundedImageView image;


        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            assetcategory=itemView.findViewById(R.id.asset_category);
            rv=itemView.findViewById(R.id.inner_rv);
            image=itemView.findViewById(R.id.asset_image);


        }

    }

}
