package com.theYarnestShop.model;

public class ProductModel {
	private String product_id;
	private String product_name;
	private String category;
	private String description;
	private float price;
	private String image;
	private int stock;
	
	public ProductModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductModel(String product_id, String product_name, String category, String description, float price,
			String image, int stock) {
		super();
		this.product_id = product_id;
		this.product_name = product_name;
		this.category = category;
		this.description = description;
		this.price = price;
		this.image = image;
		this.stock = stock;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
	
}
