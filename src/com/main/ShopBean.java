package com.main;

public class ShopBean {
	public int shopId;				//��ƷID
	private int shopPicture;		//��ƷͼƬ��ԴID
	private String storeName;		//�������
	private String shopName;		//��Ʒ����
	private String shopDescription;	//��Ʒ����
	private float shopPrice;		//��Ʒ����
	private int shopNumber;			//��Ʒ����
	private boolean isChoosed;		//��Ʒ�Ƿ��ڹ��ﳵ�б�ѡ��
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public int getShopPicture() {
		return shopPicture;
	}
	public void setShopPicture(int shopPicture) {
		this.shopPicture = shopPicture;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopDescription() {
		return shopDescription;
	}
	public void setShopDescription(String shopDescription) {
		this.shopDescription = shopDescription;
	}
	public float getShopPrice() {
		return shopPrice;
	}
	public void setShopPrice(float shopPrice) {
		this.shopPrice = shopPrice;
	}
	public int getShopNumber() {
		return shopNumber;
	}
	public void setShopNumber(int shopNumber) {
		this.shopNumber = shopNumber;
	}
	public boolean isChoosed() {
		return isChoosed;
	}
	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}
}
