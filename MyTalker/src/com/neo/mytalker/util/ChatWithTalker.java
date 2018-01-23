package com.neo.mytalker.util;

import java.util.List;
import java.util.Random;

import android.content.Context;

public class ChatWithTalker {
	public static final String NET_ERROR = "你的网线被人拔了！";
	public static String chat(Context context, int userId, String ask) {
		String ans = null;
		ChatRulesManager crm = new ChatRulesManager(context, userId);
		AskAndAnswer aaa = new AskAndAnswer(context, userId);
		aaa.deleteAnswerByAnswer(NET_ERROR);
		List<String>anss = crm.getAnswerByAsk(ask);
		Random rd = new Random();
		if(anss != null && anss.size() > 0) {
			ans = anss.get(Math.abs(rd.nextInt())%anss.size());
		}
		if(ans == null || ans.equals("")) {
			ChatWithTROL ct = new ChatWithTROL();
			try {
				ans = ct.sendToRobot(ask);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(ans == null || ans.equals("")) {
				anss = aaa.getAnswerByAsk(ask);
				if(anss != null && anss.size() > 0) {
					ans = anss.get(Math.abs(rd.nextInt())%anss.size());
				}
				if(ans == null || ans.equals("")) {
					ans = ChatWithTalker.NET_ERROR;
				}
			}
		}
		if(ans != null && !ans.equals(ChatWithTalker.NET_ERROR)) {
			aaa.addAskAndAnswer(ask, ans);
		}
		return ans;
	}
}
