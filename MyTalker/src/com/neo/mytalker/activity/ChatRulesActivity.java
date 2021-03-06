package com.neo.mytalker.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.neo.mytalker.R;
import com.neo.mytalker.adapter.ChatRulesAdapter;
import com.neo.mytalker.entity.ChatDialogEntity;
import com.neo.mytalker.entity.GlobalSettings;
import com.neo.mytalker.myinterface.CharacterParser;
import com.neo.mytalker.myinterface.CustomDialog;
import com.neo.mytalker.myinterface.CustomDialog.Builder;
import com.neo.mytalker.myinterface.PinyinComparator;
import com.neo.mytalker.myinterface.SideBar;
import com.neo.mytalker.myinterface.SideBar.OnTouchingLetterChangedListener;
import com.neo.mytalker.myinterface.ThemeInterface;
import com.neo.mytalker.util.ChatRulesManager;
import com.neo.mytalker.util.SQLiteManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ChatRulesActivity extends Activity implements ThemeInterface {
	ListView mListView;
	ChatDialogEntity mChatDialogEntity;
	List<ChatDialogEntity> mChatDialogEntityList;
	ChatRulesAdapter mChatRulesAdapter;
	TextView mAddRulesBtn;
	CustomDialog.Builder mBuilder;
	CustomDialog mCustomDialog, mAlertCunstonDialog, mLongCustomDialog;
	View mView;
	Context mContext;
	EditText editTextOne, editTextTwo;
	String question_temp, answer_temp;
	TextView mLetterDialog;
	SideBar mSideBar;
	CharacterParser mCharacterParser;
	PinyinComparator mPinyinComparator;
	List<ChatDialogEntity> mFilterData;
	TextView mQuestionTitle, mMessageTextView;
	Button mPositiveButton, mNegativeButton;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat_rules);
		/*
		 * mContext = ChatRulesActivity.this; mSideBar.setContext(mContext);
		 */
		mContext = ChatRulesActivity.this;
		mBuilder = new Builder(this);
		init();
		initTestMessage();
		setListener();
		setAdapter();
		setOnClick();
		ChangeThemeColor();
	}

	@SuppressLint("DefaultLocale")
	public void initTestMessage() {
		List<Map<String, String>> rules = new ChatRulesManager(ChatRulesActivity.this, 0).getAllRules();
		for (Iterator<Map<String, String>> it = rules.iterator(); it.hasNext();) {
			Map<String, String> rule = it.next();
			char c[] = rule.get(ChatRulesManager.askName).toCharArray();
			mChatDialogEntity = new ChatDialogEntity(rule.get(ChatRulesManager.askName),
					rule.get(ChatRulesManager.answerName), String.valueOf(c[0]),
					Integer.parseInt(rule.get(SQLiteManager.COL_NAME_ID)));
			String pinyin = mCharacterParser.getSelling(rule.get(ChatRulesManager.askName));
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if (sortString.matches("[A-Z]")) {
				mChatDialogEntity.setSortLetters(sortString.toUpperCase());
			} else {
				mChatDialogEntity.setSortLetters("#");
			}
			mChatDialogEntityList.add(mChatDialogEntity);
		}
	}

	public void init() {
		mCharacterParser = CharacterParser.getInstance();
		mPinyinComparator = new PinyinComparator();
		mChatDialogEntityList = new ArrayList<ChatDialogEntity>();
		mListView = (ListView) this.findViewById(R.id.rules_list);
		mChatRulesAdapter = new ChatRulesAdapter(mChatDialogEntityList, this);
		mAddRulesBtn = (TextView) this.findViewById(R.id.add_rules_btn);
		mView = LayoutInflater.from(ChatRulesActivity.this).inflate(R.layout.dialog_layout, null, false);
		editTextOne = (EditText) mView.findViewById(R.id.edit_one);
		editTextTwo = (EditText) mView.findViewById(R.id.edit_two);
		mLetterDialog = (TextView) this.findViewById(R.id.letter_dialog);
		mSideBar = (SideBar) this.findViewById(R.id.sidebar);
		mSideBar.setTextView(mLetterDialog);
		mQuestionTitle = (TextView) this.findViewById(R.id.question_title);
		mPositiveButton = (Button) mView.findViewById(R.id.positive_button);
		mNegativeButton = (Button) mView.findViewById(R.id.negative_button);
		mMessageTextView = (TextView) mView.findViewById(R.id.message);
	}

	public void setListener() {
		mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// TODO Auto-generated method stub
				int position = mChatRulesAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mListView.setSelection(position);
				}
			}
		});
	}

	public void setAdapter() {
		Collections.sort(mChatDialogEntityList, mPinyinComparator);
		mListView.setAdapter(mChatRulesAdapter);
	}

	public void setOnClick() {
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				showTwoBtnDialog("主人你确定要我忘记这句话嘛？", "快点忘掉", "再考虑一下", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new ChatRulesManager(ChatRulesActivity.this, 0)
								.deleteRuleById(mChatDialogEntityList.get(position).getId());
						Object o = mChatDialogEntityList.remove(position);
						Collections.sort(mChatDialogEntityList, mPinyinComparator);
						mChatRulesAdapter.updateData(mChatDialogEntityList);
						mCustomDialog.dismiss();
						if (o != null) {
							showSingleDialog("主人我已经忘记啦  =￣ω￣= ", "真乖", new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									mAlertCunstonDialog.dismiss();
								}
							});
						} else {
							showSingleDialog("主人我好像不记得这个问题诶Σ(っ °Д °;)っ", "我再找找", new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									mAlertCunstonDialog.dismiss();
								}
							});
						}
					}
				}, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mCustomDialog.dismiss();
					}
				});
				return true;
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
				// TODO Auto-generated method stub
				ChatDialogEntity item = mChatDialogEntityList.get(position);
				String question = item.getQuestion();
				String answer = item.getAnswer();
				final int positionTemp = position;
				showEditDialog("请主人调教  (*/ω＼*) ~", question, answer, "收到！", "抛弃！", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						question_temp = ((EditText) mCustomDialog.findViewById(R.id.edit_one)).getText().toString();
						answer_temp = ((EditText) mCustomDialog.findViewById(R.id.edit_two)).getText().toString();
						if (!question_temp.equals(null) && !question_temp.equals("") && !answer_temp.equals(null)
								&& !answer_temp.equals("")) {
							new ChatRulesManager(ChatRulesActivity.this, 0).updateRule((int) id, question_temp,
									answer_temp);
							mChatDialogEntityList.get(positionTemp).setQuestion(question_temp);
							mChatDialogEntityList.get(positionTemp).setAnswer(answer_temp);
							String pinyin = mCharacterParser.getSelling(question_temp);
							String sortString = pinyin.substring(0, 1).toUpperCase();
							if (sortString.matches("[A-Z]")) {
								mChatDialogEntity.setSortLetters(sortString.toUpperCase());
							} else {
								mChatDialogEntity.setSortLetters("#");
							}
							Collections.sort(mChatDialogEntityList, mPinyinComparator);
							mChatRulesAdapter.updateData(mChatDialogEntityList);
							mCustomDialog.dismiss();
						} else if ((question_temp == null || question_temp.equals("")) && !(answer_temp == null)
								&& !answer_temp.equals("")) {
							showSingleDialog("主人怎么不说问题(°ー°〃)", "这就说", new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									mAlertCunstonDialog.dismiss();
								}
							});
						} else if (((answer_temp == null) || answer_temp.equals("")) && !(question_temp == null)
								&& !question_temp.equals("")) {
							showSingleDialog("主人需要告诉我怎么回答哟≡ω≡", "好哒", new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									mAlertCunstonDialog.dismiss();
								}
							});
						} else {
							showSingleDialog("主人不说点什么吗？( =•ω•= )m", "这就说", new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									mAlertCunstonDialog.dismiss();
								}
							});
						}
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
				showEditDialog("请主人调教  (*/ω＼*) ~", "", "", "收到！", "抛弃！", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						question_temp = ((EditText) mCustomDialog.findViewById(R.id.edit_one)).getText().toString();
						answer_temp = ((EditText) mCustomDialog.findViewById(R.id.edit_two)).getText().toString();
						if (!question_temp.equals(null) && !question_temp.equals("") && !answer_temp.equals(null)
								&& !answer_temp.equals("")) {
							new ChatRulesManager(ChatRulesActivity.this, 0).addRule(question_temp, answer_temp);

							mChatDialogEntity = new ChatDialogEntity(question_temp, answer_temp,
									String.valueOf((question_temp.toCharArray())[0]), size);
							// mChatRulesAdapter.addItem(mChatDialogEntity);
							mChatDialogEntityList.add(mChatDialogEntity);
							// Log.i("click", "SSS"+question_temp);
							String pinyin = mCharacterParser.getSelling(question_temp);
							String sortString = pinyin.substring(0, 1).toUpperCase();
							if (sortString.matches("[A-Z]")) {
								mChatDialogEntity.setSortLetters(sortString.toUpperCase());
							} else {
								mChatDialogEntity.setSortLetters("#");
							}
							Collections.sort(mChatDialogEntityList, mPinyinComparator);
							mChatRulesAdapter.updateData(mChatDialogEntityList);
							mCustomDialog.dismiss();
						} else if ((question_temp == null || question_temp.equals("")) && !(answer_temp == null)
								&& !answer_temp.equals("")) {
							showSingleDialog("主人怎么不说问题(°ー°〃)", "这就说", new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									mAlertCunstonDialog.dismiss();
								}
							});
						} else if (((answer_temp == null) || answer_temp.equals("")) && !(question_temp == null)
								&& !question_temp.equals("")) {
							showSingleDialog("主人需要告诉我怎么回答哟≡ω≡", "好哒", new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									mAlertCunstonDialog.dismiss();
								}
							});
						}else {
							showSingleDialog("主人不说点什么吗？( =•ω•= )m", "这就说", new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									mAlertCunstonDialog.dismiss();
								}
							});
						}

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

	/********************/

	public void showSingleDialog(String alertText, String singleBtn, View.OnClickListener singleBtnListener) {
		mAlertCunstonDialog = mBuilder.setMessage(alertText).setSingleButton(singleBtn, singleBtnListener)
				.createSingleButtonDialog();
		mAlertCunstonDialog.show();

	}

	public void showTwoBtnDialog(String alertText, String confirmBtn, String cancleBtn,
			View.OnClickListener conFirmListener, View.OnClickListener cancleListener) {
		mCustomDialog = mBuilder.setMessage(alertText).setPositiveButton(confirmBtn, conFirmListener)
				.setNegativeButton(cancleBtn, cancleListener).createTwoButtonDialog();
		mCustomDialog.show();
	}

	/******************/
	public void showEditDialog(String alertText, String qText, String aText, String confirmBtn, String cancleBtn,
			View.OnClickListener conFirmListener, View.OnClickListener cancelListener) {
		mCustomDialog = mBuilder.setMessage(alertText).seteditOne(qText).seteditTwo(aText)
				.setPositiveButton(confirmBtn, conFirmListener).setNegativeButton(cancleBtn, cancelListener)
				.createTwoButtonDialogWithEdit();
		mCustomDialog.show();
	}

	@Override
	public void ChangeThemeColor() {
		// TODO Auto-generated method stub
		mQuestionTitle.setBackgroundColor(GlobalSettings.THEME_COLOR);
		mAddRulesBtn.setBackgroundColor(GlobalSettings.THEME_COLOR);

	}
}
