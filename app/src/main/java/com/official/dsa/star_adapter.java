package com.official.dsa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.official.dsa.lists.stars_list;
import com.squareup.picasso.Picasso;

import java.util.List;

public class star_adapter extends RecyclerView.Adapter<star_adapter.ViewHolder>{

    public List<stars_list> list;

    Context context;

    public star_adapter(Context context, List<stars_list> list){

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


        stars_list star_list=list.get(position);
        holder.starname.setText(star_list.getName());
        holder.stardesignation.setText(star_list.getDesignation());

        Picasso.with(context).load(star_list.getUrl()).into(holder.imageView);

        holder.stardesc.setText(star_list.getDesc());



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
