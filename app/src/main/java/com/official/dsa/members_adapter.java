package com.official.dsa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.official.dsa.lists.member_list;
import com.official.dsa.lists.stars_list;
import com.squareup.picasso.Picasso;

import java.util.List;

public class members_adapter extends RecyclerView.Adapter<members_adapter.ViewHolder>{

    public List<member_list> list;

    Context context;

    public members_adapter(Context context, List<member_list> list){

        this.list=list;

        this.context=context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stars_item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        final member_list star_list=list.get(position);
        holder.starname.setText(star_list.getName());
        holder.stardesignation.setText(star_list.getDesignation());

        Picasso.with(context).load(star_list.getUrl()).into(holder.imageView);

        holder.stardesc.setText(star_list.getEducation());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,panchayt_member_profile_editor.class);
                i.putExtra("key",star_list.getKey());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView starname,stardesignation,stardesc;
        RoundedImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


            starname=itemView.findViewById(R.id.star_name);
            stardesignation=itemView.findViewById(R.id.star_designation);
            imageView=itemView.findViewById(R.id.star_url);
            stardesc=itemView.findViewById(R.id.star_desc);



        }

    }

}
