package com.theYarnestShop.model;

public class UserOrderModel {
	private String order_id;
	private int user_id;
	
	public UserOrderModel(String order_id, int user_id) {
		super();
		this.order_id = order_id;
		this.user_id = user_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
}
