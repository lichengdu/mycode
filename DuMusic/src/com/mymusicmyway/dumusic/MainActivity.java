package com.mymusicmyway.dumusic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mymusicmyway.dumusic.MusicService.MyBinder;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.platformtools.Util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author ly-lichengdong 1、了解音乐播放器原理 2、设计UI 3、初始化音乐资源 4、创建音乐服务 5、兼容2.X退出界面自动解绑
 *         6、更新时间进度和音乐进度 timer和handler 7、音量 8、发通知
 * 
 */
public class MainActivity extends Activity implements OnClickListener, OnItemClickListener {

	private static final int THUMB_SIZE = 150;
	private IWXAPI api;
	private static final String APP_ID = "wxe41d914652333c5f";

	private ImageButton btnPlay;
	private ImageButton btnPalyNext;
	private SeekBar btnSeekbar;
	private TextView musicName;
	private TextView artist;
	private TextView position;
	private TextView duration;
	private ListView music_listview;
	private TextView topTv;

	private int playType;
	private String[] playTypeNames = { "顺序播放", "随机播放", "单曲播放" };
	// 本地缓存
	private SharedPreferences mySharedPreferences;
	private Editor editor;
	private int index;

	// 记录上一次播放状态 播放true 暂停 false
	private boolean prePlayStatus;
	private int preIndex;
	private TextView autoUp;
	private TextView autoDown;
	private ImageButton btnPlayPre;
	private ImageButton btn_menu2;

