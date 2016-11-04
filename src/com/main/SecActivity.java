package com.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a_shop.MainActivity;
import com.example.a_shop.R;
import com.main_left.CateAdapter;
import com.main_left.CategoryBean;
import com.main_left.StickyListHeadersListView;
import com.main_left.StickyListHeadersListView.OnHeaderClickListener;
import com.main_left.StickyListHeadersListView.OnStickyHeaderChangedListener;
import com.main_left.StickyListHeadersListView.OnStickyHeaderOffsetChangedListener;
import com.order.OrderActivity;
import com.third.CheckOutActivity;

public class SecActivity extends Activity implements OnItemClickListener,
		OnHeaderClickListener, OnStickyHeaderChangedListener,
		OnStickyHeaderOffsetChangedListener, OnClickListener {
	private ListView lv_left;
	private StickyListHeadersListView lv_right;
	private TextView shopuser;// �û���
	private TextView popTotalPrice; // ����ļ۸�
	private TextView popRecycle; // �ղ�
	private TextView popCheckOut; // ����
	private TextView title_left; // title�����,����
	private TextView title_center; // title�м����
	private ImageView title_right;
	private LinearLayout layout; // ���㲼��
	private ShopAdapter shop_adapter; // �Զ�����Ʒ��Ϣ������
	private CateAdapter cate_adapter;
	private List<ShopBean> shoplist; // ���ﳵ���ݼ���
	private List<CategoryBean> cateList;
	private String userStr;

	private int statusBarHeight; // ��������ʱ����
	private View footerView;
	private TextView tv_footer;
	private boolean fadeHeader = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		initViews();
		init();
	}

	private void initViews() {
		lv_left = (ListView) findViewById(R.id.left_list);
		lv_right = (StickyListHeadersListView) findViewById(R.id.right_list);
		title_left = (TextView) findViewById(R.id.title_left);
		title_center = (TextView) findViewById(R.id.title_center);
		title_right = (ImageView) findViewById(R.id.title_right);
		shopuser = (TextView) findViewById(R.id.shopuser);
		popTotalPrice = (TextView) findViewById(R.id.shopTotalPrice);
		popRecycle = (TextView) findViewById(R.id.collection);
		popCheckOut = (TextView) findViewById(R.id.checkOut);
		layout = (LinearLayout) findViewById(R.id.price_relative);

		title_left.setText(R.string.backtomain);
		title_center.setText(R.string.sectitle);
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);
		popCheckOut.setOnClickListener(this);
		lv_left.setOnItemClickListener(this);
	}

	private void init() {
		/* ��ȡ�û��� */
		Bundle bundle = this.getIntent().getExtras();
		userStr=bundle.getString("userstr");
		if(userStr.equals("anonymous"))	//�����û���������
			title_right.setVisibility(View.GONE);
		else 
			title_right.setVisibility(View.VISIBLE);
		shopuser.setText("��ӭ���٣�" + userStr);

		getListData(); // ��������
		shoplist = ShoppingCanst.list;
		cateList = initCateData(); // ��������
		shop_adapter = new ShopAdapter(this, shoplist, handler,
				R.layout.list_second_right);
		cate_adapter = new CateAdapter(this, cateList);
		lv_left.setAdapter(cate_adapter);

		isAddFooter();
		lv_right.setOnHeaderClickListener(this);
		lv_right.setOnStickyHeaderChangedListener(this);
		lv_right.setOnStickyHeaderOffsetChangedListener(this);
		lv_right.setDrawingListUnderStickyHeader(true);
		lv_right.setAreHeadersSticky(true);
		lv_right.setAdapter(shop_adapter);
	}

	private void isAddFooter() {
		WindowManager windowManager = this.getWindowManager();
		@SuppressWarnings("deprecation")
		int screenheight = windowManager.getDefaultDisplay().getHeight();
		float scale = getResources().getDisplayMetrics().density;
		int dpvalue = (int) ((50 * scale + 0.5f) * 10);
		int dpvalue2 = (int) ((20 * scale) + 0.5f);
		int indexHeight = screenheight - dpvalue - statusBarHeight - dpvalue2;
		if (indexHeight > 0) {
			footerView = LayoutInflater.from(this).inflate(
					R.layout.main_left_footerview, null, false);
			tv_footer = (TextView) footerView.findViewById(R.id.tv_footer);
			LinearLayout.LayoutParams liParams = (LinearLayout.LayoutParams) tv_footer
					.getLayoutParams();
			liParams.height = indexHeight;
			lv_right.addFooterView(footerView, null, false);
		}
	}

	public void onHeaderClick(StickyListHeadersListView l, View header,
			int itemPosition, long headerId, boolean currentlySticky) {
	}

	public void onStickyHeaderOffsetChanged(StickyListHeadersListView l,
			View header, int offset) {
		if (fadeHeader
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
		}
	}

	public void onStickyHeaderChanged(StickyListHeadersListView l, View header,
			int itemPosition, long headerId) {
		ShopBean dishBean = shoplist.get(itemPosition);
		long id = dishBean.shopId;
		int position = getCateId(id);
		if (position != -1) {
			lv_left.setSelection(position);
			for (CategoryBean categoryBean : cateList) {
				categoryBean.isSelected = false;
			}
			cateList.get(position).isSelected = true;
			cate_adapter.notifyDataSetChanged();
		}
	}

	private int getCateId(long id) {
		int position = 0;
		for (int i = 0; i < cateList.size(); i++) {
			position = i;
			if (id == cateList.get(i).id) {
				return position;
			}
		}
		return -1;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		statusBarHeight = getStatusBarHeight(this);
	}

	private int getStatusBarHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	// ��list
	private int shopre[][] = {
			{ R.drawable.ima6, R.drawable.ima7, R.drawable.ima8,
					R.drawable.ima5, R.drawable.ima10, R.drawable.ima9,
					R.drawable.ima3 },
			{ R.drawable.ima4, R.drawable.ima2, R.drawable.ima1,
					R.drawable.ima0 },
			{ R.drawable.ima0, R.drawable.ima1, R.drawable.ima2,
					R.drawable.ima3 },
			{ R.drawable.ima4, R.drawable.ima5, R.drawable.ima6,
					R.drawable.ima7, R.drawable.ima8, R.drawable.ima9,
					R.drawable.ima10 } };
	private String shopname[][] = {
			{ "��ɽ������", "���������", "�����׷�", "�⺣ȫ����", "��������", "ͧ����", "��������" },
			{ "���ǹ���", "���ѻ�����", "ţ���ѵ���", "�ӰǷ�" },
			{ "�ӰǷ�", "ţ���ѵ���", "���ѻ�����", "��������" },
			{ "���ǹ���", "�⺣ȫ����", "��ɽ������", "���������", "�����׷�", "ͧ����", "��������" } };
	private String shopdes[][] = {
			{ "�ر�����", "��������ף�����3Сʱ����", "��������������ĥ�ɽ����ӹ�", "�����⺣��ʳ", "��������ţ����",
					"���ݴ�ͳС��", "�ں����������������" },
			{ "������ɫ", "�й����˶ӷ�����ɫ", "����ţ��+9���켦��", "���̻�����ʳ" },
			{ "���̻�����ʳ", "����ţ��+9���켦��", "�й����˶ӷ�����ɫ", "�ں����������������" },
			{ "������ɫ", "�����⺣��ʳ", "�ر�����", "��������ף�����3Сʱ����", "��������������ĥ�ɽ����ӹ�",
					"���ݴ�ͳС��", "��������ţ����" } };
	private int shopprice[][] = { { 4, 7, 9, 13, 13, 14, 14 },
			{ 15, 16, 24, 30 }, { 30, 24, 16, 14 }, { 15, 13, 4, 7, 9, 14, 13 } };

	public void getListData() {
		ShoppingCanst.list = new ArrayList<ShopBean>();
		for (int j = 0; j < shopname.length; j++) {
			for (int i = 0; i < shopname[j].length; i++) {
				ShopBean bean = new ShopBean();
				bean.setShopId(j);
				bean.setShopPicture(shopre[j][i]);
				bean.setStoreName("" + Integer.toString(shopre[j][i]));
				bean.setShopName("" + shopname[j][i]);
				bean.setShopDescription("" + shopdes[j][i]);
				bean.setShopPrice(shopprice[j][i]);
				bean.setShopNumber(1);
				bean.setChoosed(false);
				ShoppingCanst.list.add(bean);
			}
		}
	}

	// ��list
	private String[] sort = { "0-14", "12����", "����", "�������" };

	private List<CategoryBean> initCateData() {
		List<CategoryBean> categoryBean_list = new ArrayList<CategoryBean>();
		for (int i = 0; i < 4; i++) {
			CategoryBean categoryBean = new CategoryBean();
			if (i == 0) {
				categoryBean.isSelected = true;
			} else {
				categoryBean.isSelected = false;
			}
			categoryBean.id = i;
			categoryBean.name = sort[i] + " List" + (i + 1);
			categoryBean_list.add(categoryBean);
		}
		return categoryBean_list;
	}

	private String deleteOrCheckOutShop() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < shoplist.size(); i++) {
			if (ShopAdapter.getIsSelected().get(i)) {
				sb.append(i);
				sb.append(",");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private void goCheckOut() {
		String shopIndex = deleteOrCheckOutShop();
		Intent checkOutIntent = new Intent(SecActivity.this,
				CheckOutActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString("userstr2",userStr);
		checkOutIntent.putExtras(bundle);
		checkOutIntent.putExtra("shopIndex", shopIndex);
		startActivity(checkOutIntent);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 10) { // ����ѡ����Ʒ���ܼ۸�
				float price = (Float) msg.obj;
				if (price > 0) {
					popTotalPrice.setText("��" + price);
					layout.setVisibility(View.VISIBLE);
				} else {
					layout.setVisibility(View.GONE);
				}
			} else if (msg.what == 11) {
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// ������item���Ҳ��Ʒ��ת����Ӧ��λ��
		CategoryBean categoryBean = cateList.get(position);
		for (int i = 0; i < shoplist.size(); i++) {
			ShopBean shopBean = shoplist.get(i);
			if (categoryBean.id == shopBean.shopId) {
				lv_right.setSelection(i);
				break;
			}
		}
	}

	@Override
	public void onClick(View view) {
		int rid = view.getId();
		if (rid == R.id.checkOut)
			goCheckOut();
		else if (rid == R.id.title_left) {
			Intent intent = new Intent(SecActivity.this, MainActivity.class);
			startActivity(intent);
		} else if (rid == R.id.title_right) {
			Intent intent2 = new Intent(SecActivity.this, OrderActivity.class);
			startActivity(intent2);
		}
	}
}
