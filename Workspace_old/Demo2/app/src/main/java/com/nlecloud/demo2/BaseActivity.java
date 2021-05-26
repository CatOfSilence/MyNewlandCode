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
	 * ��ʾԲ�ν���
	 * @param text
	 */
	public void ShowProgressDialog(String text){
		dialog = new ProgressDialog(this); 
		//���ý�������񣬷��ΪԲ�Σ���ת�� 
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		//����ProgressDialog ��ʾ��Ϣ 
		dialog.setMessage(text); 
		//����ProgressDialog ����ͼ�� 
		dialog.setIcon(android.R.drawable.ic_dialog_map); 
		//����ProgressDialog �Ľ������Ƿ���ȷ 
		dialog.setIndeterminate(false); 
		//����ProgressDialog �Ƿ���԰��˻ذ���ȡ�� 
		dialog.setCancelable(true); 
		//��ʾ 
		dialog.show();
	}
	
	/**
	 * ����Բ�ν�����
	 */
	public void DismissProgressDialog(){
		dialog.dismiss();
	}
	
}
