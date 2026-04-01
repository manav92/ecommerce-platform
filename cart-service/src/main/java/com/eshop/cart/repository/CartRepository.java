package com.eshop.cart.repository;

import com.eshop.cart.domain.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, String> {
}
