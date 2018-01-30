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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

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
	
	public static final String HINT_DOWNLOAD_MUSIC_ERROR = "音乐加载失败",
			musicFolderHolder = File.separator+"music",
			fileFolderHolder = File.separator+"file",
			donwloadList = File.separator+"musicList",
			imageFolderHolder = File.separator+"image";
	public static String musicFolder = null;
	private static ChatActivity context = null;
	private static MediaPlayer musicControler = null;
	private static MusicEntity musicNow = null;
	private static List<MusicEntity>musicHistory = new ArrayList<MusicEntity>(),
			musicDownloaded = null;
	public static void init(ChatActivity ct) {
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
	
	private static void musicPlay(ChatActivity at, String uri) {
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
	public static MusicEntity musicPlay(ChatActivity at, MusicEntity music) {
		init(at);
Log.i("music", "name:"+music.getAlbumName());
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
			
//			saveAlbumImage(music);
	
			//更新当前歌曲
			musicNow = music;
			MusicEntity temp = addHistory(musicNow);
			if(temp != null) {
				musicNow = temp;
			}
Log.i("music", "url:"+uri);
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
					if(NetUtil.doGetMusic(GetMusicUrl.getSongUrlById(music.getMusicId()),
						file.getPath()+File.separator+music.getMusicId()) != 200) {
						Toast.makeText(context, HINT_DOWNLOAD_MUSIC_ERROR, Toast.LENGTH_LONG).show();
					}
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
	
	public static MUSIC_STATUS musicContinue(ChatActivity activity) {
		init(activity);
		if(musicControler != null && !musicControler.isPlaying()) {
			musicControler.start();
			loadMusicOnlyButton(context);
			return MUSIC_STATUS.CONTINUE_SUCCESS;
		}
		return MUSIC_STATUS.CONTINUE_IS_PLAYING;
	}
	
	//	public abstract void musicPlayById(String musicId);
//	public static void musicContinue(ChatActivity at) {
//		init(at);
//		musicControler.start();
//	}

	public static MUSIC_STATUS musicStop(ChatActivity at) {
		init(at);
		if(musicControler != null && musicControler.isPlaying()) {
			musicControler.pause();
			loadMusicOnlyButton(at);
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
public static List<MusicEntity>getMusicDownloaded(){
		return null;
	}
	public static Bitmap getMusicImageById(long musicId) {
		return null;
	}
	
	private static String findDownloaded(ChatActivity at, MusicEntity music) {
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

	public static MusicEntity findDownloadById(ChatActivity at, long musicId) {
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
			oos.close();
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
	public static Bitmap getAlbumImage(ChatActivity context, MusicEntity music) {
//Log.i("music", "image:"+music.getAlbumImageUri());
		String uri = musicFolder+imageFolderHolder;
		File file = new File(uri);
		if(!file.exists()) {
			file.mkdir();
		}
		uri += File.separator+music.getAlbumId();
//Log.i("music", "image uri:"+uri);
		if(file.exists()) {
			return BitmapFactory.decodeFile(uri);
		}
//		saveAlbumImage(music);
		return NetUtil.doGetBitmap(music.getAlbumImageUri(), null);
	}
	public static boolean saveAlbumImage(MusicEntity music) {
        try {
			String uri = musicFolder+imageFolderHolder+File.separator+music.getAlbumId();
			File file = new File(uri);
			if(file.exists()) {
				return false;
			}
//			file.createNewFile();
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
	
	public static MUSIC_STATUS musicPrevious(ChatActivity context) {
		MusicEntity me = MusicManager.getPreviousMusic(context);
		if(me != null) {
			me.play(context);
			return MUSIC_STATUS.PREVIOUS_SUCCESS;
		}
		return MUSIC_STATUS.PREVIOUS_NOT_FOUND;
	}
	public static MusicEntity getPreviousMusic(ChatActivity context) {
		init(context);
		int index = musicHistory.indexOf(musicNow);
		if(index > 0) {
			return musicHistory.get(index - 1);
		}
		return null;
	}

	public static MUSIC_STATUS musicNext(ChatActivity context) {
		MusicEntity me = MusicManager.getNextMusic(context);
		if(me != null) {
			me.play(context);
			return MUSIC_STATUS.NEXT_SUCCESS;
		}
		return MUSIC_STATUS.NEXT_NOT_FOUND;
	}

	public static MusicEntity getNextMusic(ChatActivity context) {
		init(context);
		int index = musicHistory.indexOf(musicNow);
		if(index < musicHistory.size() - 1) {
//			musicNow = musicHistory.get(index + 1);
//			musicNow.play(activity);
			return musicHistory.get(index + 1);
		}
		return null;
	}
	
	public static void deleteAllMusicDownloaded() {
		File file = new File(musicFolder);
		if(file.exists()) {
			file.delete();
		}
	}

	public static void loadMusicOnlyButton(ChatActivity ca) {
		ca.initNotificationBar();
	}

	public static void loadMusicInfoToNotification(final MusicEntity me, final ChatActivity ca) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final Bitmap bm = me.loadAlbumBitmap(ca);
				ca.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						ca.initNotificationBar(
							me.getMusicName(), bm);
					}
				});
			}
		}).start();
	}
}
