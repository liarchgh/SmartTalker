package com.example.testandroid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.util.Log;

public abstract class MusicManager {
	private static final String musicFolderHolder = File.separator+"music",
			fileFolderHolder = File.separator+"file",
			imageFolderHolder = File.separator+"image";
	private static String musicFolder = null;
	private static Activity activity = null;
	private static MediaPlayer musicControler = null;
	private static MusicEntity musicNow = null;
	private static Queue<MusicEntity>musicHistory = new LinkedList<MusicEntity>();
	private static List<MusicEntity>musicDownloaded = new ArrayList<MusicEntity>();
	public static void init(Activity at) {
		activity = at;
		if(musicControler == null) {
			musicControler = new MediaPlayer();
		}
		if(musicFolder == null) {
			musicFolder = activity.getFilesDir().getPath()+musicFolderHolder;
		}
	}
	
//	public static void musicPlay(Activity at) {
//		init(at);
//		try {
//			musicControler.prepare();
//			musicControler.start();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	private static void musicPlay(Activity at, String uri) {
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
	
	public static void musicPlay(Activity at, MusicEntity music) {
			try {
				String uri = findDownloaded(at, music);
	Log.i("music", "uri:"+uri);
				if(uri == null) {
					uri = GetMusicUrl.getSongUrlById(music.getMusicId());
					musicDownload(music);
				}
				else {
					music = findDownloadById(at, music.getMusicId());
				}
	Log.i("music", "uri:"+uri);
				if(uri == null) {
					return ;
				}
		
				musicNow = music;
Log.i("music", "index:"+musicHistory.indexOf(music));
				musicHistory.add(musicNow);
				musicPlay(at, uri);
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
		}

	//	private static void musicPlay() {
//	}
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
	
	public static void musicPrevious() {
		int index = musicHistory.indexOf(musicNow);
		if(index > 0) {
			
		}
	}

	public static void musicStop(Activity at) {
		init(at);
		musicControler.pause();
	}

	public static List<MusicEntity>getMusicHistory(){
		return musicHistory;
	}
	public static List<MusicEntity>searchMusicInNetease(String key){
		return GetMusicUrl.searchMusicByKey(key);
	}
//	public abstract void musicPlayById(String musicId);
	public static void musicContinue(Activity at) {
		init(at);
		musicControler.start();
	}
	public static List<MusicEntity>getMusicDownloaded(){
		return null;
	}
	public static Bitmap getMusicImageById(long musicId) {
		return null;
	}
	
	private static String findDownloaded(Activity at, MusicEntity music) {
		init(at);
		String uri = musicFolder+fileFolderHolder+File.separator+music.getMusicId();
		File file = new File(uri);
Log.i("music", "uri:"+uri);
		if(file.exists()) {
			return uri;
		}
		return null;
	}

	public static MusicEntity findDownloadById(Activity at, long musicId) {
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
}
