package com.theYarnestShop.model;

public class OrderModel {
	private String order_id;
	private String product_id;
	private String order_date;
	private int quantity;
	private float total_price;
	
	public OrderModel(String order_id, String product_id, String order_date, int quantity, float total_price) {
		super();
		this.order_id = order_id;
		this.product_id = product_id;
		this.order_date = order_date;
		this.quantity = quantity;
		this.total_price = total_price;
	}
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getTotal_price() {
		return total_price;
	}
	public void setTotal_price(float total_price) {
		this.total_price = total_price;
	}
		
}
