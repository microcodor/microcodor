
package com.wxdroid.basemodule.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;


/**
 * Description: 截屏声音播放
 */
public class SnapUtil
{

    private MediaPlayer shootMP;

    public void playSnapshotSound(Context context) {
        AudioManager audioM =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = audioM.getStreamVolume(AudioManager.STREAM_MUSIC);

        if (volume != 0) {
            if (shootMP == null)
                shootMP =
                        MediaPlayer.create(context,
                                Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
            if (shootMP != null)
                shootMP.start();
        }
    }
}
