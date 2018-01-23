package com.mymusicmyway.dumusic;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicInfoAdapter extends BaseAdapter {
	private List<MusicInfo> mMusicList;
	private Context context;
	private String art;
	private String name;
	public int index=-1;//当前播放
	public MusicInfoAdapter(Context context, List<MusicInfo> mMusicList) {
		this.mMusicList = mMusicList;
		this.context = context;
		
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mMusicList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mMusicList.get(arg0);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.music_item, null);
		}
		TextView artist = (TextView) view.findViewById(R.id.artist);
		TextView musicName = (TextView) view.findViewById(R.id.musicName);
		TextView time = (TextView) view.findViewById(R.id.time);
		TextView duMusic=(TextView)view.findViewById(R.id.dumusic);
		art=mMusicList.get(position).getArtist()+"   "+mMusicList.get(position).getAlbum();
		artist.setText(art.length()<=45?art:art.substring(0, 45)+"...");
		name=mMusicList.get(position).getName();
		
		//name="<MARQUEE onmouseover=this.stop() onmouseout=this.start() scrollAmount=3 scrollDelay=4 direction=left  >"+name+"</MARQUEE>";
		//CharSequence charSequence=Html.fromHtml(name);
		
		musicName.setText(name);
		time.setText(mMusicList.get(position).getDuration());
		if(index>=0 && position==index){
			view.setBackgroundResource(R.drawable.icon_playing);
			
			duMusic.setVisibility(View.VISIBLE);
		}else {
			musicName.setFocusableInTouchMode(false);
			view.setBackgroundResource(0);
			duMusic.setVisibility(View.GONE);
		}
		return view;
	}
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
}
