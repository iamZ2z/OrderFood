package com.main;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a_shop.R;
import com.main_left.StickyListHeadersAdapter;

public class ShopAdapter extends BaseAdapter implements
		StickyListHeadersAdapter {
	private Handler mHandler;
	private int resourceId; // ��������ͼ��ԴID
	private Context context; // ���������
	private List<ShopBean> list; // ���ݼ���List
	private LayoutInflater inflater; // ���������
	private static HashMap<Integer, Boolean> isSelected;

	public ShopAdapter(Context context, List<ShopBean> list, Handler mHandler,
			int resourceId) {
		this.list = list;
		this.context = context;
		this.mHandler = mHandler;
		this.resourceId = resourceId;
		inflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();
		initDate();
	}

	private void initDate() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		ShopAdapter.isSelected = isSelected;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShopBean bean = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(resourceId, null);
			holder = new ViewHolder();
			holder.shop_photo = (ImageView) convertView
					.findViewById(R.id.shop_photo);
			holder.shop_name = (TextView) convertView
					.findViewById(R.id.shop_name);
			holder.shop_description = (TextView) convertView
					.findViewById(R.id.shop_description);
			holder.shop_price = (TextView) convertView
					.findViewById(R.id.shop_price);
			holder.shop_number = (TextView) convertView
					.findViewById(R.id.shop_number);
			holder.shop_check = (CheckBox) convertView
					.findViewById(R.id.shop_check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.shop_photo.setImageResource(bean.getShopPicture());
		holder.shop_name.setText(bean.getShopName());
		holder.shop_description.setText(bean.getShopDescription());
		holder.shop_price.setText("��" + bean.getShopPrice());
		holder.shop_number.setTag(position);
		holder.shop_number.setText(String.valueOf(bean.getShopNumber()));
		holder.shop_number.setOnClickListener(new ShopNumberClickListener());
		holder.shop_check.setTag(position);
		holder.shop_check.setChecked(getIsSelected().get(position));
		holder.shop_check
				.setOnCheckedChangeListener(new CheckBoxChangedListener());
		return convertView;
	}

	private final class ViewHolder {
		public ImageView shop_photo; // ��ƷͼƬ
		public TextView shop_name; // ��Ʒ����
		public TextView shop_description; // ��Ʒ����
		public TextView shop_price; // ��Ʒ�۸�
		public TextView shop_number; // ��Ʒ����
		public CheckBox shop_check; // ��Ʒѡ��ť
	}// ����TextView���������

	private final class ShopNumberClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// ��ȡ��Ʒ������
			String str = ((TextView) v).getText().toString();
			int shopNum = Integer.valueOf(str);
			showDialog(shopNum, (TextView) v);
		}
	}

	private int number = 0; // ��¼�Ի����е�����
	private EditText editText; // �Ի����������༭��

	/**
	 * �����Ի��������Ʒ������
	 * 
	 * @param shopNum
	 *            ��Ʒԭ��������
	 * @param textNum
	 *            Item����ʾ��Ʒ�����Ŀؼ�
	 */
	private void showDialog(int shopNum, final TextView textNum) {
		View view = inflater.inflate(R.layout.number_update, null);
		Button btnSub = (Button) view.findViewById(R.id.numSub);
		Button btnAdd = (Button) view.findViewById(R.id.numAdd);
		editText = (EditText) view.findViewById(R.id.edt);
		editText.setText(String.valueOf(shopNum));
		btnSub.setOnClickListener(new ButtonClickListener());
		btnAdd.setOnClickListener(new ButtonClickListener());
		number = shopNum;
		new AlertDialog.Builder(context).setView(view)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// ���û����ĵ���Ʒ�������µ�������
						int position = (Integer) textNum.getTag();
						list.get(position).setShopNumber(number);
						handler.sendMessage(handler.obtainMessage(1, textNum));
					}
				}).setNegativeButton("ȡ��", null).create().show();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) { // ������Ʒ����
				((TextView) msg.obj).setText(String.valueOf(number));
				// ������Ʒ������֪ͨActivity������Ҫ���ѵ��ܽ��
				mHandler.sendMessage(mHandler
						.obtainMessage(10, getTotalPrice()));
			} else if (msg.what == 2) // ���ĶԻ����е�����
				editText.setText(String.valueOf(number));
		}
	};

	// Button���������
	private final class ButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.numSub) {
				if (number > 1) {
					number--;
					handler.sendEmptyMessage(2);
				}
			} else if (v.getId() == R.id.numAdd) {
				number++;
				handler.sendEmptyMessage(2);
			}
		}
	}

	// CheckBoxѡ��ı������
	private final class CheckBoxChangedListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton cb, boolean flag) {
			int position = (Integer) cb.getTag();
			getIsSelected().put(position, flag);
			ShopBean bean = list.get(position);
			bean.setChoosed(flag);
			mHandler.sendMessage(mHandler.obtainMessage(10, getTotalPrice()));
		}
	}

	/**
	 * ����ѡ����Ʒ�Ľ��
	 * 
	 * @return ������Ҫ���ѵ��ܽ��
	 */
	private float getTotalPrice() {
		ShopBean bean = null;
		float totalPrice = 0;
		for (int i = 0; i < list.size(); i++) {
			bean = list.get(i);
			if (bean.isChoosed()) {
				totalPrice += bean.getShopNumber() * bean.getShopPrice();
			}
		}
		return totalPrice;
	}

	class HeaderViewHolder {
		// list_header
		TextView text;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = inflater.inflate(R.layout.main_left_header, parent,
					false);
			holder.text = (TextView) convertView
					.findViewById(R.id.tv_header_title);
			convertView.setTag(holder);
		} else
			holder = (HeaderViewHolder) convertView.getTag();
		holder.text.setText((list.get(position).shopId + 1) + " group");
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return list.get(position).shopId;
	}
}