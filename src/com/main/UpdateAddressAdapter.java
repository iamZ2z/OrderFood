package com.main;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a_shop.R;

public class UpdateAddressAdapter extends BaseAdapter {
	private int resourceId;					//��Ŀ��ͼ��ԴID
	private List<AddressBean> list;			//��ַ���ݼ���
	private LayoutInflater inflater;		//���������
	public UpdateAddressAdapter(Context context,List<AddressBean> list
			,int resourceId){
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
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AddressBean bean = list.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(resourceId, null);
			holder.addresseeItemName = (TextView) 
					convertView.findViewById(R.id.addresseeItemName);
			holder.smearedItemAddress = (TextView)
					convertView.findViewById(R.id.smearedItemAddress);
			holder.detailItemAddress = (TextView)
					convertView.findViewById(R.id.detailItemAddress);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.addresseeItemName.setText(bean.getName());
		holder.smearedItemAddress.setText(bean.getSmearedAddress());
		holder.detailItemAddress.setText(bean.getDetailAddress());
		
		return convertView;
	}
	
	private final class ViewHolder{
		public TextView addresseeItemName;	//�ռ�������
		public TextView smearedItemAddress;	//����ַ
		public TextView detailItemAddress;	//��ϸ��ַ
	}

}
