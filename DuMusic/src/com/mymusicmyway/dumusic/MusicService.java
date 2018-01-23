package com.mymusicmyway.dumusic;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;

/**
 * 在服务中对音乐进行操作
 * 
 * @author ly-lichengdong
 *
 */
public class MusicService extends Service {
	MyBinder binder = new MyBinder();
	MediaPlayer palyer = new MediaPlayer();// 播放音频视频
	//存储当前播放歌曲路径用于点击播放判断是否正在播放
	String currPath="";
	@Override
	public IBinder onBind(Intent Intent) {
		return binder;
	}
	public class MyBinder extends Binder {
		public MusicService getService() {
			return MusicService.this;
		}
	}
	/**
	 * 开始播放
	 */
	public void play(String fpath) {
		currPath=fpath;
		// 重置播放状态
		palyer.reset();
		// 根据路径播放
		try {
			palyer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			palyer.setDataSource(fpath);
			// 缓存 --- 缓冲
			palyer.prepare();
			// 缓存成功
			palyer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					palyer.start();
				}
			});
		} catch (Exception e) {
		  e.getMessage();
		}
	}

	/**
	 * 暂停播放
	 */
	public void pause() {
		if (palyer.isPlaying()) {
			palyer.pause();
		}
	}
	/**
	 * 继续播放
	 */
	public void continueMusic() {
		palyer.start();
	}

}
