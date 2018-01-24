package com.neo.mytalker.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;

public class ChatRulesManager extends AskAndAnswer{
	public ChatRulesManager(Context context, int userId) {
		super(context, userId);
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

	public void showAll() {
		super.showAll();
	}
}
