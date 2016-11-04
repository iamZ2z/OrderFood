package com.example.a_shop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.example.a_shop.R;

public class LoginActivity extends Activity {
	EditText ev1, ev2, ev3;
	private Button btn1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Bmob.initialize(this, "a0b799e4e10c97f4f6732691db41f575");

		ev1 = (EditText) findViewById(R.id.et1);
		ev2 = (EditText) findViewById(R.id.et2);
		ev3 = (EditText) findViewById(R.id.et3);
		btn1 = (Button) findViewById(R.id.login);

		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserBean p1 = new UserBean();
				p1.setUser(ev1.getText().toString());
				p1.setPass(ev2.getText().toString());
				p1.setPhone(ev3.getText().toString());
				p1.save(new SaveListener<String>() {
					@Override
					public void done(String objectId, BmobException e) {
						if (e == null) {
							// toast("添加数据成功，返回objectId为："+objectId);
							Toast.makeText(LoginActivity.this, "添加数据成功",
									Toast.LENGTH_SHORT).show();
						} else {
							// toast("创建数据失败：" + e.getMessage());
							Toast.makeText(LoginActivity.this,
									"创建数据失败：" + e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});

	}
}
