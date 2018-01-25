package com.neo.mytalker.activity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.neo.mytalker.R;
import com.neo.mytalker.adapter.ChatRulesAdapter;
import com.neo.mytalker.entity.ChatDialogEntity;
import com.neo.mytalker.myinterface.CustomDialog;
import com.neo.mytalker.myinterface.CustomDialog.Builder;
import com.neo.mytalker.util.ChatRulesManager;
import com.neo.mytalker.util.SQLiteManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class ChatRulesActivity extends Activity{
	ListView mListView;
	ChatDialogEntity mChatDialogEntity;
	ArrayList<ChatDialogEntity> mChatDialogEntityList;
	ChatRulesAdapter mChatRulesAdapter;
	ImageView mAddRulesBtn;
	CustomDialog.Builder mBuilder;
	CustomDialog mCustomDialog;
	View mView;
	Context mContext;
	EditText editTextOne, editTextTwo;
	String question_temp, answer_temp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat_rules);
		mBuilder = new Builder(this);
		init();
		initTestMessage();
		setAdapter();
		setOnClick();
	}
	
	public void initTestMessage() {
		List<Map<String, String>> rules = new ChatRulesManager(ChatRulesActivity.this, 0).getAllRules();
		for(Iterator<Map<String, String>>it = rules.iterator();
				it.hasNext(); ) {
			Map<String, String>rule = it.next();
			mChatDialogEntity = new ChatDialogEntity(rule.get(ChatRulesManager.askName),
					rule.get(ChatRulesManager.answerName),
					Integer.parseInt(rule.get(SQLiteManager.COL_NAME_ID)));
			mChatDialogEntityList.add(mChatDialogEntity);
		}
	}
	
	public void init() {
		mChatDialogEntityList = new ArrayList<ChatDialogEntity>();
		mListView = (ListView) this.findViewById(R.id.rules_list);
		mChatRulesAdapter = new ChatRulesAdapter(mChatDialogEntityList, this);
		mAddRulesBtn = (ImageView) this.findViewById(R.id.add_rules_btn);
		mView = LayoutInflater.from(ChatRulesActivity.this).inflate(R.layout.dialog_layout, null, false);
		editTextOne = (EditText) mView.findViewById(R.id.edit_one);
		editTextTwo = (EditText) mView.findViewById(R.id.edit_two);
	}
	
	public void setAdapter() {
		mListView.setAdapter(mChatRulesAdapter);
	}
	
	public void setOnClick() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
				// TODO Auto-generated method stub
				ChatDialogEntity item = mChatDialogEntityList.get(position);
				String question = item.getQuestion();
				String answer = item.getAnswer();
				final int positionTemp = position;
				showEditDialog("Please tell the rules~", question, answer, "收到！", "抛弃！", new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						question_temp = ((EditText)mCustomDialog.findViewById(R.id.edit_one)).getText().toString();
						answer_temp = ((EditText)mCustomDialog.findViewById(R.id.edit_two)).getText().toString();
						
						new ChatRulesManager(ChatRulesActivity.this, 0)
							.updateRule((int)id, question_temp, answer_temp);
						
						mChatDialogEntityList.get(positionTemp).setQuestion(question_temp);
						mChatDialogEntityList.get(positionTemp).setAnswer(answer_temp);
						mChatRulesAdapter.updateData(mChatDialogEntityList);
						mCustomDialog.dismiss();
					}
				}, new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mCustomDialog.dismiss();
					}
				});
			}
		});
		
		
		mAddRulesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int size = mChatRulesAdapter.getCount();
				showEditDialog("Please tell the rules~", "", "", "收到！", "抛弃！", new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						question_temp = ((EditText)mCustomDialog.findViewById(R.id.edit_one)).getText().toString();
						answer_temp = ((EditText)mCustomDialog.findViewById(R.id.edit_two)).getText().toString();
						
						new ChatRulesManager(ChatRulesActivity.this, 0).addRule(question_temp, answer_temp);

						mChatDialogEntity = new ChatDialogEntity(question_temp, answer_temp, size);
				//		mChatRulesAdapter.addItem(mChatDialogEntity);
						mChatDialogEntityList.add(mChatDialogEntity);
						Log.i("click", "SSS"+question_temp);
						mChatRulesAdapter.updateData(mChatDialogEntityList);
						mCustomDialog.dismiss();
					}
				}, new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mCustomDialog.dismiss();
					}
				});
			}
		});
	}
	public void showEditDialog(String alertText,String qText, String aText, String confirmBtn, String cancleBtn, View.OnClickListener conFirmListener, View.OnClickListener cancelListener) {
		mCustomDialog = mBuilder.setMessage(alertText)
				.seteditOne(qText)
				.seteditTwo(aText)
				.setPositiveButton(confirmBtn, conFirmListener)
				.setNegativeButton(cancleBtn, cancelListener)
				.createTwoButtonDialogWithEdit();
		mCustomDialog.show();
	}
}