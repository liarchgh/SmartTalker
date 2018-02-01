package com.neo.mytalker.entity;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.util.MusicManager;

import android.graphics.Bitmap;

public class MusicEntity implements Serializable{
	private String musicName;
	private long musicId;
	private List<String> artistNames;
	private String albumName;
	private String albumImageUri;
	private long albumId;
	private Bitmap albumImage = null;

	public Bitmap getAlbumImage() {
		return albumImage;
	}

	public void setAlbumImage(Bitmap albumImage) {
		this.albumImage = albumImage;
	}

	public long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}

	public String getMusicName() {
		return musicName;
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public long getMusicId() {
		return musicId;
	}

	public void setMusicId(long musicId) {
		this.musicId = musicId;
	}

	public List<String> getArtistNames() {
		return artistNames;
	}

	public void setArtistNames(List<String> artistNames) {
		this.artistNames = artistNames;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumImageUri() {
		return albumImageUri;
	}

	public void setAlbumImageUri(String albumImageUri) {
		this.albumImageUri = albumImageUri;
	}

	public MusicEntity(String musicName, long musicId) {
		super();
		this.musicName = musicName;
		this.musicId = musicId;
	}

	public MusicEntity(String musicName, long musicId, List<String> artistNames, String albumName, String albumImageUri,
			long albumId) {
		super();
		this.musicName = musicName;
		this.musicId = musicId;
		this.artistNames = artistNames;
		this.albumName = albumName;
		this.albumImageUri = albumImageUri;
		this.albumId = albumId;
	}

	public void play(ChatActivity activity) {
		MusicManager.musicPlay(activity, this);
		MusicManager.loadMusicInfoToNotification(this, activity);
//		MusicManager.loadMusicOnlyButton(activity);
	}
	public class RequestBody{
		public resBody result;
		public long code;
	}
	public class resBody{
		public List<song> songs;
		public long songCount;
	}
	public class song{
		public long id;
		public String name;
		public List<artist> ar;
		public album al;
	}
	public class album{
		public long id;
		public String name;
		public String picUrl;
	}
	public class artist{
		public long id;
		public String name;
	}

	public Bitmap loadAlbumBitmap(ChatActivity context) {
//		if(getAlbumImage() != null) {
//			return getAlbumImage();
//		}
		Bitmap bm = MusicManager.getAlbumImage(context, this);
//		MusicManager.saveAlbumImage(this);
//		return getAlbumImage();
//Log.i("bm", "url:"+getAlbumImageUri());
//		Bitmap bm = NetUtil.doGetBitmap(getAlbumImageUri(), null);
//Log.i("bm", "height:"+bm.getHeight());
		return bm;
	}
	
	public boolean isPlaying() {
		return MusicManager.isPlaying(this);
	}
	
	public String getArtistsNamesToString() {
		StringBuffer ars = new StringBuffer();
		for(Iterator<String>ar = getArtistNames().iterator();
				true;) {
			ars.append(ar.next());
			if(ar.hasNext()) {
				ars.append(",");
			}
			else {
				break;
			}
		}
		return ars.toString();
	}
}
