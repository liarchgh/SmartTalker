package com.neo.mytalker.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.entity.MusicEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

public abstract class MusicManager {
	public static enum MUSIC_STATUS {
		PLAY_SUCCESS,
		PLAY_NOT_FOUND,
		STOP_SUCCESS,
		STOP_NO_MUSIC,
		CONTINUE_SUCCESS,
		CONTINUE_IS_PLAYING,
		NEXT_SUCCESS,
		NEXT_NOT_FOUND,
		PREVIOUS_SUCCESS,
		PREVIOUS_NOT_FOUND,
		ERROR,
		FOLDER;
	}
	
	public static final String musicFolderHolder = File.separator+"music",
			fileFolderHolder = File.separator+"file",
			donwloadList = File.separator+"musicList",
			imageFolderHolder = File.separator+"image";
	public static String musicFolder = null;
	private static Context context = null;
	private static MediaPlayer musicControler = null;
	private static MusicEntity musicNow = null;
	private static List<MusicEntity>musicHistory = new ArrayList<MusicEntity>(),
			musicDownloaded = null;
	public static void init(Context ct) {
		context = ct;
		if(musicControler == null) {
			musicControler = new MediaPlayer();
		}
		if(musicFolder == null) {
			musicFolder = context.getFilesDir().getPath()+musicFolderHolder;
		}
		if(musicDownloaded == null) {
			musicDownloaded = readDownloadedMusicList();
		}
	}
	
