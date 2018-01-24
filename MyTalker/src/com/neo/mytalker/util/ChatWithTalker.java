package com.neo.mytalker.util;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.neo.mytalker.fragments.ChatRecordFragment;

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
		ANSWER_FEATURE_ERROR = "命令出错了\n(╬▔皿▔)";
	public static final String FEATURE_MUSIC = "music#",
		FEATURE_MUSIC_PLAY = "play#",
		FEATURE_MUSIC_STOP = "stop",
		FEATURE_MUSIC_CONTINUE = "continue";
	private static MediaPlayer music = null;
	private static String chat(Context context, int userId, String ask) {
		//最后应该返回的回答
		String ans = null;


		//随机数 用于查询到多个时随机选取
		Random rd = new Random();

		//判断是否进入特殊功能 
		String ansFeature = checkFeature(ask);
		
		//进入普通聊天
		if(ansFeature == null || ansFeature.equals("")) {
			//或许当前对应的规则
			ChatRulesManager crm = new ChatRulesManager(context, userId);

			//查询规则 获取结果
			List<String>anss = new ChatRulesManager(context, userId)
					.getRuleByAsk(ask);
			
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
	
	private static String checkFeature(String ask) {
		String answer = null;
		if(ask.length() >= ChatWithTalker.FEATURE_MUSIC.length()
			&& ask.substring(0, ChatWithTalker.FEATURE_MUSIC.length())
				.equals(ChatWithTalker.FEATURE_MUSIC)
			) {
			answer = ChatWithTalker.featureMusic(
				ask.substring(ChatWithTalker.FEATURE_MUSIC.length())
			);
			if(answer == null || answer.equals("")) {
Log.i("music", ChatWithTalker.ANSWER_FEATURE_ERROR);
				answer = ChatWithTalker.ANSWER_FEATURE_ERROR;
			}
			return answer;
		}
		return null;
	}

	private static String featureMusicPlay(String musicName) {
		String musicUrl = GetMusicUrl.getSongUrl(musicName);
		if(musicUrl == null || musicUrl.equals("")) {
			return ChatWithTalker.ANSWER_MUSIC_PLAY_NOT_FOUND;
		}
		try {
			music.stop();
			music = new MediaPlayer();
			music.setDataSource(musicUrl);
			music.prepare();
			music.start();
Log.i("music", "start");
			return ChatWithTalker.ANSWER_MUSIC_PLAY;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ChatWithTalker.ANSWER_MUSIC_ERROR;
	}

	private static String featureMusicStop() {
		try {
			if(music.isPlaying()) {
Log.i("music", "stop when play");
				music.pause();
				return ChatWithTalker.ANSWER_MUSIC_STOP;
			}
			else {
				return ChatWithTalker.ANSWER_MUSIC_STOP_NO_MUSIC;
			}
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
		return ChatWithTalker.ANSWER_MUSIC_ERROR;
	}

	private static String featureMusicContinue() {
		try {
			if(!music.isPlaying()) {
				music.start();
				return ChatWithTalker.ANSWER_MUSIC_CONTINUE;
			}
			else {
				return ChatWithTalker.ANSWER_MUSIC_CONTINUE_IS_PLAYING;
			}
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
		return ChatWithTalker.ANSWER_MUSIC_ERROR;
	}
	
	private static String featureMusic(String musicControl) {
		if(musicControl.length() >= ChatWithTalker.FEATURE_MUSIC_PLAY.length()
			&& musicControl.substring(0, ChatWithTalker.FEATURE_MUSIC_PLAY.length())
				.equals(ChatWithTalker.FEATURE_MUSIC_PLAY)
			) {
			return featureMusicPlay(musicControl.substring(
					ChatWithTalker.FEATURE_MUSIC_PLAY.length()));
		}
		else if(musicControl.length() >= ChatWithTalker.FEATURE_MUSIC_STOP.length()
			&& musicControl.substring(0, ChatWithTalker.FEATURE_MUSIC_STOP.length())
				.equals(ChatWithTalker.FEATURE_MUSIC_STOP)
			) {
			return featureMusicStop();
		}
		else if(musicControl.length() >= ChatWithTalker.FEATURE_MUSIC_CONTINUE.length()
			&& musicControl.substring(0, ChatWithTalker.FEATURE_MUSIC_CONTINUE.length())
				.equals(ChatWithTalker.FEATURE_MUSIC_CONTINUE)
			) {
			return featureMusicContinue();
		}
		return null;
	}
	
	private Context context = null;
	private int userId = 0;
	private String ask = null;
	private ChatRecordFragment mChatRecFrag;
	public ChatWithTalker(ChatRecordFragment mChatRecFrag, Context context, int userId, String ask) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.userId = userId;
		this.ask = ask;
		this.mChatRecFrag = mChatRecFrag;
		init();
	}
	
	private void init() {
		if(music == null) {
			music = new MediaPlayer();
		}
	}
	
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
		mChatRecFrag.AddRecord(false, result);
		super.onPostExecute(result);
	}
}
