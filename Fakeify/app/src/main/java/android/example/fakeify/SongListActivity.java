package android.example.fakeify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<AudioClass> songList = new ArrayList<>();
    ImageView deleteSong;
    ImageView menuImage;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        noMusicTextView = (TextView) findViewById(R.id.no_songs_text);
        deleteSong = (ImageView) findViewById(R.id.delete_song);
        menuImage = (ImageView) findViewById(R.id.menu_img);
        navigationView = (NavigationView) findViewById(R.id.navigationView);



        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = navigationView.getVisibility();
                if(visibility==View.VISIBLE){
                    navigationView.setVisibility(View.GONE);
                }
                else{
                    navigationView.setVisibility(View.VISIBLE);
                }
            }
        });


        if(checkPermission() == false){
            requestPermission();
            return;
        }


        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, sortOrder);
        while(cursor.moveToNext()) {
            AudioClass songData = new AudioClass(cursor.getString(1), cursor.getString(0), cursor.getString(2));
            if (new File(songData.getPath()).exists())
                songList.add(songData);




            if(songList.size() == 0){
                noMusicTextView.setVisibility(View.VISIBLE);
            }
            else{
                //recylcerview
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new MusicListAdapter(songList, getApplicationContext()));
            }

        }
    }

    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(SongListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }

    }

    void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(SongListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(SongListActivity.this,"Read permission is required. Please allow it from settings.", Toast.LENGTH_SHORT).show();
        }
        else{
            ActivityCompat.requestPermissions(SongListActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new MusicListAdapter(songList, getApplicationContext()));
        }
    }
}