package com.neo.mytalker.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;

public class ChatRulesManager extends AskAndAnswer{
	protected String dataBaseName = "";

	public ChatRulesManager(Context context, int userId) {
		super();
		init(context, userId, SQLiteManager.DB_NAME_RULE);
	}
	
	public void updateRule(int id, String ask, String answer) {
		super.updateAskAndAnswer(id, ask, answer);
	}

	public void addRule(String ask, String answer) {
		super.addAskAndAnswer(ask, answer);
	}
	
	public void deleteRuleById(int id) {
		super.deleteAnswerById(id);
	}
	
	public List<String> getAnswerByAskInRules(String ask){
		return getAnswerByAsk(ask);
	}

//	//传入用户说的话，查询之前规则
//	public List<String> getRuleByAsk(String ask) {
//		return getAnswerByAsk(ask);
//	}
	
	public List<Map<String, String>>getAllRules(){
		return getHistory(-1, (int)1e9);
	}
	
//	public void showAll() {
//		super.showAll();
//	}
}
