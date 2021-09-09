package com.myapp.spring.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.myapp.spring.api.ShoppingCartListNotFoundException;
import com.myapp.spring.api.ShoppingCartNotFoundException;
import com.myapp.spring.api.ProductNotFoundException;

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
ResponseEntity<?> addProduct(@PathVariable String cartId, @PathVariable Integer productId) throws ShoppingCartNotFoundException, ProductNotFoundException{

ShoppingCart cart = this.shoppingCartRepository.findById(cartId).get();

if(cart == null){

throw new ShoppingCartNotFoundException(cartId);
}

Product product = this.productRepository.findById(productId).get();

if(product == null){

throw new ProductNotFoundException(productId.toString());
}

cart.addProduct(product);
cart.addProductQuantity(product);
cart.lastModified = new Date();


ShoppingCart updated = this.shoppingCartRepository.save(cart);
this.productRepository.save(product);

return new ResponseEntity<ShoppingCart>(updated, HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.DELETE, value = "/{cartId}/product/{productId}")
public ResponseEntity<?> removeProduct(@PathVariable String cartId, @PathVariable Integer productId) throws ShoppingCartListNotFoundException, ProductNotFoundException{

ShoppingCart cart = this.shoppingCartRepository.findById(cartId).get();

if(cart == null){

throw new ShoppingCartListNotFoundException(cartId);
}

Product product = this.productRepository.findById(productId).get();

if(product == null){

throw new ProductNotFoundException(productId.toString());
}

cart.removeProductQuantity(product);
cart.lastModified = new Date();



ShoppingCart updated = this.shoppingCartRepository.save(cart);
this.productRepository.save(product);

return new ResponseEntity<ShoppingCart>(updated, HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.PUT, value = "/{cartId}")
ResponseEntity<?> update(@RequestBody ShoppingCart input, @PathVariable String cartId) throws ShoppingCartNotFoundException{

ShoppingCart cart = this.shoppingCartRepository.findById(cartId).get();

if(cart == null){

throw new ShoppingCartNotFoundException(cartId);
}

ShoppingCart updated = this.shoppingCartRepository.save(input);
return new ResponseEntity<ShoppingCart>(updated, HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.POST, value = "/order/{cartId}")
ResponseEntity<?> order(@RequestBody ShoppingCart input, @PathVariable String cartId) throws ShoppingCartNotFoundException{

ShoppingCart cart = this.shoppingCartRepository.findById(cartId).get();
if(cart == null){

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
ResponseEntity<?> delete(@PathVariable String cartId) throws ShoppingCartNotFoundException{

ShoppingCart cart = this.shoppingCartRepository.findById(cartId).get();

if(cart == null){

throw new ShoppingCartNotFoundException(cartId);
}

if(!cart.productQuantities.isEmpty()){
List<Product> productList = new ArrayList<Product>();
for(Entry<Integer, Integer> entry: cart.productQuantities.entrySet()){
Product product = this.productRepository.findById(entry.getKey()).get();
if(product != null){

productList.add(product);
}
}
if(!productList.isEmpty()) this.productRepository.saveAll(productList);
}

this.shoppingCartRepository.deleteById(cartId);
return new ResponseEntity<ShoppingCart>(HttpStatus.NO_CONTENT);

}


} 