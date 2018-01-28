package com.neo.mytalker.util;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.provider.ContactsContract.Directory;
import android.util.Log;

public class GetMusicUrl {
	private final static String
//		searchUrlPre = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=json&query={search}&page_no=1&page_size=30",
//		fileUrlPre = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.song.play&songid={songid}",
//		songIdInJson = "\"song_id\"",
//		fileLinkInJson = "\"file_link\"";
		songNameHolder = "{search}",
		searchUrlPre = "https://api.imjad.cn/cloudmusic/?type=search&search_type=1&s="+songNameHolder,
		songIdHolder = "{songid}",
		fileUrlPre = "http://music.163.com/song/media/outer/url?id="+songIdHolder+".mp3",
		songIdInJson = "\"id\":[^\"]*";


	public static String getSongUrl(String songName, final String musicFolderPath) {
//		return "http://music.163.com/song/media/outer/url?id=640565.mp3";

		final String musicFilePath = musicFolderPath+
			File.separator+songName+".mp3";
Log.i("music", "file uri:"+musicFilePath);
		if(new File(musicFilePath).exists()) {
			return musicFilePath;
		}
		
		File file = new File(musicFolderPath);
		if(!file.exists()) {
			if(!file.mkdir()) {
				return null;
			}
		}
			
		String songId = "";
		try {
			songId = getSongId(songName);

//			Log.i("music", "id:"+songId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
Log.i("music", "id:"+songId);
		final String url = fileUrlPre.replace(songIdHolder, songId);
		if(songId != null) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						NetUtil.doGetMusic(url, musicFilePath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
		return url;
	}

	private static String getSongId(String songName) throws IOException {
		String url = searchUrlPre.replace(songNameHolder, URLEncoder.encode(songName, "utf-8"));
//		url = URLEncoder.encode(url, "utf-8");
		url = url.replace(" ", "%20");
Log.i("music", "id url:"+url);
		String content = NetUtil.doGetString(url, null);
		Pattern rule = Pattern.compile(songIdInJson);
		Matcher mt = rule.matcher(content);
		if(mt.find()) {
			String res = mt.group();
			return res.substring(5, res.length()-1);
		}
		return null;
	}

	private static List<String> getSongIds(String songName) throws IOException {
		List<String>res = new ArrayList<String>();
		String url = searchUrlPre.replace(songNameHolder, songName);
		url = url.replace(" ", "%20");
		String content = NetUtil.doGetString(url, null);
		Pattern rule = Pattern.compile(songIdInJson);
		Matcher mt = rule.matcher(content);
		while(mt.find()) {
			url = mt.group(0);
			res.add(url.substring(5, url.length()-1));
		}
		return res;
	}

	public static List<String> getSongUrls(String songName) {
		List<String> songIds = null,
			songUrls = new ArrayList<String>();
		try {
			songIds = getSongIds(songName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(songIds != null) {
			for(String songId : songIds) {
				String url = fileUrlPre.replace(songIdHolder, songId);
				songUrls.add(url);
			}
		}
		return songUrls;
	}
	
	public static List<String>getSongsDownloaded(final String musicFolderPath){
		List<String>musics = new ArrayList<String>();
		File folder = new File(musicFolderPath);
		if(folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			String name = null;
			for(int i = 0; i < files.length; ++i) {
				name = files[i].getName();
				musics.add(name.substring(0, name.length() - 4));
			}
		}
		return musics;
	}
}
