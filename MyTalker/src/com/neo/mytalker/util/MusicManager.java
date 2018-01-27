package com.neo.mytalker.util;

import java.util.List;

import com.neo.mytalker.entity.MusicEntity;

import android.graphics.Bitmap;

public abstract class MusicManager {
	public abstract List<MusicEntity>getMusicHistory();
	public abstract List<MusicEntity>searchMusicInNetease(String key);
	public abstract List<MusicEntity>searchMusicInNetease(MusicEntity key);
	public abstract void musicPlayById(String musicId);
	public abstract void musicStop();
	public abstract void musicContinue();
	public abstract List<MusicEntity>getMusicDownloaded();
	public abstract Bitmap getMusicImageById(String musicId);
}
