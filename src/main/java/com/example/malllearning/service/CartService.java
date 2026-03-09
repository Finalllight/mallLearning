package com.example.malllearning.service;

import com.example.malllearning.entity.Cart;
import com.example.malllearning.entity.CartItem;
import com.example.malllearning.entity.Product;
import com.example.malllearning.repository.CartItemRepository;
import com.example.malllearning.repository.CartRepository;
import com.example.malllearning.repository.ProductRepository;
import com.example.malllearning.vo.CartItemVO;
import com.example.malllearning.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }
    @Transactional
    public CartVO addItem(Long userId, Long productId, Integer quantity) throws Exception {
        validateQuantity(quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("商品不存在"));

        Cart cart = getOrCreateCart(userId);

        Optional<CartItem> existingItemOpt =
                cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId);

        int newQuantity = quantity;
        CartItem cartItem;

        if (existingItemOpt.isPresent()) {
            cartItem = existingItemOpt.get();
            newQuantity = cartItem.getQuantity() + quantity;
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
        }

        if (newQuantity > product.getStock()) {
            throw new Exception("库存不足，当前库存：" + product.getStock());
        }

        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);

        return buildCartVO(cart);
    }

    @Transactional
    public CartVO updateItemQuantity(Long userId, Long productId, Integer quantity) throws Exception {
        validateQuantity(quantity);

        Cart cart = cartRepository.findFirstByUserIdOrderByIdAsc(userId)
                .orElseThrow(() -> new Exception("购物车不存在"));

        CartItem cartItem = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId)
                .orElseThrow(() -> new Exception("购物车中没有该商品"));

        Product product = cartItem.getProduct();
        if (quantity > product.getStock()) {
            throw new Exception("库存不足，当前库存：" + product.getStock());
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return buildCartVO(cart);
    }

    @Transactional
    public CartVO removeItem(Long userId, Long productId) throws Exception {
        Cart cart = cartRepository.findFirstByUserIdOrderByIdAsc(userId)
                .orElseThrow(() -> new Exception("购物车不存在"));

        CartItem cartItem = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId)
                .orElseThrow(() -> new Exception("购物车中没有该商品"));

        cartItemRepository.delete(cartItem);

        return buildCartVO(cart);
    }

    @Transactional(readOnly = true)
    public CartVO getCart(Long userId) {
        Optional<Cart> cartOpt = cartRepository.findFirstByUserIdOrderByIdAsc(userId);
        if (cartOpt.isEmpty()) {
            return emptyCartVO();
        }
        return buildCartVO(cartOpt.get());
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findFirstByUserIdOrderByIdAsc(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    return cartRepository.save(cart);
                });
    }

    private void validateQuantity(Integer quantity) throws Exception {
        if (quantity == null || quantity <= 0) {
            throw new Exception("商品数量必须大于0");
        }
    }

    private CartVO emptyCartVO() {
        CartVO vo = new CartVO();
        vo.setCartId(null);
        vo.setItems(new ArrayList<>());
        vo.setTotalAmount(BigDecimal.ZERO);
        return vo;
    }

    private CartVO buildCartVO(Cart cart) {
        List<CartItem> cartItems = cartItemRepository.findByCart_Id(cart.getId());

        List<CartItemVO> itemVOList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            Product product = item.getProduct();

            BigDecimal subTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(subTotal);

            CartItemVO itemVO = new CartItemVO();
            itemVO.setProductId(product.getId());
            itemVO.setProductName(product.getName());
            itemVO.setPrice(product.getPrice());
            itemVO.setQuantity(item.getQuantity());
            itemVO.setSubTotal(subTotal);

            itemVOList.add(itemVO);
        }

        CartVO cartVO = new CartVO();
        cartVO.setCartId(cart.getId());
        cartVO.setItems(itemVOList);
        cartVO.setTotalAmount(totalAmount);

        return cartVO;
    }
}