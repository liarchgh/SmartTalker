package com.neo.mytalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.neo.mytalker.util.ChatRulesManager;
import com.neo.mytalker.util.SQLiteManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Test extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		Log.i("data base", Test.this.getFilesDir().getPath());
//		SQLiteManager su = new SQLiteManager(Test.this.getFilesDir().getPath(), "test", "test");
//		su.executeNotQuery("create table test (id integer primary key autoincrement, send text, receive text)");
//		su.executeNotQuery("insert into test(send, receive) values('qqq', 'www')");

//		//test insert
//		List<Map<String, String>>items = new ArrayList<Map<String, String>>();
//		Map<String, String>limit = new HashMap<String, String>();
//		limit.put("send", "1111");
//		limit.put("receive", "2222");
//		items.add(limit);
//		limit = new HashMap<String, String>();
//		limit.put("send", "pppp");
//		limit.put("receive", "oooo");
//		items.add(limit);
//		su.insertIterms(items);
		
//		//test update
//		su.executeNotQuery("update "+su.getTable()+" set send='000' where id = "+3);
//		Map<String,String>change = new HashMap<String, String>();
//		change.put("receive", "uiop");
//		su.updateItem(3+"", change);
		
//		//test delete
//		su.executeNotQuery("delete from test where id = 3");
//		su.deleteItemById();

		ChatRulesManager crm = new ChatRulesManager(Test.this, 0);
//		crm.addRule("2b", "9s");
//		crm.updateRule(2, "2b", "A2");
		
//		//查询规则
//		List<String>as = crm.getAnswerByAsk("2b");
//		for(int i = 0; i < as.size(); ++i) {
//			Log.i("data base", "i:"+as.get(i));
//		}
		
		crm.deleteRuleById(3);
		crm.showAll();
	}
}
