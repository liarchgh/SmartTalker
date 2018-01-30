package com.example.testandroid;
import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.widget.SectionIndexer;

public class MusicEntity implements Serializable{
	private String musicName;
	private long musicId;
	private List<String> artistNames;
	private String albumName;
	private String albumImageUri;

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

	public MusicEntity(String musicName, long musicId, List<String> artistNames, String albumName, String albumImageUri) {
		super();
		this.musicName = musicName;
		this.musicId = musicId;
		this.artistNames = artistNames;
		this.albumName = albumName;
		this.albumImageUri = albumImageUri;
	}
	
	public void play(Activity activity) {
		MusicManager.musicPlay(activity, this);
	}
}
