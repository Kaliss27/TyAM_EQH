package com.example.entregable2eqh;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;

public class MainActivity extends Activity
{
    MediaPlayer player;
    Thread posThread;
    Runnable runnable;
    SeekBar sbProgress;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sbProgress = findViewById(R.id.sbProgress);
        handler = new Handler();

        player = new MediaPlayer();
        player.setOnPreparedListener(mediaPlayer -> {
            sbProgress.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
        });

        Button btnAudio1 = findViewById(R.id.btnAudio1);
        btnAudio1.setOnClickListener(v -> {

            if (player.isPlaying())
            {
                player.stop();
                player.seekTo(0);
                sbProgress.setProgress(0);
                changeSeekBar();
            }

            Uri mediaUri = Uri.parse("android.resource://" + getBaseContext().getPackageName() + "/" + R.raw.mr_blue_sky);
            try
            {
                player.setDataSource(getBaseContext(), mediaUri);
                player.prepare();
                Toast.makeText(getApplicationContext(), "Now playing: Mr. Blue Sky", Toast.LENGTH_LONG).show();
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }

        });

        Button btnAudio2 = findViewById(R.id.btnAudio2);
        btnAudio2.setOnClickListener(v -> {

            if (player.isPlaying())
            {
                player.stop();
                player.seekTo(0);
                sbProgress.setProgress(0);
                changeSeekBar();
            }

            Uri mediaUri = Uri.parse("android.resource://" + getBaseContext().getPackageName() + "/" + R.raw.lake_shore_drive);
            try
            {
                player.setDataSource(getBaseContext(), mediaUri);
                player.prepare();
                Toast.makeText(getApplicationContext(), "Now playing: Lake Shoe Drive", Toast.LENGTH_LONG).show();
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }

        });

        Button btnAudio3 = findViewById(R.id.btnAudio3);
        btnAudio3.setOnClickListener(v -> {

            if (player.isPlaying())
            {
                player.stop();
                player.seekTo(0);
                sbProgress.setProgress(0);
                changeSeekBar();
            }

            Uri mediaUri = Uri.parse("android.resource://" + getBaseContext().getPackageName() + "/" + R.raw.fox_on_the_run);
            try
            {
                player.setDataSource(getBaseContext(), mediaUri);
                player.prepare();
                Toast.makeText(getApplicationContext(), "Now playing: Fox On The Run", Toast.LENGTH_LONG).show();
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }

        });

        player.setOnPreparedListener(mp -> {
            sbProgress.setMax(player.getDuration());
            player.start();
            changeSeekBar();
        });

        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    player.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    private void changeSeekBar()
    {
        sbProgress.setProgress(player.getCurrentPosition());
        if(player.isPlaying())
        {
            runnable = () -> changeSeekBar();
            handler.postDelayed(runnable, 1000);
        }
    }
    @Override
    protected void onDestroy () {
        super.onDestroy();
        // cleanup
        super.onStop();
        if (player.isPlaying()) {
            player.stop();
            player.release();
        }
        player = null;
    }
}


