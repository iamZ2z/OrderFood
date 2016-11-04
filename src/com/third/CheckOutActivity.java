package com.third;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a_shop.R;
import com.main.AddressBean;
import com.main.NoScrollListView;
import com.main.ShopBean;
import com.main.ShoppingCanst;
import com.main.UpdateAddressActivity;
import com.order.OrderActivity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CheckOutActivity extends Activity implements
		OnValueChangeListener, Formatter, OnScrollListener {
	private Button sureCheckOut; // ȷ�Ϲ���
	private TextView addresseeName; // �ջ�������
	private TextView smearedAddress; // �ջ�������ַ
	private TextView detailAddress; // �ջ�����ϸ��ַ
	private TextView checkOutAllPrice; // ������ܽ��
	private TextView title_left; // title�����,����
	private TextView title_center; // title�м����
	private ImageView title_right;
	private RelativeLayout addressRelative; // ��ʾ�ջ�����Ϣ�Ĳ���
	private NoScrollListView checkOutListView;// ��Ʒ�б�
	private TextView leaveWorld;// �˿�����
	private NumberPicker desknum;

	private CheckOutAdapter adapter;
	private List<ShopBean> list; // ������Ʒ���ݼ���
	private List<AddressBean> addressList; // �ջ��˵�ַ���ݼ���

	private static int REQUESTCODE = 1; // ��ת������
	private float allPrice = 0; // ������Ʒ��Ҫ���ܽ��
	private int desk_num;

	private String str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		Bmob.initialize(this, "a0b799e4e10c97f4f6732691db41f575");
		initView();
		init();
	}

	private void putData() {
		CheckOutBmobBean checkOutBmobBean = new CheckOutBmobBean();
		StringBuffer temp = new StringBuffer();

		String shopIndex = getIntent().getStringExtra("shopIndex");
		String[] shopIndexs = shopIndex.split(",");
		for (String s : shopIndexs) {
			int position = Integer.valueOf(s);
			ShopBean bean = ShoppingCanst.list.get(position);
			temp.append(bean.getShopName()).append("x")
					.append(bean.getShopNumber()).append(" ");
		}
		checkOutBmobBean.setShop_labuchang(temp);
		checkOutBmobBean.setAccount(str);
		checkOutBmobBean.setDesknum(desk_num);
		checkOutBmobBean.setLeaveword("" + leaveWorld.getText());
		checkOutBmobBean.setTotalprice("" + allPrice);
		checkOutBmobBean.save(new SaveListener<String>() {
			@Override
			public void done(String arg0, BmobException arg1) {
				if (arg1 == null)
					Toast.makeText(CheckOutActivity.this, "���ͳɹ�",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(CheckOutActivity.this, "����ʧ��",
							Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void init() {
		Bundle bundle = this.getIntent().getExtras();
		str = bundle.getString("userstr2");
		if (str.equals("anonymous")) // �����û���������
			title_right.setVisibility(View.GONE);
		else
			title_right.setVisibility(View.VISIBLE);
		list = new ArrayList<ShopBean>();
		String shopIndex = getIntent().getStringExtra("shopIndex");
		String[] shopIndexs = shopIndex.split(",");
		for (String s : shopIndexs) {
			int position = Integer.valueOf(s);
			ShopBean bean = ShoppingCanst.list.get(position);
			allPrice += bean.getShopNumber() * bean.getShopPrice();
			list.add(bean);
		}
		getAddressData();
		addressList = ShoppingCanst.addressList;
		checkOutAllPrice.setText("��л���Ĺ���:" + str + "      ��" + list.size()
				+ "����Ʒ       �ܼۣ�" + allPrice);
		showInfo(0); // Ĭ����ʾ��һ����ַ��Ϣ

		adapter = new CheckOutAdapter(this, list, R.layout.checkout_item);
		checkOutListView.setAdapter(adapter);
	}

	// ��ʼ��UI����
	private void initView() {
		sureCheckOut = (Button) findViewById(R.id.sureCheckOut);
		sureCheckOut.setBackgroundColor(Color.parseColor("#FF0000"));
		sureCheckOut.setTextColor(this.getResources().getColor(
				R.color.light_green));
		addresseeName = (TextView) findViewById(R.id.addresseeName);
		smearedAddress = (TextView) findViewById(R.id.smearedAddress);
		detailAddress = (TextView) findViewById(R.id.detailAddress);
		checkOutAllPrice = (TextView) findViewById(R.id.checkOutAllPrice);
		title_left = (TextView) findViewById(R.id.title_left);
		title_center = (TextView) findViewById(R.id.title_center);
		title_right = (ImageView) findViewById(R.id.title_right);
		checkOutListView = (NoScrollListView) findViewById(R.id.checkOutListView);
		addressRelative = (RelativeLayout) findViewById(R.id.addressRelative);
		leaveWorld = (TextView) findViewById(R.id.leaveWorld);
		desknum = (NumberPicker) findViewById(R.id.desknum);

		ClickListener cl = new ClickListener();
		title_left.setText(R.string.sureOrder);
		title_center.setText(R.string.checkOut);
		title_left.setOnClickListener(cl);
		title_right.setOnClickListener(cl);
		sureCheckOut.setOnClickListener(cl);
		addressRelative.setOnClickListener(cl);
		desknum.setFormatter(this);
		desknum.setOnValueChangedListener(this);
		desknum.setOnScrollListener(this);
		desknum.setValue(1);
		desknum.setMinValue(0);
		desknum.setMaxValue(50);
	}

	// ��ʾ�ջ���������ַ����Ϣ
	private void showInfo(int index) {
		AddressBean bean = addressList.get(index);
		addresseeName.setText(bean.getName());
		smearedAddress.setText(bean.getSmearedAddress());
		detailAddress.setText(bean.getDetailAddress());
	}

	// ��ȡ�ջ��˵�ַ���ݼ���
	private void getAddressData() {
		ShoppingCanst.addressList = new ArrayList<AddressBean>();
		AddressBean bean = new AddressBean();
		bean.setName("�ſ�");
		bean.setSmearedAddress("����ʡ�人�������");
		bean.setDetailAddress("�����羰���ƼҴ�����1��  15527196048");
		ShoppingCanst.addressList.add(bean);
		AddressBean bean2 = new AddressBean();
		bean2.setName("����");
		bean2.setSmearedAddress("����ʡ�人�������");
		bean2.setDetailAddress("�ƼҴ�����1�Ŷ����羰��  18140549110");
		ShoppingCanst.addressList.add(bean2);
	}

	private final class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int rid = v.getId();
			if (rid == R.id.sureCheckOut) { // ȷ�ϵ����ť
				Toast.makeText(getApplicationContext(),
						"������ɣ��ܹ����ѣ�" + allPrice, Toast.LENGTH_SHORT).show();
				putData();
			} else if (rid == R.id.addressRelative) { // �޸ĵ�ַ
				Intent intent = new Intent(CheckOutActivity.this,
						UpdateAddressActivity.class);
				startActivityForResult(intent, REQUESTCODE);
			} else if (rid == R.id.title_left) { // ����ⷵ��
				finish();
			} else if (rid == R.id.title_right) {
				Intent intent2 = new Intent(CheckOutActivity.this,
						OrderActivity.class);
				startActivity(intent2);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			handler.sendMessage(handler.obtainMessage(1,
					bundle.getInt("addressIndex")));
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) { // �첽���ĵ�ַ
				int tempIndex = (Integer) msg.obj;
				showInfo(tempIndex);
			}
		}
	};

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		desk_num = newVal;
	}

	@Override
	public String format(int value) {
		String tempStr = String.valueOf(value);
		if (value < 10)
			tempStr = "0" + tempStr;
		return tempStr;
	}

	@Override
	public void onScrollStateChange(NumberPicker view, int scrollState) {
	}
}