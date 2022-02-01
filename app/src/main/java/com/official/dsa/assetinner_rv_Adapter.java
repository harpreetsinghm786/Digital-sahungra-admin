package com.official.dsa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.official.dsa.lists.assets_list;

import java.util.List;

public class assetinner_rv_Adapter extends RecyclerView.Adapter<assetinner_rv_Adapter.ViewHolder>{

    public List<String> list;

    Context context;

    public assetinner_rv_Adapter(Context context, List<String> list){

        this.list=list;

        this.context=context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_inner_item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        String assets_list=list.get(position);

        holder.assetname.setText((position+1)+". "+assets_list);




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView assetname;


        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


            assetname=itemView.findViewById(R.id.listitemname);



        }

    }

}
