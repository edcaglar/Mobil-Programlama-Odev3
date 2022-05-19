package android.example.fakeify;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder>{

    ArrayList<AudioClass> songList;
    Context context;

    public MusicListAdapter(ArrayList<AudioClass> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MusicListAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MusicListAdapter.ViewHolder holder, int position) {
        AudioClass songData = songList.get(position);
        holder.titleTextView.setText(songData.getTitle());


        holder.shareSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        holder.deleteSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(songData.getPath());

                File file = new File(songData.getPath());
                if(file.exists()) {
                    boolean deleted = file.delete();
                    if(deleted)
                        System.out.println("file Deleted :");
                    else
                        System.out.println("Not deleted :");
                }
                else
                    System.out.println("file not found :");

            }
        });

        holder.shareSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath = songData.getPath();
                Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", new File(filePath));

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                share.putExtra(Intent.EXTRA_STREAM, uri);

                Intent shareIntent = Intent.createChooser(share, "Send Song File");
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(shareIntent);

            }
        });

        if(MyMediaPlayer.currentIndex==position){
            holder.titleTextView.setTextColor(Color.parseColor("#FF0000"));
        }
        else {
            holder.titleTextView.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to another activity

                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("LIST", songList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        ImageView iconImageView;
        ImageView deleteSong;
        ImageView shareSong;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.music_title_text);
            iconImageView = (ImageView) itemView.findViewById(R.id.icon_view);
            deleteSong = (ImageView) itemView.findViewById(R.id.delete_song);
            shareSong = (ImageView) itemView.findViewById(R.id.share_song);

        }
    }
}
