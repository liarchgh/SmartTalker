package com.neo.mytalker.util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetMusicUrl {
	private final static String searchUrlPre = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=json&query={search}&page_no=1&page_size=30",
			songNameHolder = "{search}",
			fileUrlPre = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.song.play&songid={songid}",
			songIdHolder = "{songid}",
			songIdInJson = "\"song_id\"",
			fileLinkInJson = "\"file_link\"";

	private static String getSongId(String songName) throws IOException {
		String url = searchUrlPre.replace(songNameHolder, songName);
		url = url.replace(" ", "%20");
		String content = NetUtil.doGetString(url, null);
		Pattern rule = Pattern.compile(songIdInJson+":\"[^\"]*\"");
		Matcher mt = rule.matcher(content);
		if(mt.find()) {
			String res = mt.group();
			return res.substring(songIdInJson.length()+2, res.length()-1);
		}
		return null;
	}

	public static String getSongUrl(String songName) {
		String songId = null;
		try {
			songId = getSongId(songName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(songId != null) {
			String url = fileUrlPre.replace(songIdHolder, songId);
			String res = null;
			try {
				String content = NetUtil.doGetString(url, null);
				Pattern rule = Pattern.compile(fileLinkInJson+":\"[^\"]*");
				Matcher mt = rule.matcher(content);
				if(mt.find()) {
					res = mt.group();
					res = res.substring(fileLinkInJson.length() + 2, res.length());
					res = res.replace("\\/", "/");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return res;
		}
		return null;
	}

	private static List<String> getSongIds(String songName) throws IOException {
		List<String>res = new ArrayList<String>();
		String url = searchUrlPre.replace(songNameHolder, songName);
		url = url.replace(" ", "%20");
		String content = NetUtil.doGetString(url, null);
		Pattern rule = Pattern.compile(songIdInJson+":\"[^\"]*\"");
		Matcher mt = rule.matcher(content);
		while(mt.find()) {
			url = mt.group(0);
			res.add(url.substring(songIdInJson.length()+2, url.length()-1));
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
				String res = null;
				try {
					String content = NetUtil.doGetString(url, null);
					Pattern rule = Pattern.compile(fileLinkInJson+":\"[^\"]*");
					Matcher mt = rule.matcher(content);
					if(mt.find()) {
						res = mt.group();
						res = res.substring(fileLinkInJson.length() + 2, res.length());
						res = res.replace("\\/", "/");
						songUrls.add(res);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return songUrls;
	}
}
