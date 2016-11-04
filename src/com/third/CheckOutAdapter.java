package com.third;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a_shop.R;
import com.main.ShopBean;

public class CheckOutAdapter extends BaseAdapter {
	private int resourceId; // ��ͼ��ԴID
	private List<ShopBean> list; // ѡ����Ʒ����
	private LayoutInflater inflater; // ���������

	public CheckOutAdapter(Context context, List<ShopBean> list, int resourceId) {
		this.list = list;
		this.resourceId = resourceId;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShopBean bean = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(resourceId, null);
			holder.storeName = (TextView) convertView
					.findViewById(R.id.storeName);
			holder.checkOut_price = (TextView) convertView
					.findViewById(R.id.checkOut_price);
			holder.checkOut_description = (TextView) convertView
					.findViewById(R.id.checkOut_description);
			holder.checkOut_number = (TextView) convertView
					.findViewById(R.id.checkOut_number);
			holder.checkOut_singlePrice = (TextView) convertView
					.findViewById(R.id.checkOut_singlePrice);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		float allPrice = bean.getShopNumber() * bean.getShopPrice();
		holder.storeName.setText(bean.getStoreName());
		holder.checkOut_price.setText("��" + allPrice);
		holder.checkOut_description.setText(bean.getShopName());
		holder.checkOut_number.setText(bean.getShopNumber() + "��");
		holder.checkOut_singlePrice.setText("��" + bean.getShopPrice());

		return convertView;
	}

	private final class ViewHolder {
		public TextView storeName; // �������
		public TextView checkOut_price; // ����Ʒ�����ܽ��
		public TextView checkOut_description; // ��Ʒ����
		public TextView checkOut_number; // ��Ʒ����
		public TextView checkOut_singlePrice; // ������Ʒ�ļ۸�
	}
}
