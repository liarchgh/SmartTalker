package com.neo.mytalker.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neo.mytalker.entity.MusicEntity;
import com.neo.mytalker.entity.MusicEntity.RequestBody;
import com.neo.mytalker.entity.MusicEntity.artist;
import com.neo.mytalker.entity.MusicEntity.song;

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

	public static String getSongUrlById(long id) {
		return fileUrlPre.replace(songIdHolder, id+"");
	}

	public static String getSongUrl(String songName, final String musicFolderPath) {
//		return "http://music.163.com/song/media/outer/url?id=640565.mp3";

//		final String musicFilePath = musicFolderPath+
//			File.separator+songName+".mp3";
//		if(new File(musicFilePath).exists()) {
//			return musicFilePath;
//		}
//		
//		File file = new File(musicFolderPath);
//		if(!file.exists()) {
//			if(!file.mkdir()) {
//				return null;
//			}
//		}
			
		String songId = "";
		try {
			songId = getSongId(songName);

//			Log.i("music", "id:"+songId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String url = fileUrlPre.replace(songIdHolder, songId);
//		if(songId != null) {
//			
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					try {
//						NetUtil.doGetMusic(url, musicFilePath);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}).start();
//		}
		return url;
	}
	
	private static String getSongId(String songName) throws IOException {
		String url = searchUrlPre.replace(songNameHolder, URLEncoder.encode(songName, "utf-8"));
//		url = URLEncoder.encode(url, "utf-8");
		url = url.replace(" ", "%20");
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
		String url = searchUrlPre.replace(songNameHolder, URLEncoder.encode(songName, "utf-8"));
		url = url.replace(" ", "%20");
Log.i("music", "url:"+url);
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
	
	public static List<MusicEntity>searchMusicByKey(String key){
		String content;
		List<MusicEntity>musics = new ArrayList<MusicEntity>();
		try {
			//拼接查询的url
			String url = searchUrlPre.replace(songNameHolder, URLEncoder.encode(key, "utf-8"));
//System.out.println(url);
			url = url.replace(" ", "%20");
			
			//使用get方法获得json
			content = NetUtil.doGetString(url, null);
//System.out.println(content);
//			for(int i = 0; i < content.length(); ++i) {
//				if(i == 15448) {
//					System.out.print("\n");
//				}
//				if(i > 15448-100 && i < 15548) {
//					System.out.print(content.charAt(i));
//				}
//			}
			
			//解析json
			Gson resJson = new Gson();
			Type tokens = new TypeToken<RequestBody>(){}.getType();
			//request即为解析结果
			RequestBody request = resJson.fromJson(content, tokens);
//			resJson.fromJson(content, tokens);
//			System.out.println(request.result.songs.get(0).ar.get(0).name);
			
			List<song>songs = request.result.songs;
			List<String>temps = null;
			for(Iterator<song>it = songs.iterator();
					it.hasNext(); ) {
//				System.out.println("SS");
				song so = it.next();
				temps = new ArrayList<String>();
				for(Iterator<artist>aar = so.ar.iterator();
						aar.hasNext(); ) {
					temps.add(aar.next().name);
				}
				musics.add(new MusicEntity(so.name, so.id, temps,
					so.al.name, so.al.picUrl, so.al.id));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return musics;
	}
}
