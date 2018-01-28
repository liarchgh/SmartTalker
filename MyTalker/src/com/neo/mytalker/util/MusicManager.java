package com.neo.mytalker.util;

import java.util.List;

import com.neo.mytalker.entity.MusicEntity;

import android.graphics.Bitmap;
import android.media.MediaPlayer;

public abstract class MusicManager {
	private MediaPlayer musicControler = null;
	private MusicEntity music = null;
	private List<MusicEntity>musicHistory;
	protected void init() {
		if(musicControler == null) {
			musicControler = new MediaPlayer();
		}
	}
	protected void musicPlay() {
		init();
		musicControler.start();
	}

	public List<MusicEntity>getMusicHistory(){
		return musicHistory;
	}
	public List<MusicEntity>searchMusicInNetease(String key){
		GetMusicUrl.getSongUrls(key);
		return null;
	}
	public abstract List<MusicEntity>searchMusicInNetease(MusicEntity key);
//	public abstract void musicPlayById(String musicId);
	public abstract void musicPlay(String musicId);
	public abstract void musicStop();
	public abstract void musicContinue();
	public abstract List<MusicEntity>getMusicDownloaded();
	public abstract Bitmap getMusicImageById(String musicId);
}
