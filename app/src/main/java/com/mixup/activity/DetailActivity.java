package com.mixup.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixup.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {


    private ImageView image_View, play, pause;
    private TextView artist_name_detail;
    private String track_name, artist_name, genere, preview_url, artist_image;

    private MediaPlayer mediaPlayer;
    private int playbackPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        artist_image = getIntent().getStringExtra("artist_image");
        preview_url = getIntent().getStringExtra("preview_url");
        genere = getIntent().getStringExtra("genere");
        artist_name = getIntent().getStringExtra("artist_name");
        track_name = getIntent().getStringExtra("track_name");

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back_lay);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;

            }
        });
        image_View = (ImageView) findViewById(R.id.image_View);
        Picasso.with(DetailActivity.this).load(artist_image).into(image_View);

        artist_name_detail = (TextView) findViewById(R.id.artist_name_detail);
        artist_name_detail.setText(artist_name);

        play = (ImageView) findViewById(R.id.play);
        pause = (ImageView) findViewById(R.id.pause);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    playAudio(preview_url);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    playbackPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                }
            }
        });

    }

    private void playAudio(final String url) throws Exception {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    killMediaPlayer();
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }catch (Throwable t){
                    t.printStackTrace();
                }


            }
        };
        new Thread(runnable).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
