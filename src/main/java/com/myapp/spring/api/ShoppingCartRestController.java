package com.myapp.spring.api;


import static org.assertj.core.api.Assertions.entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.myapp.spring.model.Product;
import com.myapp.spring.model.ShoppingCart;
import com.myapp.spring.repository.ProductRepository;
import com.myapp.spring.repository.ShoppingCartRepository;

@RestController
@RequestMapping("/carts")
public class ShoppingCartRestController {

	@Autowired
private  ShoppingCartRepository shoppingCartRepository;
	@Autowired
private  ProductRepository productRepository;

@RequestMapping(method = RequestMethod.GET)
public ResponseEntity<List<ShoppingCart>> getShoppingCarts() throws ShoppingCartListNotFoundException{

List<ShoppingCart> carts = this.shoppingCartRepository.findAll();

if(carts.isEmpty()){

throw new ShoppingCartListNotFoundException();
}
return new ResponseEntity<List<ShoppingCart>>(carts, HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.GET, value = "/user/{userName}")
public ResponseEntity<List<ShoppingCart>> getShoppingCartsByUserName(@PathVariable String userName) throws ShoppingCartListNotFoundException {

List<ShoppingCart> carts = this.shoppingCartRepository.findByUserName(userName);

if(carts.isEmpty()){

throw new ShoppingCartListNotFoundException(userName);
}
return new ResponseEntity<List<ShoppingCart>>(carts, HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.GET, value = "/{cartId}")
ResponseEntity<?> readShoppingCart(@PathVariable String cartId) throws ShoppingCartNotFoundException{

ShoppingCart cart = this.shoppingCartRepository.findById(cartId).get();

if(cart == null){

throw new ShoppingCartNotFoundException(cartId);
}

return new ResponseEntity<ShoppingCart>(cart, HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.POST)
ResponseEntity<?> add(@RequestBody ShoppingCart input, UriComponentsBuilder ucBuilder){
ShoppingCart result = this.shoppingCartRepository.save(
new ShoppingCart(
ShoppingCart.PENDING,
input.userName,
input.products,
input.productQuantities,
input.orderDate,
input.lastModified,
input.totalPrice
));

HttpHeaders headers = new HttpHeaders();
headers.setLocation(ucBuilder.path("/carts/{id}").buildAndExpand(result.getId()).toUri());
return new ResponseEntity<ShoppingCart>(result, headers, HttpStatus.CREATED);
}

@RequestMapping(method = RequestMethod.POST, value = "/{cartId}/product/{productId}")
ResponseEntity<?> addProduct(@PathVariable String cartId, @PathVariable Integer productId){

ShoppingCart cart = this.shoppingCartRepository.findById(cartId).get();

if(cart == null){

throw new ShoppingCartNotFoundException(cartId);
}

Product product = this.productRepository.findById(productId).get();

if(product == null){

throw new ProductNotFoundException(productId);
}

cart.addProduct(product);
cart.addProductQuantity(product);
cart.lastModified = new Date();
//update product stock
if(!product.removeStock()){
return new ResponseEntity<ShoppingCart>(cart, HttpStatus.ACCEPTED);
}

ShoppingCart updated = this.shoppingCartRepository.save(cart);
this.productRepository.save(product);

return new ResponseEntity<ShoppingCart>(updated, HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.DELETE, value = "/{cartId}/product/{productId}")
ResponseEntity<?> removeProduct(@PathVariable String cartId, @PathVariable String productId){
logger.info("---Removing product'" + productId +"' from shopping cart '" + cartId +"'---");
ShoppingCart cart = this.shoppingCartRepository.findOne(cartId);

if(cart == null){
logger.error("---Unable to update shopping cart'" + cartId +"' not found---");
throw new ShoppingCartNotFoundException(cartId);
}

Product product = this.productRepository.findOne(productId);

if(product == null){
logger.error("---Unable to update product'" + productId +"' not found---");
throw new ProductNotFoundException(productId);
}

cart.removeProductQuantity(product);
cart.lastModified = new Date();

product.addStock();

ShoppingCart updated = this.shoppingCartRepository.save(cart);
this.productRepository.save(product);

return new ResponseEntity<ShoppingCart>(updated, HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.PUT, value = "/{cartId}")
ResponseEntity<?> update(@RequestBody ShoppingCart input, @PathVariable String cartId){
logger.info("---Updating shopping cart '" + cartId +"'---");
ShoppingCart cart = this.shoppingCartRepository.findOne(cartId);

if(cart == null){
logger.error("---Unable to update shopping cart'" + cartId +"' not found---");
throw new ShoppingCartNotFoundException(cartId);
}

ShoppingCart updated = this.shoppingCartRepository.save(input);
return new ResponseEntity<ShoppingCart>(updated, HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.POST, value = "/order/{cartId}")
ResponseEntity<?> order(@RequestBody ShoppingCart input, @PathVariable String cartId){
logger.info("---Placing order on shopping cart '" + cartId +"'---");
ShoppingCart cart = this.shoppingCartRepository.findOne(cartId);
if(cart == null){
logger.error("---Unable to place order on shopping cart'" + cartId +"' not found---");
throw new ShoppingCartNotFoundException(cartId);
}
//we only get the status and total price info from the input
cart.status = ShoppingCart.ORDERED;
cart.lastModified = new Date();
cart.orderDate = new Date();

ShoppingCart updated = this.shoppingCartRepository.save(cart);
return new ResponseEntity<ShoppingCart>(updated, HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.DELETE, value = "/{cartId}")
ResponseEntity<?> delete(@PathVariable String cartId){
logger.info("---Deleting shopping cart '" + cartId +"'---");
ShoppingCart cart = this.shoppingCartRepository.findOne(cartId);

if(cart == null){
logger.error("---Unable to delete shopping cart '" + cartId +"' not found---");
throw new ShoppingCartNotFoundException(cartId);
}

if(!cart.productQuantities.isEmpty()){
List<Product> productList = new ArrayList<Product>();
for(Map.Entry<String, Integer> entry: cart.productQuantities.entrySet()){
Product product = this.productRepository.findOne(entry.getKey());
if(product != null){
product.addStock(entry.getValue());
productList.add(product);
}
}
if(!productList.isEmpty()) this.productRepository.save(productList);
}

this.shoppingCartRepository.delete(cartId);
return new ResponseEntity<ShoppingCart>(HttpStatus.NO_CONTENT);

}


} 