package com.neo.mytalker.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class AskAndAnswer {
	//数据库操作类
	private SQLiteManager sm = null;
	//要打开的数据库名称
	protected String dataBaseName
		= "";
	protected String getDataBaseName() {
		return dataBaseName;
	}

	protected void setDataBaseName(String dataBaseName) {
		AskAndAnswer.this.dataBaseName = dataBaseName;
	}

	//用户ID，区分不同用户，同时作为表名组成部分
	private String userId;
	//表中列名
	public static final String askName = "ask",
		answerName = "answer";
	
	public AskAndAnswer() {}

	public AskAndAnswer(Context context, int userId) {
		init(context, userId, SQLiteManager.DB_NAME_HISTORY);
	}

	public void init(Context context, int userId, String dataBaseName) {
		// TODO Auto-generated constructor stub
		this.dataBaseName = dataBaseName;
		AskAndAnswer.this.userId = "user"+userId;
		AskAndAnswer.this.sm =
			new SQLiteManager(context.getFilesDir().getPath(),
			dataBaseName, AskAndAnswer.this.userId
		);
		if(!AskAndAnswer.this.sm.hasTable()) {
			Map<String, String>cols = new HashMap<String, String>();
			cols.put(askName, SQLiteManager.COL_TYPE_TEXT);
			cols.put(answerName, SQLiteManager.COL_TYPE_INTEGER);
			this.sm.createTable(cols);
		}
	}

	//根据Id查询之前若干个历史记录
	public List<Map<String, String>> getHistory(int id, int number) {
		return AskAndAnswer.this.sm.queryConsequent(id, number);
	}
	
	//传入用户说的话，查询之前的历史记录
	public List<String> getAnswerByAsk(String ask) {
		Map<String, String>limit = new HashMap<String, String>();
		limit.put(askName, ask);
		List<Map<String,String>>items = AskAndAnswer.this.sm.queryByLimit(limit);
		List<String>answers = new ArrayList<String>();
		for(Iterator<Map<String, String>>it = items.iterator();
				it.hasNext(); ) {
			answers.add(it.next().get(answerName));
		}
		return answers;
	}
	
	//将制定ID的对话更改（规则或历史）
	protected final void updateAskAndAnswer(int id, String ask, String answer) {
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

//	public final void deleteAnswerByAnswer(String answer) {
//		Map<String, String>limit = new HashMap<String, String>();
//		limit.put(answerName, answer);
//		AskAndAnswer.this.sm.deleteItem(limit);
//	}
}
