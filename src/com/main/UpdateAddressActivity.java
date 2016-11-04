package com.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a_shop.R;

public class UpdateAddressActivity extends Activity {
	private TextView title_left;			//�����
	private TextView title_center;			//�м����
	private ListView updateAddressListView;	//�ϼ�������ʾ�б�
	private UpdateAddressAdapter adapter;	//�Զ���������
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updateaddress);
		initView();
		init();
	}

	private void initView() {
		title_left = (TextView) findViewById(R.id.title_left);
		title_center = (TextView) findViewById(R.id.title_center);
		updateAddressListView = (ListView) findViewById(R.id.updateAddressListView);
		
		title_left.setText(R.string.checkOut);
		title_center.setText(R.string.updateAddress);
		title_left.setOnClickListener(new textViewClickListener());
		updateAddressListView.setOnItemClickListener(new ListViewItemClickListener());
	
	}

	private void init() {
		if(ShoppingCanst.addressList != null){
			adapter = new UpdateAddressAdapter(this,ShoppingCanst.addressList
					,R.layout.updateaddress_item);
			updateAddressListView.setAdapter(adapter);
		}
	}
	//textView�¼����������
		private final class textViewClickListener implements OnClickListener{
			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.title_left){
					//����������
					finish();
				}
			}
		}
		
		//ListView��Ŀ���������
		private final class ListViewItemClickListener implements OnItemClickListener{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("addressIndex", position);
				setResult(RESULT_OK,intent);
				finish();
			}
		}
}
