package com.yhd.samples;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.yhd.hdmediaplayer.MediaPlayerHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {
    private static final String TAG="MainActivity";
    private final static String URL="http://file.xjkb.com/group1/M00/00/1A/eEzdEFh8Z-mAQdc6AcR3DvBgTPU994.mp4";
    private String loclURL = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/local_video.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
        // cpAssertVideoToLocalPath();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // MediaPlayerHelper.getInstance().setSurfaceView((SurfaceView)findViewById(R.id.surfaceView)).playAsset(MainActivity.this,"test.mp4");
                MediaPlayerHelper.getInstance().setSurfaceView((SurfaceView)findViewById(R.id.surfaceView)).play(loclURL);
            }
        },2000);
    }

    private void permission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
            @Override
            public void passPermissons() {
                Toast.makeText(MainActivity.this, "权限通过，可以做其他事情!", Toast.LENGTH_SHORT).show();
                cpAssertVideoToLocalPath();
            }

            @Override
            public void forbitPermissons() {

            }
        };
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);
    }

    public void cpAssertVideoToLocalPath() {
        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/local_video.mp4");
            myInput = this.getAssets().open("test.mp4");
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }

            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onEditClick(View v){
        if(v.getId()==R.id.urlButton){
            MediaPlayerHelper.getInstance().play(URL);
        }else if(v.getId()==R.id.assetsMP3Button){
            MediaPlayerHelper.getInstance().playAsset(MainActivity.this,"test.mp3");
        }else if(v.getId()==R.id.assetsMP4Button){
            MediaPlayerHelper.getInstance().playAsset(MainActivity.this,"test.mp4");
        }
    }

    protected void onStart(){
        super.onStart();
        MediaPlayerHelper.getInstance().setProgressInterval(1000).setMediaPlayerHelperCallBack(new MediaPlayerHelper.MediaPlayerHelperCallBack() {
            @Override
            public void onCallBack(MediaPlayerHelper.CallBackState state, MediaPlayerHelper mediaPlayerHelper, Object... args) {
                Log.v(TAG,"--"+state.toString());
                if(state== MediaPlayerHelper.CallBackState.PROGRESS){
                    int percent=(int)args[0];
                    Log.v(TAG,"--progress:"+percent);
                }
            }
        });
    }

    protected void onStop(){
        super.onStop();
        if(MediaPlayerHelper.getInstance().getMediaPlayer()!=null){
            MediaPlayerHelper.getInstance().getMediaPlayer().stop();
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        MediaPlayerHelper.getInstance().release();
    }
}
