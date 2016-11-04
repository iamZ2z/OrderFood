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
	private Button sureCheckOut; // 确认购买
	private TextView addresseeName; // 收货人姓名
	private TextView smearedAddress; // 收货人区地址
	private TextView detailAddress; // 收货人详细地址
	private TextView checkOutAllPrice; // 结算的总金额
	private TextView title_left; // title左标题,返回
	private TextView title_center; // title中间标题
	private ImageView title_right;
	private RelativeLayout addressRelative; // 显示收货人信息的布局
	private NoScrollListView checkOutListView;// 商品列表
	private TextView leaveWorld;// 顾客留言
	private NumberPicker desknum;

	private CheckOutAdapter adapter;
	private List<ShopBean> list; // 结算商品数据集合
	private List<AddressBean> addressList; // 收货人地址数据集合

	private static int REQUESTCODE = 1; // 跳转请求码
	private float allPrice = 0; // 购买商品需要的总金额
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
					Toast.makeText(CheckOutActivity.this, "发送成功",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(CheckOutActivity.this, "发送失败",
							Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void init() {
		Bundle bundle = this.getIntent().getExtras();
		str = bundle.getString("userstr2");
		if (str.equals("anonymous")) // 匿名用户订单隐藏
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
		checkOutAllPrice.setText("感谢您的光临:" + str + "      共" + list.size()
				+ "个商品       总价￥" + allPrice);
		showInfo(0); // 默认显示第一条地址信息

		adapter = new CheckOutAdapter(this, list, R.layout.checkout_item);
		checkOutListView.setAdapter(adapter);
	}

	// 初始化UI界面
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

	// 显示收货人姓名地址等信息
	private void showInfo(int index) {
		AddressBean bean = addressList.get(index);
		addresseeName.setText(bean.getName());
		smearedAddress.setText(bean.getSmearedAddress());
		detailAddress.setText(bean.getDetailAddress());
	}

	// 获取收货人地址数据集合
	private void getAddressData() {
		ShoppingCanst.addressList = new ArrayList<AddressBean>();
		AddressBean bean = new AddressBean();
		bean.setName("张骏");
		bean.setSmearedAddress("湖北省武汉市武昌区");
		bean.setDetailAddress("东湖风景区黄家大湾特1号  15527196048");
		ShoppingCanst.addressList.add(bean);
		AddressBean bean2 = new AddressBean();
		bean2.setName("吴威");
		bean2.setSmearedAddress("湖北省武汉市武昌区");
		bean2.setDetailAddress("黄家大湾特1号东湖风景区  18140549110");
		ShoppingCanst.addressList.add(bean2);
	}

	private final class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int rid = v.getId();
			if (rid == R.id.sureCheckOut) { // 确认点击按钮
				Toast.makeText(getApplicationContext(),
						"结算完成，总共花费￥" + allPrice, Toast.LENGTH_SHORT).show();
				putData();
			} else if (rid == R.id.addressRelative) { // 修改地址
				Intent intent = new Intent(CheckOutActivity.this,
						UpdateAddressActivity.class);
				startActivityForResult(intent, REQUESTCODE);
			} else if (rid == R.id.title_left) { // 左标题返回
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
			if (msg.what == 1) { // 异步更改地址
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