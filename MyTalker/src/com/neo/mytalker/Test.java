package com.neo.mytalker;

import com.neo.mytalker.util.ChatWithTROL;
import com.neo.mytalker.util.ChatWithTalker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

//		ChatRulesManager crm = new ChatRulesManager(Test.this, 0);
//		crm.addRule("2b", "9s");
//		crm.updateRule(2, "2b", "A2");
		
//		//查询规则
//		List<String>as = crm.getAnswerByAsk("2b");
//		for(int i = 0; i < as.size(); ++i) {
//			Log.i("data base", "i:"+as.get(i));
//		}
		
//		crm.deleteRuleById(3);
//		crm.showAll();
	}
	
	public void chat(View v) {
Log.i("chat", "click");
		final String ask = ((EditText)Test.this.findViewById(R.id.et)).getText().toString();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
Log.i("chat", "net");
				final String ans = ChatWithTalker.chat(Test.this, 0, ask);
				final TextView tv = ((TextView)Test.this.findViewById(R.id.tv));
				tv.post(new Runnable() {
					
					@Override
					public void run() {
Log.i("chat", "set");
						// TODO Auto-generated method stub
						tv.setText(ans);
					}
				});
			}
		}).start();
		
//		try {
//			Log.i("use util", ChatWithTROL.sendToRobot("你真棒"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
