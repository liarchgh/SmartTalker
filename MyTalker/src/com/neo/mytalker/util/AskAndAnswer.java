package com.neo.mytalker.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;

public class AskAndAnswer {
	private SQLiteManager sm = null;
	private static final String dataBaseName = "DBRules";
	private String userId;
	private static final String askName = "ask",
		answerName = "answer";
	public AskAndAnswer(Context context, int userId) {
		// TODO Auto-generated constructor stub
		AskAndAnswer.this.userId = "user"+userId;
		AskAndAnswer.this.sm =
			new SQLiteManager(context.getFilesDir().getPath(),
			dataBaseName, AskAndAnswer.this.userId
		);
		if(!AskAndAnswer.this.sm.hasTable()) {
			Map<String, String>cols = new HashMap<String, String>();
			cols.put(askName, SQLiteManager.ColTypeText);
			cols.put(answerName, SQLiteManager.ColTypeText);
			this.sm.createTable(cols);
		}
	}
	
	public List<String> getAnswerByAsk(String ask) {
		Map<String, String>limit = new HashMap<String, String>();
		limit.put(askName, ask);
		List<Map<String,String>>items = AskAndAnswer.this.sm.executeQuery(limit);
		List<String>answers = new ArrayList<String>();
		for(Iterator<Map<String, String>>it = items.iterator();
				it.hasNext(); ) {
			answers.add(it.next().get(answerName));
		}
		return answers;
	}
	
	public final void updateAskAndAnswer(int id, String ask, String answer) {
		Map<String, String>change = new HashMap<String, String>();
		if(ask != null && !ask.equals("")) {
			change.put(askName, ask);
		}
		if(answer != null && !answer.equals("")) {
			change.put(answerName, answer);
		}
		sm.updateItem(id, change);
	}

	public final void addAskAndAnswer(String ask, String answer) {
		Map<String, String>rule = new HashMap<String, String>();
		rule.put(askName, ask);
		rule.put(answerName, answer);
		sm.insertItem(rule);
	}
	
	public final void deleteAnswerById(int id) {
		AskAndAnswer.this.sm.deleteItemById(id);
	}

	public void showAll() {
		AskAndAnswer.this.sm.showAll();
	}
}
