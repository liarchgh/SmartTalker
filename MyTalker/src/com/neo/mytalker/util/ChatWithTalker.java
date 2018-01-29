package com.neo.mytalker.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.neo.mytalker.entity.MusicEntity;
import com.neo.mytalker.entity.MusicEntity.artist;
import com.neo.mytalker.fragments.ChatRecordFragment;
import com.neo.mytalker.util.MusicManager.MUSIC_STATUS;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

public class ChatWithTalker extends AsyncTask<Void, Integer, String>{
	private static final String ANSWER_NET_ERROR = "网线被人拔了！\n∑(っ °Д °;)っ",
		ANSWER_MUSIC_PLAY = "音乐酱来了\nO(∩_∩)O~~",
		ANSWER_MUSIC_PLAY_NOT_FOUND = "找不到音乐酱了\n(╯﹏╰)",
		ANSWER_MUSIC_STOP = "音乐酱跑掉了\n(｡˘•ε•˘｡)",
		ANSWER_MUSIC_STOP_NO_MUSIC = "音乐酱早就走了\n(≡ω≡．)",
		ANSWER_MUSIC_CONTINUE = "音乐酱又回来了\no(*≧▽≦)ツ",
		ANSWER_MUSIC_CONTINUE_IS_PLAYING = "音乐酱早就在了\n(￣、￣)",
		ANSWER_MUSIC_ERROR = "音乐酱出错了\n(┙>∧<)┙へ┻┻",
		ANSWER_MUSIC_NO_HISTORY = "音乐酱还没来过\n(￣、￣)",
		ANSWER_MUSIC_PREVIOUS = "音乐酱退后了一首",
		ANSWER_MUSIC_PREVIOUS_NOT_FOUND = "音乐酱没有路可退了",
		ANSWER_MUSIC_NEXT = "音乐酱前进了一首",
		ANSWER_MUSIC_NEXT_NOT_FOUND = "音乐酱已经没路可走了",
		ANSWER_FEATURE_ERROR = "命令出错了\n(╬▔皿▔)",
		MUSIC_FOLDER = "music";
	public static final String
		FEATURE_MUSIC_PLAY = "播放音乐",
		FEATURE_MUSIC_STOP = "停止播放",
		FEATURE_MUSIC_HISTORY = "音乐历史",
		FEATURE_MUSIC_DOWNLOADED = "已下载音乐",
		FEATURE_MUSIC_PREVIOUS = "上一首",
		FEATURE_MUSIC_NEXT = "下一首",
		FEATURE_MUSIC_CONTINUE = "继续播放";
//	private static MediaPlayer music = null;
//	private static String musicFolderPath = null;
	private static Context context1 = null;
	private static String chat(Context context, int userId, String ask) {
		ChatWithTalker.context = context;
		
		//最后应该返回的回答
		String ans = null;

		//随机数 用于查询到多个时随机选取
		Random rd = new Random();

		//判断是否进入特殊功能 
		String ansFeature = checkFeature(context, ask);
		
		
		//进入普通聊天
		if(ansFeature == null || ansFeature.equals("")) {
			//或许当前对应的规则
			ChatRulesManager crm = new ChatRulesManager(context, userId);

			//查询规则 获取结果
			List<String>anss = crm.getRuleByAsk(ask);
			
			//判断是否可以使用规则
			if(anss != null && anss.size() > 0) {
				ans = anss.get(Math.abs(rd.nextInt())%anss.size());
			}

			//历史记录
			AskAndAnswer aaa = new AskAndAnswer(context, userId);
//			//删除历史记录中的网络异常
//			aaa.deleteAnswerByAnswer(ANSWER_NET_ERROR);

			//若仍无回答 则在线连接机器人
			if(ans == null || ans.equals("")) {
				ChatWithTROL ct = new ChatWithTROL();
				try {
					ans = ct.sendToRobot(ask);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				//若在线连接失败 则通过历史记录回答
				if(ans == null || ans.equals("")) {
					anss = aaa.getAnswerByAsk(ask);
					if(anss != null && anss.size() > 0) {
						ans = anss.get(Math.abs(rd.nextInt())%anss.size());
					}
					
					//历史记录获取失败 返回网络异常提示
					if(ans == null || ans.equals("")) {
						ans = ChatWithTalker.ANSWER_NET_ERROR;
					}
				}
			}
			
			//若网络无错 将本条对话加入历史记录
			if(ans != null && !ans.equals(ChatWithTalker.ANSWER_NET_ERROR)) {
				aaa.addAskAndAnswer(ask, ans);
			}
			return ans;
		}
		return ansFeature;
	}
	
//	private static String checkFeature(String ask) {
//		String answer = null;
//		if(ask.length() >= ChatWithTalker.FEATURE_MUSIC.length()
//			&& ask.substring(0, ChatWithTalker.FEATURE_MUSIC.length())
//				.equals(ChatWithTalker.FEATURE_MUSIC)
//			) {
//			answer = ChatWithTalker.featureMusic(
//				ask.substring(ChatWithTalker.FEATURE_MUSIC.length())
//			);
//			if(answer == null || answer.equals("")) {
//				answer = ChatWithTalker.ANSWER_FEATURE_ERROR;
//			}
//			return answer;
//		}
//		return null;
//	}
//
	private static String featureMusicPlay(Context ct, String musicName) {
		List<MusicEntity>ms = MusicManager.searchMusicInNetease(musicName);
		if(ms.size() > 0) {
			ms.get(0).play(ct);
			return ANSWER_MUSIC_PLAY;
		}
		else {
			return ANSWER_MUSIC_PLAY_NOT_FOUND;
		}
//		final String musicUrl = GetMusicUrl.getSongUrl(musicName, musicFolderPath);
//		if(musicUrl == null || musicUrl.equals("")) {
//			return ChatWithTalker.ANSWER_MUSIC_PLAY_NOT_FOUND;
//		}
//		try {
//			music.stop();
//			music = new MediaPlayer();
//			music.setDataSource(musicUrl);
//			music.prepare();
//			music.start();
//
//			return ChatWithTalker.ANSWER_MUSIC_PLAY;
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return ChatWithTalker.ANSWER_MUSIC_ERROR;
	}

	private static String featureMusicPrevious(Context ct) {
		MUSIC_STATUS res = MusicManager.musicPrevious(ct);;
		if(res == MusicManager.MUSIC_STATUS.PREVIOUS_SUCCESS) {
			return ANSWER_MUSIC_PREVIOUS;
		}
		else if(res == MUSIC_STATUS.PREVIOUS_NOT_FOUND) {
			return ANSWER_MUSIC_PREVIOUS_NOT_FOUND;
		}
		return ANSWER_MUSIC_ERROR;
	}

	private static String featureMusicNext(Context ct) {
		MUSIC_STATUS res = MusicManager.musicNext(ct);;
		if(res == MusicManager.MUSIC_STATUS.NEXT_SUCCESS) {
			return ANSWER_MUSIC_NEXT;
		}
		else if(res == MUSIC_STATUS.NEXT_NOT_FOUND) {
			return ANSWER_MUSIC_NEXT_NOT_FOUND;
		}
		return ANSWER_MUSIC_ERROR;
	}

	private static String featureMusicStop(Context ct) {
//		try {
//			if(music.isPlaying()) {
//				music.pause();
//				return ChatWithTalker.ANSWER_MUSIC_STOP;
//			}
//			else {
//				return ChatWithTalker.ANSWER_MUSIC_STOP_NO_MUSIC;
//			}
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return ChatWithTalker.ANSWER_MUSIC_ERROR;
		MUSIC_STATUS res = MusicManager.musicStop(ct);
		if(res == MusicManager.MUSIC_STATUS.STOP_SUCCESS) {
			return ANSWER_MUSIC_STOP;
		}
		else if(res == MUSIC_STATUS.STOP_NO_MUSIC) {
			return ANSWER_MUSIC_STOP_NO_MUSIC;
		}
		return ANSWER_MUSIC_ERROR;
	}

	private static String featureMusicContinue(Context at) {
		MusicManager.musicContinue(at);
		return ChatWithTalker.ANSWER_MUSIC_ERROR;
	}
	
	public static String featureMusicHistoryToString() {
//		List<String>musics = GetMusicUrl.getSongsDownloaded(musicFolderPath);
		List<MusicEntity>musics = MusicManager.getMusicHistory();
		StringBuffer res = new StringBuffer();
		boolean first = false;
		MusicEntity temp = null;
Log.i("music", "size:"+musics.size());
		for(Iterator<MusicEntity>it = musics.iterator();
				it.hasNext(); ) {
			if(first) {
				res.append("\n");
			}
			else {
				first = true;
			}
			temp = it.next();
			StringBuffer ars = new StringBuffer();
			for(Iterator<String>ar = temp.getArtistNames().iterator();
					true;) {
				ars.append(ar.next());
				if(ar.hasNext()) {
					ars.append(",");
				}
				else {
					break;
				}
			}
			res.append(temp.getMusicName()+"("+temp.getAlbumName()+")"
				+"("+ars.toString()+")");
		}
Log.i("music", "res:"+res.toString());
		String ret = res.toString();
		if(ret == null || ret.length() <= 0) {
			return ANSWER_MUSIC_NO_HISTORY;
		}
		return ret;
	}
	
	private static String checkFeature(Context ct, String feature) {
Log.i("music", "music control:"+feature);
		if(feature.length() >= ChatWithTalker.FEATURE_MUSIC_PLAY.length()
			&& feature.substring(0, ChatWithTalker.FEATURE_MUSIC_PLAY.length())
				.equals(ChatWithTalker.FEATURE_MUSIC_PLAY)
			&& feature.substring(
				ChatWithTalker.FEATURE_MUSIC_PLAY.length()).length() > 0
			) {
			
			return featureMusicPlay(ct, feature.substring(
					ChatWithTalker.FEATURE_MUSIC_PLAY.length()));
		}
		else if(feature.length() >= ChatWithTalker.FEATURE_MUSIC_STOP.length()
			&& feature.substring(0, ChatWithTalker.FEATURE_MUSIC_STOP.length())
				.equals(ChatWithTalker.FEATURE_MUSIC_STOP)
			&& feature.substring(
				ChatWithTalker.FEATURE_MUSIC_STOP.length()).length() <= 0
			) {
			return featureMusicStop(ct);
		}
		else if(feature.length() >= ChatWithTalker.FEATURE_MUSIC_CONTINUE.length()
			&& feature.substring(0, ChatWithTalker.FEATURE_MUSIC_CONTINUE.length())
				.equals(ChatWithTalker.FEATURE_MUSIC_CONTINUE)
			&& feature.substring(
				ChatWithTalker.FEATURE_MUSIC_CONTINUE.length()).length() <= 0
			) {
			return featureMusicContinue(ct);
		}
		else if(feature.length() >= ChatWithTalker.FEATURE_MUSIC_HISTORY.length()
			&& feature.substring(0, ChatWithTalker.FEATURE_MUSIC_HISTORY.length())
				.equals(ChatWithTalker.FEATURE_MUSIC_HISTORY)
			&& feature.substring(
				ChatWithTalker.FEATURE_MUSIC_HISTORY.length()).length() <= 0
			) {
			return featureMusicHistoryToString();
		}
		else if(feature.length() >= ChatWithTalker.FEATURE_MUSIC_PREVIOUS.length()
			&& feature.substring(0, ChatWithTalker.FEATURE_MUSIC_PREVIOUS.length())
				.equals(ChatWithTalker.FEATURE_MUSIC_PREVIOUS)
			&& feature.substring(
				ChatWithTalker.FEATURE_MUSIC_PREVIOUS.length()).length() <= 0
			) {
			return featureMusicPrevious(ct);
		}
		else if(feature.length() >= ChatWithTalker.FEATURE_MUSIC_NEXT.length()
			&& feature.substring(0, ChatWithTalker.FEATURE_MUSIC_NEXT.length())
				.equals(ChatWithTalker.FEATURE_MUSIC_NEXT)
			&& feature.substring(
				ChatWithTalker.FEATURE_MUSIC_NEXT.length()).length() <= 0
			) {
			return featureMusicNext(ct);
		}
		return null;
	}
	
	private static Context context = null;
	private static int userId = 0;
	private static String ask = null;
	private ChatRecordFragment mChatRecFrag;
	public ChatWithTalker(ChatRecordFragment mChatRecFrag, Context context, int userId, String ask) {
		// TODO Auto-generated constructor stub
		ChatWithTalker.context = context;
		ChatWithTalker.userId = userId;
		ChatWithTalker.ask = ask;
		this.mChatRecFrag = mChatRecFrag;
//		init();
	}
	
//	private static void init() {
//		if(music == null) {
//			music = new MediaPlayer();
//		}
//		if(musicFolderPath == null) {
//			musicFolderPath = context1.getFilesDir().getPath()+File.separator
//				+MUSIC_FOLDER;
//			File tPath = new File(musicFolderPath);
//			if(!tPath.exists()) {
//				tPath.mkdir();
//			}
//		}
//	}
	
	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return chat(context, userId, ask);
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
//		mChatRecFrag.stopLoading();
//Log.i("dynamic", "res:"+result);
		mChatRecFrag.AddRecord(false, result);
		if(result.equals(ChatWithTalker.ANSWER_MUSIC_PLAY)) {
			MusicEntity me = MusicManager.getMusicNow();
			mChatRecFrag.mChatActivity.initNotificationBar(me.getMusicName(), me.getAlbumImage());
		}
		super.onPostExecute(result);
	}
}
