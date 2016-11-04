package com.example.a_shop;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import com.main.SecActivity;

//登录注册界面
public class MainActivity extends Activity {
	private ImageView btn_login, btn_register, btn_noname, iv_loading;
	private EditText ev_account, ev_password;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Bmob.initialize(this, "a0b799e4e10c97f4f6732691db41f575");
		btn_login = (ImageView) findViewById(R.id.btn1);
		btn_register = (ImageView) findViewById(R.id.btn2);
		btn_noname = (ImageView) findViewById(R.id.btn3);
		ev_account = (EditText) findViewById(R.id.ev1);
		ev_password = (EditText) findViewById(R.id.ev2);
		iv_loading = (ImageView) findViewById(R.id.iv_loading);

		btn_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 查找Person表里的数据
				BmobQuery<UserBean> bmobQuery = new BmobQuery<UserBean>();
				bmobQuery.addWhereEqualTo("user", ev_account.getText()
						.toString());
				bmobQuery.addWhereEqualTo("pass", ev_password.getText()
						.toString());
				handler.post(runnable);
				bmobQuery.findObjects(new FindListener<UserBean>() {
					@Override
					public void done(List<UserBean> object, BmobException e) {
						if (object.size() == 1) {
							handler.post(runnable2);
							Toast.makeText(MainActivity.this, "查询成功",
									Toast.LENGTH_SHORT).show();

							Intent intent = new Intent();
							Bundle bundle = new Bundle();
							bundle.putString("userstr", ev_account.getText()
									.toString());
							intent.putExtras(bundle);
							intent.setClass(MainActivity.this,
									SecActivity.class);
							startActivity(intent);
						} else if (object.size() == 0) {
							handler.post(runnable2);
							Toast.makeText(MainActivity.this, "帐号或密码错误",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});

		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});

		btn_noname.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("userstr", "anonymous");
				intent.putExtras(bundle);
				intent.setClass(MainActivity.this, SecActivity.class);
				startActivity(intent);
			}
		});
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			iv_loading.setVisibility(View.VISIBLE);
		}
	};
	Runnable runnable2 = new Runnable() {
		@Override
		public void run() {
			iv_loading.setVisibility(View.GONE);
		}
	};
}
