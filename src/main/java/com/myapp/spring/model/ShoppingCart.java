

package com.myapp.spring.model;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Id;


public class ShoppingCart {




public static String ORDERED = "ordered";

public static String PENDING = "pending";




@Id

private String id;




public String status; //pending, ordered




public String userName;




public HashMap<Integer, Product> products;




public HashMap<Integer, Integer> productQuantities;






public Date lastModified;






public Date orderDate;




//total price

public int totalPrice = 0;




public ShoppingCart(){}




public ShoppingCart(String status, String userName,

HashMap<Integer, Product> products,

HashMap<Integer, Integer> productQuantities,

Date orderDate, Date lastModified, int totalPrice){

this.status = status;

this.userName = userName;

this.products = products;

this.productQuantities = productQuantities;

this.orderDate = orderDate;

this.lastModified = lastModified;

this.totalPrice = totalPrice;

}




public String getId(){

return this.id;

}




public Product getProductFromId(String productId){

return this.products.get(productId);

}




public void addProduct(Product product){




//Check if the product is in the HashMap

Product fromCart = this.products.get(product.getProductId());




if(fromCart == null){

this.products.put(product.getProductId(), product);

}




}




public void addProductQuantity(Product product){

Integer productId = product.getProductId();

if(this.productQuantities.containsKey(productId)){

int quantity = this.productQuantities.get(productId);

quantity++;

this.productQuantities.put(productId, quantity);

}

else {

// init the product quantities if key not found

this.productQuantities.put(productId, 1);

}

this.totalPrice += product.getPrice();

}




public void removeProduct(Integer productId){

if(this.products.containsKey(productId)){

this.products.remove(productId);

}

}




public void removeProductQuantity(Product product){




Integer productId = product.getProductId();

if(this.productQuantities.containsKey(productId)){

int quantity = this.productQuantities.get(productId);

quantity--;

//remove datas from the HashMaps when quantity is too low

if(quantity <1){

this.productQuantities.remove(productId);

this.removeProduct(productId);

}

else {

this.productQuantities.put(productId, quantity);

}

this.totalPrice -= product.getPrice();
}

}

}