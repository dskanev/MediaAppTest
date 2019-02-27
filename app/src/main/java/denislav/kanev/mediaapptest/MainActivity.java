package denislav.kanev.mediaapptest;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    // Ui Components
    private VideoView myVideoView;
    private Button btnPlayVideo, btnPlayMusic, btnPauseMusic;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    private SeekBar volumeSeekBar;
    private AudioManager audioManager;
    private SeekBar moveSeekBar;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myVideoView = findViewById(R.id.myVideoView);

        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        btnPauseMusic = findViewById(R.id.btnPauseMusic);
        volumeSeekBar = findViewById(R.id.seekBarVolume);
        moveSeekBar = findViewById(R.id.seekBarMove);

        btnPlayVideo.setOnClickListener(MainActivity.this);
        btnPlayMusic.setOnClickListener(MainActivity.this);
        btnPauseMusic.setOnClickListener(MainActivity.this);

        mediaController = new MediaController(MainActivity.this);
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maximumVolumeOfUserDevice = audioManager.
                getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolumeOfUserDevice = audioManager.
                getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maximumVolumeOfUserDevice);
        volumeSeekBar.setProgress(currentVolumeOfUserDevice);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        moveSeekBar.setOnSeekBarChangeListener(MainActivity.this);
        moveSeekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnCompletionListener(MainActivity.this);
    }
        @Override
        public void onClick (View buttonView){

            switch (buttonView.getId()) {
                case R.id.btnPlayVideo:
                    Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
                    myVideoView.setVideoURI(videoUri);
                    myVideoView.setMediaController(mediaController);
                    mediaController.setAnchorView(myVideoView);
                    myVideoView.start();
                    break;
                case R.id.btnPlayMusic:
                    mediaPlayer.start();
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            moveSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    }, 0, 1000);
                    break;
                case R.id.btnPauseMusic:
                    mediaPlayer.pause();
                    timer.cancel();
                    break;
            }

        }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPlayer.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        timer.cancel();
    }
}


