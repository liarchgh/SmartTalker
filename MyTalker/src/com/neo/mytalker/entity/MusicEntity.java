package com.neo.mytalker.entity;
import java.util.List;

public class MusicEntity {
	private String musicName;
	private long musicId;
	private List<String> artistNames;
	private String albumName;
	private String albumImageUrl;

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

	public String getAlbumImageUrl() {
		return albumImageUrl;
	}

	public void setAlbumImageUrl(String albumImageUrl) {
		this.albumImageUrl = albumImageUrl;
	}
	
	public MusicEntity(String musicName, long musicId) {
		super();
		this.musicName = musicName;
		this.musicId = musicId;
	}

	public MusicEntity(String musicName, long musicId, List<String> artistNames, String albumName,
			String albumImageUrl) {
		super();
		this.musicName = musicName;
		this.musicId = musicId;
		this.artistNames = artistNames;
		this.albumName = albumName;
		this.albumImageUrl = albumImageUrl;
	}
}
