package android.example.fakeify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

public class BroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.example.EXAMPLE_ACTION".equals(intent.getAction())) {
            String receivedText = intent.getStringExtra("com.example.EXTRA_TEXT");
            Toast.makeText(context, receivedText, Toast.LENGTH_SHORT).show();
            adjustVolume(receivedText);
        }
    }

    public void adjustVolume(String string){
        int count;
        if(string.equals("VolumeUp")) {
            for (count = 0; count < 20; count++) {
                MusicPlayerActivity.audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            }
        }
        if(string.equals("VolumeDown")){
            for (count = 0;count<20;count++) {
                MusicPlayerActivity.audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            }
        }

    }
}