	private List<MusicInfo> infos = new ArrayList<MusicInfo>();
	private MyServiceConnection conn;
	public MediaPlayer musicPalyer;
	public MusicService mService;
	private Handler myHandler = new Handler();
	Random random = new Random();
	private AudioManager au;
	private MusicInfoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_main);

		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.registerApp(APP_ID);

		// 获得本地缓存设置
		mySharedPreferences = getSharedPreferences("myShared", Activity.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		editor = mySharedPreferences.edit();
		initView();
		// 获得本地存储的用户播放模式
		playType = mySharedPreferences.getInt("playType", -1);
		if (playType == -1) {
			// 初始化播放模式
			playType = 0;// 顺序播放
			topTv.setText(playTypeNames[playType]);
			editor.putInt("playType", playType);
			editor.commit();
		}
		// 获取之前播放歌曲和状态
		prePlayStatus = mySharedPreferences.getBoolean("prePlayStatus", false);
		if(prePlayStatus){
			btnPlay.setImageResource(R.drawable.icon_pause_normal);
		}else{
			btnPlay.setImageResource(R.drawable.icon_play_normal);
		}
		preIndex = mySharedPreferences.getInt("preIndex", 0);

		// 加载音乐列表
		initMusicInfos();
		// 音乐列表为空
		if (infos.size() == 0) {
			Toast.makeText(getApplicationContext(), "无可播放音乐", Toast.LENGTH_SHORT).show();

		}
		if (preIndex >= infos.size()) {
			preIndex = 0;
		}
		index = preIndex;
		// 先注册服务连接音乐播放服务 两种方式 startService binderService
		// Intent intent = new Intent("com.example.mymusicmyway.MusicService");
		Intent intent = new Intent(this, MusicService.class);
		conn = new MyServiceConnection();
		// 兼容2.X退出自动解绑
		startService(intent);
		// 绑定服务
		if (infos.size() > 0) {
			bindService(intent, conn, Context.BIND_AUTO_CREATE);
		}
		// 控制音量
		au = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// 设置歌曲列表

		adapter = new MusicInfoAdapter(this, infos);
		adapter.index = index;
		music_listview.setAdapter(adapter);
		music_listview.setSelection(index);
	}

	/**
	 * 音乐播放同步更新进度线程
	 */
	private Runnable music_run = new Runnable() {
		@Override
		public void run() {
			btnSeekbar.setProgress(musicPalyer.getCurrentPosition());
			position.setText(secondMills2String(musicPalyer.getCurrentPosition()));
			if (!infos.get(index).getName().equals(musicName.getText().toString())) {
				String name = infos.get(index).getName();
				musicName.setText(name.length() <= 13 ? name : name.substring(0, 13) + "...");
				btnSeekbar.setMax(musicPalyer.getDuration());
				String art = infos.get(index).getArtist();
				artist.setText(art.length() <= 13 ? art : art.substring(0, 13) + "...");
				duration.setText(infos.get(index).getDuration());

			}
			// 每隔一秒
			myHandler.postDelayed(music_run, 1000);
		}
	};

	/*
	 * 加载音乐列表
	 */
	public void initMusicInfos() {
		// 初始化音乐文件
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor cursor = null;
		cursor = getContentResolver().query(uri, null, null, null, null);
		if (cursor == null) {
			return;
		}
		// Cursor
		while (cursor.moveToNext()) {
			// 一首歌曲是一个对象类
			String name = cursor.getString(cursor.getColumnIndex(Audio.Media.TITLE));
			String path = cursor.getString(cursor.getColumnIndex(Audio.Media.DATA));
			int duration = cursor.getInt(cursor.getColumnIndex(Audio.Media.DURATION));
			String artist = cursor.getString(cursor.getColumnIndex(Audio.Media.ARTIST));
			String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
			// 排除损坏的文件
			if (name != null && path != null && duration != 0 && secondMills2Int(duration) > 2) {
				MusicInfo info = new MusicInfo();
				info.setName(name);
				info.setFpath(path);
				// 返回毫秒如两分钟 1000*60*2
				info.setDuration(secondMills2String(duration));
				info.setArtist(artist);
				info.setAlbum(album);
				infos.add(info);
			}
		}

	}

	/*
	 * 后台播放服务
	 */
	class MyServiceConnection implements ServiceConnection {
		/**
		 * 必须在配置中注册服务才会回调连接成功执行
		 */
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicService.MyBinder binder = (MyBinder) service;
			mService = binder.getService();
			musicPalyer = mService.palyer;
			musicPalyer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer arg0) {
					if (playType == 0) {
						playMusic(2, 0);
					} else if (playType == 1) {
						playMusic(0, random.nextInt(infos.size() % (infos.size() - 0 + 1) + 0));
					} else if (playType == 2) {
						playMusic(2, index--);
					} else {
						Toast.makeText(getApplicationContext(), "无法获得播放模式", Toast.LENGTH_SHORT).show();
					}
				}
			});

			prePlayStatus = mySharedPreferences.getBoolean("prePlayStatus", false);
			// preIndex = mySharedPreferences.getInt("preIndex", 0);

			index = preIndex;
			String name1 = infos.get(index).getName();
			musicName.setText(name1);
			btnSeekbar.setMax(musicPalyer.getDuration());
			String art = infos.get(index).getArtist();
			artist.setText(art.length() <= 13 ? art : art.substring(0, 13) + "...");
			duration.setText(infos.get(index).getDuration());
			myHandler.removeCallbacks(music_run);
			myHandler.post(music_run);

			// 当app启动播放第一首歌曲
			if (!musicPalyer.isPlaying()) {
				if (prePlayStatus) {
					playMusic(0, index);
				}
			} else {
				btnPlay.setImageResource(R.drawable.icon_pause_normal);
			}
		}

		/**
		 * 断开执行
		 */
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
		}

	}

	public void playMusic(int status, int i) {
		// 状态
		switch (status) {
		case 1: // 上一曲
			index--;
			if (index < 0) {
				index = infos.size() - 1;
			}
			break;
		case 2: // 下一曲
			index++;
			if (index > infos.size() - 1) {
				index = 0;
			}

			break;

		default:
			index = i;
		}
		if (musicPalyer != null) {
			if (infos.size() > 0) {
				mService.play(infos.get(index).getFpath());
				String name = infos.get(index).getName();
				musicName.setText(name);
				btnSeekbar.setMax(musicPalyer.getDuration());
				String art = infos.get(index).getArtist();
				artist.setText(art.length() <= 13 ? art : art.substring(0, 13) + "...");
				duration.setText(infos.get(index).getDuration());
				myHandler.removeCallbacks(music_run);
				myHandler.post(music_run);
				adapter.index = index;
				// music_listview.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				// music_listview.setSelection(index);
				// 更新并记录播放状态
				prePlayStatus = true;
				editor.putBoolean("prePlayStatus", prePlayStatus);
				preIndex = index;
				editor.putInt("preIndex", preIndex);
				editor.commit();
				// 开启动画
				startResAnimation();
				// myAnimView.reStart=true;
				// myAnimView.invalidate();
			}

		}
	}

	/*
	 * 加载控件绑定监听事件
	 */
	private void initView() {
		btnPlay = (ImageButton) findViewById(R.id.btn_play);
		btnPlayPre = (ImageButton) findViewById(R.id.btn_playPre);
		btnPalyNext = (ImageButton) findViewById(R.id.btn_playNext);
		btnSeekbar = (SeekBar) findViewById(R.id.playback_seekbar2);

		musicName = (TextView) findViewById(R.id.musicname_tv2);
		musicName.setMovementMethod(LinkMovementMethod.getInstance());
		artist = (TextView) findViewById(R.id.artist_tv2);
		position = (TextView) findViewById(R.id.position_tv2);
		duration = (TextView) findViewById(R.id.duration_tv2);

		music_listview = (ListView) findViewById(R.id.music_listview);
		btn_menu2 = (ImageButton) findViewById(R.id.btn_menu2);

		topTv = (TextView) findViewById(R.id.topTv);
		topTv.setOnClickListener(this);

		//myAnimView = (MyAnimView) findViewById(R.id.myAnimView);

		musicName.setOnClickListener(this);

		// 获得音量加减
		autoUp = (TextView) findViewById(R.id.auto_up);
		autoDown = (TextView) findViewById(R.id.auto_down);
		// 设置上一曲下月一曲
		btnPlay.setOnClickListener(this);
		btnPalyNext.setOnClickListener(this);
		btnPlayPre.setOnClickListener(this);
		// 监听
		autoUp.setOnClickListener(this);
		autoDown.setOnClickListener(this);
		btn_menu2.setOnClickListener(this);
		music_listview.setOnItemClickListener(this);
		// 设置拖动
		btnSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean fromUser) {
				if (fromUser) {
					if(musicPalyer==null){
						Toast.makeText(getApplicationContext(), "当前无播放任务", Toast.LENGTH_SHORT).show();
						return;
					}
						
					if (musicPalyer.isPlaying()) {
						musicPalyer.seekTo(btnSeekbar.getProgress());
					}

				}
			}
		});
	}

	public String secondMills2String(int mills) {
		int seconds = mills / 1000;
		int second = seconds % 60;
		int minutes = (seconds - second) / 60;
		// 格式化
		DecimalFormat df = new DecimalFormat("00");
		return df.format(minutes) + ":" + df.format(second);
	}

	public int secondMills2Int(int mills) {
		int seconds = mills / 1000;
		int second = seconds % 60;
		int minutes = (seconds - second) / 60;
		// 格式化
		return minutes;
	}

	/**
	 * 解绑服务
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (conn != null) {
			unbindService(conn);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// 重新加载歌曲信息
		if(prePlayStatus){
			btnPlay.setImageResource(R.drawable.icon_pause_normal);
		}else{
			btnPlay.setImageResource(R.drawable.icon_play_normal);
		}
		index = preIndex;
		String name = infos.get(index).getName();
		musicName.setText(name);
		btnSeekbar.setMax(musicPalyer.getDuration());
		String art = infos.get(index).getArtist();
		artist.setText(art.length() <= 13 ? art : art.substring(0, 13) + "...");
		duration.setText(infos.get(index).getDuration());
		myHandler.removeCallbacks(music_run);
		myHandler.post(music_run);
		

	}

	@Override
	protected void onPause() {
		// 停止线程防止占用资源
		super.onPause();
		myHandler.removeCallbacks(music_run);
	};

	/**
	 * 获得点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 判断点击的是谁
		switch (v.getId()) {
		case R.id.btn_play:
			// 点击播放
			// 如果在播放就暂停，反之播放
			if (musicPalyer.isPlaying()) {
				// 移出线程释放资源
				prePlayStatus = false;
				myHandler.removeCallbacks(music_run);
				mService.pause();
				btnPlay.setImageResource(R.drawable.icon_play_normal);
			} else {
				prePlayStatus = true;
				startResAnimation();
				// 启动线程
				myHandler.removeCallbacks(music_run);
				myHandler.post(music_run);
				mService.continueMusic();
				btnPlay.setImageResource(R.drawable.icon_pause_normal);
			}

			editor.putBoolean("prePlayStatus", prePlayStatus);
			editor.commit();
			break;
		case R.id.btn_playNext:
			// 下一曲
			playMusic(2, 0);
			break;
		case R.id.btn_playPre:
			// 随机
			playMusic(0, random.nextInt(infos.size() % (infos.size() - 0 + 1) + 0));
			break;
		case R.id.auto_up:
			// 音量+
			au.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			break;
		case R.id.auto_down:
			// 音量-
			au.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			break;
		case R.id.btn_menu2:
			// 列表
			//shareWeix();
			sendImgToWX(myShot(this));
			break;
		case R.id.topTv:
			// 切换播放模式
			if (playType == 0) {
				playType = playType + 1;
			} else if (playType == 1) {
				playType = playType + 1;
			} else if (playType == 2) {
				playType = playType - playTypeNames.length + 1;
			}
			topTv.setText(playTypeNames[playType]);
			editor.putInt("playType", playType);
			editor.commit();
			break;

		case R.id.musicname_tv2:
			music_listview.setSelection(index);
			break;
		default:
			break;
		}
	}

	

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	/**
	 * 监听listviewItem的点击事件如果适配器中重写了onclick事件则会失效
	 */
	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
		if (!mService.currPath.equals(infos.get(position).getFpath())) {
			index = position;
			playMusic(0, position);
		} else {
			Toast.makeText(getApplicationContext(), "当前歌曲正在播放", Toast.LENGTH_SHORT).show();
		}
	}

	// 动画
	@SuppressLint("NewApi")
	private void startResAnimation() {
		// 显示歌名
		topTv.setText(infos.get(index).getName());
		ObjectAnimator moveln = ObjectAnimator.ofFloat(topTv, "translationX", -200f, 0f);
		ObjectAnimator rotate = ObjectAnimator.ofFloat(topTv, "rotation", 0f, 360f);
		ObjectAnimator fadeOut = ObjectAnimator.ofFloat(topTv, "alpha", 1f, 0f, 1f);

		ObjectAnimator changeBig = ObjectAnimator.ofFloat(topTv, "scaleY", 1f, 3f, 1f);
		AnimatorSet set = new AnimatorSet();
		rotate.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				// 动画完成显示播放模式
				topTv.setText(playTypeNames[playType]);
			}
		});
		set.play(rotate).with(changeBig).with(fadeOut).after(moveln);
		set.setDuration(3500);
		set.start();

	}
	
	private void shareWeix() {
		String text = "我正在用DuMusic听歌，赶紧下载体验https://lichengdu.github.io/htm/DuMusic.apk";
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = text;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		req.scene = true ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

		// 调用api接口发送数据到微信
		boolean flag = api.sendReq(req);
	}

	public void sendImgToWX(Bitmap bmp) {

		WXImageObject imgObj = new WXImageObject(bmp);
		String text = "我正在用DuMusic听歌，赶紧下载体验https://lichengdu.github.io/htm/DuMusic.apk";
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true); // 设置缩略图

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);

	}

	public Bitmap myShot(Activity activity) {
		// 获取windows中最顶层的view
		View view = activity.getWindow().getDecorView();
		view.buildDrawingCache();
		// 获取状态栏高度
		Rect rect = new Rect();
		view.getWindowVisibleDisplayFrame(rect);
		int statusBarHeights = rect.top;
		Display display = activity.getWindowManager().getDefaultDisplay();
		// 获取屏幕宽和高
		int widths = display.getWidth();
		int heights = display.getHeight();
		// 允许当前窗口保存缓存信息
		view.setDrawingCacheEnabled(true);
		// 去掉状态栏
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeights, widths,
				heights - statusBarHeights);
		// 销毁缓存信息
		view.destroyDrawingCache();
		return bmp;

	}
}
