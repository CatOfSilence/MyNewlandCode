/**
 * 
 */
package com.nlecloud.demo2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

/**
 * @author JH
 *
 */
public class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	

	Toast mToast;

	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mToast == null) {
						mToast = Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_LONG);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});
			
		}
	}

	ProgressDialog dialog;
	
	/**
	 * 显示圆形进度
	 * @param text
	 */
	public void ShowProgressDialog(String text){
		dialog = new ProgressDialog(this); 
		//设置进度条风格，风格为圆形，旋转的 
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		//设置ProgressDialog 提示信息 
		dialog.setMessage(text); 
		//设置ProgressDialog 标题图标 
		dialog.setIcon(android.R.drawable.ic_dialog_map); 
		//设置ProgressDialog 的进度条是否不明确 
		dialog.setIndeterminate(false); 
		//设置ProgressDialog 是否可以按退回按键取消 
		dialog.setCancelable(true); 
		//显示 
		dialog.show();
	}
	
	/**
	 * 隐藏圆形进度条
	 */
	public void DismissProgressDialog(){
		dialog.dismiss();
	}
	
}
