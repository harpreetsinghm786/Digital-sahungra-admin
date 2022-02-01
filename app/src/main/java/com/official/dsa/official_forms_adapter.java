package com.official.dsa;


import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.official.dsa.lists.complaint_list;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class official_forms_adapter extends RecyclerView.Adapter< official_forms_adapter.ViewHolder> {

    private Context context;
    private List<official_form_list> uploads;



    public official_forms_adapter(Context context, List<official_form_list> uploads) {
        this.uploads = uploads;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forms_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final official_form_list list = uploads.get(position);

        holder.title.setText(list.getName());

        holder.timeago.setText(getTimeAgo(list.getDate())+"");

        holder.description.setText(list.getDescription());

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();

                downloadFile(context, list.getName(), ".pdf", DIRECTORY_DOWNLOADS, list.getUrl());
            }
        });





    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public void filterlist(List<official_form_list> filterlist) {
        uploads = filterlist;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView title,timeago,description;

        LinearLayout moreabout_palte;
        Button download;


        public ViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.filename);
            timeago=itemView.findViewById(R.id.timeago);
            moreabout_palte=itemView.findViewById(R.id.moreabout_plate);
            download=itemView.findViewById(R.id.download);
            description=itemView.findViewById(R.id.form_description);

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


    public void downloadFile(Context context, String filename, String fileExtension, String destinationDirector, String url) {



        DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, filename + fileExtension);
        Intent mediascanner = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediascanner.setData(uri);
        context.sendBroadcast(mediascanner);
        downloadManager.enqueue(request);


    }
}

