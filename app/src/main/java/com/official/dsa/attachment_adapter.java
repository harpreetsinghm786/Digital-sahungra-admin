package com.official.dsa;


import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.official.dsa.lists.attachment_list;

import java.io.File;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class attachment_adapter extends RecyclerView.Adapter< attachment_adapter.ViewHolder> {

    private Context context;
    private List<attachment_list> uploads;

    public attachment_adapter(Context context, List<attachment_list> uploads) {
        this.uploads = uploads;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attachment_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final attachment_list list = uploads.get(position);
        holder.title.setText(list.getFilename()+"."+list.getType());


        try {
            if(list.getType().equals("jpg") || list.getType().equals("png") ){
                holder.imageView.setImageResource(R.mipmap.file_icon);
            }else if(list.getType().equals("mp3")){
                holder.imageView.setImageResource(R.mipmap.mp3);
            }else if(list.getType().equals("pdf")){
                holder.imageView.setImageResource(R.mipmap.pdf);
            }else{
                holder.imageView.setImageResource(R.mipmap.file);
            }
        }catch (Exception e){}


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(context, list.getFilename(), "."+list.getType(), DIRECTORY_DOWNLOADS, list.getUrl());

                Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView title;
        ImageView imageView;


        public ViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.filename);
            imageView = itemView.findViewById(R.id.icon);

            this.item = itemView;

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


