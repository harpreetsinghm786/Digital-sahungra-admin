package com.official.dsa;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.official.dsa.lists.attachment_list;
import com.official.dsa.lists.complaint_list;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class complaints_adapter extends RecyclerView.Adapter< complaints_adapter.ViewHolder> {

    private Context context;
    private List<complaint_list> uploads;

    public complaints_adapter(Context context, List<complaint_list> uploads) {
        this.uploads = uploads;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complaints_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final complaint_list list = uploads.get(position);

        holder.title.setText(list.getKey());

        holder.timeago.setText(getTimeAgo(list.getDate())+"");

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(context,complaints_details.class);
                i.putExtra("key",list.getKey());
                context.startActivity(i);
            }
        });






    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView title,timeago;



        public ViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.filename);
            timeago=itemView.findViewById(R.id.timeago);

            this.item = itemView;

        }


    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(Date date) {
        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return "in the future";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }
}

