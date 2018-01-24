package com.neo.mytalker.myinterface;
import java.util.ArrayList;
import java.util.Date;

import com.neo.mytalker.R;
import com.neo.mytalker.adapter.ChatRecordAdapter;
import com.neo.mytalker.adapter.ChatRulesAdapter;
import com.neo.mytalker.entity.ChatDialogEntity;
import com.neo.mytalker.entity.ChatRecordData;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public static class Builder{
		private String message;
		private View contentView;
		private String positiveButtonText;
		private String negativeButtonText;
		private String singleButtonText;
		private View.OnClickListener positiveButtonClickListener;
		private View.OnClickListener negativeButtonClickListener;
		private View.OnClickListener singleButtonClickListener;
		private View layout;
		private CustomDialog dialog;
		private String editOne;
		private String editTwo;
		
		public Builder(Context context) {
			dialog = new CustomDialog(context, R.style.Dialog);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.dialog_layout, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

		public Builder seteditOne(String edit) {
			this.editOne = edit;
			return this;
		}
		
		public Builder seteditTwo(String edit) {
			this.editTwo = edit;
			return this;
		}
		
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setSingleButton(String singleButtonText, View.OnClickListener listener) {
            this.singleButtonText = singleButtonText;
            this.singleButtonClickListener = listener;
            return this;
        }
        

        /**
         * 创建单按钮对话框
         * @return
         */
        public CustomDialog createSingleButtonDialog() {
            showSingleButton();
            layout.findViewById(R.id.single_button).setOnClickListener(singleButtonClickListener);
            //如果传入的按钮文字为空，则使用默认的“返回”
            if (singleButtonText != null) {
                ((Button) layout.findViewById(R.id.single_button)).setText(singleButtonText);
            } else {
                ((Button) layout.findViewById(R.id.single_button)).setText("返回");
            }
            create();
            return dialog;
        }

        
        /**
         * 创建双按钮对话框
         * @return
         */
        public CustomDialog createTwoButtonDialog() {
            showTwoButton();
            layout.findViewById(R.id.positive_button).setOnClickListener(positiveButtonClickListener);
            layout.findViewById(R.id.negative_button).setOnClickListener(negativeButtonClickListener);
            //如果传入的按钮文字为空，则使用默认的“是”和“否”
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positive_button)).setText(positiveButtonText);
            } else {
                ((Button) layout.findViewById(R.id.positive_button)).setText("是");
            }
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negative_button)).setText(negativeButtonText);
            } else {
                ((Button) layout.findViewById(R.id.negative_button)).setText("否");
            }
            create();
            return dialog;
        }
        
        /**
         * 创建双按钮对话框和编辑框
         * @return
         */
        public CustomDialog createTwoButtonDialogWithEdit() {
            showTwoButton();
            showEditView();
            if(!editOne.equals(null)) {
            	((EditText) layout.findViewById(R.id.edit_one)).setText(editOne);
            }else {
            	((EditText) layout.findViewById(R.id.edit_one)).setText("你教教我嘛~");
            }
            
            if(!editOne.equals(null)) {
            	((EditText) layout.findViewById(R.id.edit_two)).setText(editTwo);
            }else {
            	((EditText) layout.findViewById(R.id.edit_two)).setText("答案嘞？");
            }
            layout.findViewById(R.id.positive_button).setOnClickListener(positiveButtonClickListener);
            layout.findViewById(R.id.negative_button).setOnClickListener(negativeButtonClickListener);
            //如果传入的按钮文字为空，则使用默认的“是”和“否”
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positive_button)).setText(positiveButtonText);
            } else {
                ((Button) layout.findViewById(R.id.positive_button)).setText("是");
            }
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negative_button)).setText(negativeButtonText);
            } else {
                ((Button) layout.findViewById(R.id.negative_button)).setText("否");
            }
            create();
            return dialog;
        }

        /**
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        private void create() {
            if (message != null) {      //设置提示内容
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {       //如果使用Builder的setContentview()方法传入了布局，则使用传入的布局
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            dialog.setContentView(layout);
            dialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示
        }

        /**
         * 显示双按钮布局，隐藏单按钮
         */
        private void showTwoButton() {
            layout.findViewById(R.id.single_button_layout).setVisibility(View.GONE);
            layout.findViewById(R.id.two_button_layout).setVisibility(View.VISIBLE);
        }

        /**
         * 显示单按钮布局，隐藏双按钮
         */
        private void showSingleButton() {
            layout.findViewById(R.id.single_button_layout).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.two_button_layout).setVisibility(View.GONE);
        }
        
        /**
         * 隐藏单按钮，隐藏双按钮
         */
        private void showOneuttonButListView() {
        	layout.findViewById(R.id.single_button_layout).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.two_button_layout).setVisibility(View.GONE);
        }
        
        private void showEditView() {
        	layout.findViewById(R.id.two_edit_text).setVisibility(View.VISIBLE);
        }
    }
		
}
