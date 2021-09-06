package com.myapp.spring.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="products")
public class Product {

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PRODUCT_ID")
	private Integer productId;
	
	
	@Column(name = "PRODUCT_NAME")
	@NotNull
	private String productName;
	
	@Column(name = "PRODUCT_DESCRIPTION")
	@NotNull
	private String description;
	
	@Column(name = "PRODUCT_PRICE")
	@NotNull
	private Double price;
	
	@Column(name = "PRODUCT_RATING")
	@NotNull
	private Double starRating;
	
	public Product() {
		// TODO Auto-generated constructor stub
	}

	public Product(@NotNull String productName, @NotNull String description, @NotNull Double price,
			@NotNull Double starRating) {
		this.productName = productName;
		this.description = description;
		this.price = price;
		this.starRating = starRating;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getStarRating() {
		return starRating;
	}

	public void setStarRating(Double starRating) {
		this.starRating = starRating;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, price, productId, productName, starRating);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Product))
			return false;
		Product other = (Product) obj;
		return Objects.equals(description, other.description) && Objects.equals(price, other.price)
				&& Objects.equals(productId, other.productId) && Objects.equals(productName, other.productName)
				&& Objects.equals(starRating, other.starRating);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Product [productId=");
		builder.append(productId);
		builder.append(", productName=");
		builder.append(productName);
		builder.append(", description=");
		builder.append(description);
		builder.append(", price=");
		builder.append(price);
		builder.append(", starRating=");
		builder.append(starRating);
		builder.append("]");
		return builder.toString();
	}
	
	
}

