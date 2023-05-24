package com.example.video;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.InCallService;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
//lokal video:
    private static final String VIDEO_SAMPLE_LOCAL = "video1";

//    video youtube:
    private static final String VIDEO_SAMPLE = "https://www.youtube.com/watch?v=jYIcxdDuGMk&ab_channel=SalPriadi";

    private TextView mBufferingTextView;
    //    merefer video dari xml
    private VideoView mVideoView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = findViewById(R.id.videoView);
        mBufferingTextView = findViewById(R.id.buffering_textView);

//        untuk controller video (play, pause, seek) yang dibawah
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);

//        menambahkan callback toast ketika video selesai
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_SHORT).show();
//            mengembalikan controller ke menit ke1
            mVideoView.seekTo(1);
            mVideoView.start();
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mBufferingTextView.setVisibility(VideoView.INVISIBLE);
                mVideoView.seekTo(1);
                mVideoView.start();
            }
        });
    }

    private Uri getMedia(String mediaName){
//        untuk mengecek apakah urlnya valid, kalau tidak valid play yang lokal
        if (URLUtil.isValidUrl(mediaName)){
            return Uri.parse(mediaName);
        }else {
//        untuk video lokal:
            return Uri.parse("android.resource://" + getPackageName()
                    + "/raw/" + VIDEO_SAMPLE_LOCAL);
        }
    }

    private void initializePlayer(){
        mBufferingTextView.setVisibility(VideoView.VISIBLE);
        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);
        mVideoView.start();
    }

    private void releasePlayer(){
        mVideoView.stopPlayback();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            mVideoView.pause();
        }
    }
}