	private static void musicPlay(Context at, String uri) {
		init(at);
//		musicPlay();
		try {
			if(musicControler != null && musicControler.isPlaying()) {
				musicControler.stop();
			}
			musicControler = new MediaPlayer();
			musicControler.setDataSource(uri);
			musicControler.prepare();
			musicControler.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//播放音乐
	public static MusicEntity musicPlay(Context at, MusicEntity music) {
		init(at);
		//判断是否是当前正在播放的歌曲，是的话直接返回当前播放对象
		if(music != null && musicNow != null && 
				music.getMusicId() == musicNow.getMusicId()) {
			return musicNow;
		}
		try {
			//检测是否已下载，已下载的话、引用改为从下载列表里查出的对象，方便判断在列表里的位置
			String uri = findDownloaded(at, music);
			if(uri == null || uri.equals("")) {
				uri = GetMusicUrl.getSongUrlById(music.getMusicId());
				musicDownload(music);
			}
			else {
				music = findDownloadById(at, music.getMusicId());
			}
			//没有结果 直接返回 表示失败
			if(uri == null || uri.equals("")) {
				return null;
			}
			
			saveAlbumImage(music);
	
			//更新当前歌曲
			musicNow = music;
			MusicEntity temp = addHistory(musicNow);
			if(temp != null) {
				musicNow = temp;
			}
			musicPlay(at, uri);
			return musicNow;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return musicNow;
	}

	private static void musicDownload(final MusicEntity music) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(4000);
					File file = new File(musicFolder);
					if(!file.exists()) {
						file.mkdir();
					}
					file = new File(file.getPath()+fileFolderHolder);
					if(!file.exists()) {
						file.mkdir();
					}
					NetUtil.doGetMusic(GetMusicUrl.getSongUrlById(music.getMusicId()),
							file.getPath()+File.separator+music.getMusicId());
					saveDownloadedMusicList(music);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void musicContinue() {
		if(musicControler != null) {
			musicControler.start();
		}
	}
	
	public static MUSIC_STATUS musicPrevious(Context activity) {
		init(activity);
		int index = musicHistory.indexOf(musicNow);
		if(index > 0) {
//			musicNow = musicHistory.get(index - 1);
//			musicNow.play(activity);
			musicHistory.get(index - 1).play(activity);
			return MUSIC_STATUS.PREVIOUS_SUCCESS;
		}
		return MUSIC_STATUS.PREVIOUS_NOT_FOUND;
	}
	
	public static MUSIC_STATUS musicNext(Context activity) {
		init(activity);
		int index = musicHistory.indexOf(musicNow);
		if(index < musicHistory.size() - 1) {
//			musicNow = musicHistory.get(index + 1);
//			musicNow.play(activity);
			musicHistory.get(index + 1).play(activity);
			return MUSIC_STATUS.NEXT_SUCCESS;
		}
			return MUSIC_STATUS.NEXT_NOT_FOUND;
	}

	public static MUSIC_STATUS musicStop(Context at) {
		init(at);
		if(musicControler != null && musicControler.isPlaying()) {
			musicControler.pause();
			return MUSIC_STATUS.STOP_SUCCESS;
		}
		else {
			return MUSIC_STATUS.STOP_NO_MUSIC;
		}
	}

	public static List<MusicEntity>getMusicHistory(){
		return musicHistory;
	}
	public static List<MusicEntity>searchMusicInNetease(String key){
		return GetMusicUrl.searchMusicByKey(key);
	}
//	public abstract void musicPlayById(String musicId);
	public static void musicContinue(Context at) {
		init(at);
		musicControler.start();
	}
	public static List<MusicEntity>getMusicDownloaded(){
		return null;
	}
	public static Bitmap getMusicImageById(long musicId) {
		return null;
	}
	
	private static String findDownloaded(Context at, MusicEntity music) {
		init(at);
		MusicEntity res = null;
		String uri = musicFolder+fileFolderHolder+File.separator+music.getMusicId();
		for(Iterator<MusicEntity>it = musicDownloaded.iterator();
				it.hasNext();) {
			res = it.next();
			if(res.getMusicId() == music.getMusicId()) {
				return uri;
			}
		}
//		File file = new File(uri);
//Log.i("music", "uri:"+uri);
//		if(file.exists()) {
//			return uri;
//		}
		return null;
	}

	public static MusicEntity findDownloadById(Context at, long musicId) {
		MusicEntity res = null;
		for(Iterator<MusicEntity>it = musicDownloaded.iterator();
				it.hasNext();) {
			res = it.next();
			if(res.getMusicId() == musicId) {
				return res;
			}
		}
		return null;
	}
	
	private static boolean saveDownloadedMusicList(MusicEntity music) {
		musicDownloaded.add(music);
		return saveDownloadMusicList();
	}

	private static boolean saveDownloadMusicList() {
		File file = new File(musicFolder+donwloadList);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(file));
			oos.writeObject(musicDownloaded);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	private static List<MusicEntity>readDownloadedMusicList(){
		File file = new File(musicFolder+donwloadList);
		if(!file.exists()) {
			return new ArrayList<MusicEntity>();
		}
		try {
			ObjectInputStream oos = new ObjectInputStream(
					new FileInputStream(file));
			return (List<MusicEntity>)oos.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<MusicEntity>();
	}
	public static MusicEntity addHistory(MusicEntity musicNow) {
		MusicEntity temp = null;
		for(Iterator<MusicEntity>it = musicHistory.iterator();
				it.hasNext(); ) {
			temp = it.next();
			if(temp.getMusicId() == musicNow.getMusicId()) {
				return temp;
			}
		}
		musicHistory.add(musicNow);
		return null;
	}
	public static Bitmap getAlbumImage(Context context, MusicEntity music) {
		String uri = musicFolder+imageFolderHolder+File.separator+music.getAlbumId();
		File file = new File(uri);
		if(file.exists()) {
			return BitmapFactory.decodeFile(uri);
		}
		return NetUtil.doGetBitmap(music.getAlbumImageUri(), null);
	}
	public static boolean saveAlbumImage(MusicEntity music) {
        try {
			String uri = musicFolder+imageFolderHolder+File.separator+music.getAlbumId();
			File file = new File(uri);
			if(file.exists()) {
				return false;
			}
			file.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));   
			music.getAlbumImage().compress(Bitmap.CompressFormat.JPEG, 100, bos);   
			bos.flush();
			bos.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
        return false;
	}
	public static boolean musicIsPlaying() {
		if(musicControler != null && musicControler.isPlaying()) {
			return true;
		}
		return false;
	}

	public static MusicEntity getMusicNow() {
		return musicNow;
	}
}
