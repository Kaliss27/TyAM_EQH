package com.example.entregable3eqh;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

public class DetailsActivity extends Activity
{
    MediaPlayer player;
    Thread posThread;
    Uri mediaUri;
    int pos;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_details);

        SeekBar sbProgress = findViewById (R.id.sbProgress);
        sbProgress.setOnSeekBarChangeListener (new MySeekBarChangeListener ());

        player = new MediaPlayer ();
        mediaUri=getIntent().getData();
        player.setOnPreparedListener (mediaPlayer ->
        {
            posThread = new Thread (() ->
            {
                try
                {
                    while (player.isPlaying ())
                    {
                        Thread.sleep (1000);
                        sbProgress.setProgress (player.getCurrentPosition ());
                    }
                } catch (InterruptedException in)
                {
                    in.printStackTrace ();
                }
            });

            sbProgress.setMax (mediaPlayer.getDuration ());
            if (pos > -1) mediaPlayer.seekTo (pos);
            mediaPlayer.start ();
            posThread.start ();
        });

        ImageButton btnPlay = findViewById (R.id.imageButton_play);
        btnPlay.setOnClickListener (v ->
        {
            if (player.isPlaying ())
            {
                posThread.interrupt ();
                player.stop ();
                player.seekTo (pos);
                sbProgress.setProgress (pos);
                pos = player.getCurrentPosition();
            }
            try
            {
                player.setDataSource(getApplicationContext (), mediaUri);
                player.prepare ();
            }
            catch (IOException ex)
            {
                ex.printStackTrace ();
            }
        });

        ImageButton btnPause = findViewById (R.id.imageButton_pause);
        btnPause.setOnClickListener (v ->
        {
            pos = player.getCurrentPosition();
            if (player.isPlaying ())
            {
                posThread.interrupt ();
                player.stop ();
                player.seekTo (pos);
                sbProgress.setProgress (pos);
            }
        });
    }

    @Override
    protected void onSaveInstanceState (@NonNull Bundle outState)
    {
        super.onSaveInstanceState (outState);

        outState.putString ("SONG", mediaUri != null ? mediaUri.toString (): "");
        outState.putInt ("PROGRESS", player != null ?  player.getCurrentPosition () : pos);
        outState.putBoolean ("ISPLAYING", player != null && player.isPlaying ());

        if (player.isPlaying ())
        {
            posThread.interrupt ();
            player.stop ();
            player.seekTo (pos);
            player.release ();
            player = null;
        }
    }

    @Override
    protected void onRestoreInstanceState (@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState (savedInstanceState);

        mediaUri = Uri.parse (savedInstanceState.getString ("SONG"));
        pos = savedInstanceState.getInt ("PROGRESS");
        boolean isPlaying = savedInstanceState.getBoolean ("ISPLAYING");

        if (player == null) return;

        try
        {
            player.reset();
            player.setDataSource(getBaseContext(), mediaUri);
            if (isPlaying) player.prepareAsync();
            if (!isPlaying) player.stop();
        }
            catch (IOException | IllegalStateException ioex)
        {
            ioex.printStackTrace ();
        }
    }

    @Override
    protected void onDestroy ()
    {
        super.onDestroy();
        // cleanup
        if (player != null && player.isPlaying ())
        {
            player.stop ();
            player.release ();
        }

        player = null;
    }

    @Override
    protected void onResume()
    {
        super.onResume ();
    }

    @Override
    protected void onStart()
    {
        super.onStart ();

    }

    class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener
    {
        @Override
        public void onProgressChanged (SeekBar seekBar, int i, boolean b)
        {
            if (b)
            {
                player.pause ();
                player.seekTo (i);
                player.start ();
            }
        }

        @Override
        public void onStartTrackingTouch (SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch (SeekBar seekBar) {}
    }
}
