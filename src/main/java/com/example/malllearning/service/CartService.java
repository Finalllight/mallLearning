package com.example.malllearning.service;

import com.example.malllearning.common.ResultCode;
import com.example.malllearning.entity.Cart;
import com.example.malllearning.entity.CartItem;
import com.example.malllearning.entity.Product;
import com.example.malllearning.exception.BusinessException;
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
    public CartVO addItem(Long userId, Long productId, Integer quantity)  {
        validateQuantity(quantity);

        Product product = getProductOrThrow(productId);
        Cart cart = getOrCreateCart(userId);

        CartItem cartItem = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId)
                .orElseGet(() -> {
                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setProduct(product);
                    item.setQuantity(0);
                    return item;
                });

        int newQuantity = cartItem.getQuantity() + quantity;
        validateStock(product, newQuantity);

        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);

        return buildCartVO(cart);
    }

    @Transactional
    public CartVO updateItemQuantity(Long userId, Long productId, Integer quantity) {
        validateQuantity(quantity);

        Cart cart = getCartOrThrow(userId);
        CartItem cartItem = getCartItemOrThrow(cart.getId(), productId);

        Product product = cartItem.getProduct();
        validateStock(product, quantity);

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return buildCartVO(cart);
    }



    @Transactional
    public CartVO removeItem(Long userId, Long productId) {
        Cart cart = getCartOrThrow(userId);
        CartItem cartItem = getCartItemOrThrow(cart.getId(), productId);

        cartItemRepository.delete(cartItem);

        return buildCartVO(cart);
    }

    @Transactional(readOnly = true)
    public CartVO getCart(Long userId) {
        return cartRepository.findFirstByUserIdOrderByIdAsc(userId)
                .map(this::buildCartVO)
                .orElseGet(this::emptyCartVO);
    }

    private Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "商品不存在"));
    }

    private Cart getCartOrThrow(Long userId) {
        return cartRepository.findFirstByUserIdOrderByIdAsc(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "购物车不存在"));
    }

    private CartItem getCartItemOrThrow(Long cartId, Long productId) {
        return cartItemRepository.findByCart_IdAndProduct_Id(cartId, productId)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "购物车中没有该商品"));
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findFirstByUserIdOrderByIdAsc(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    return cartRepository.save(cart);
                });
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "商品数量必须大于0");
        }
    }

    private void validateStock(Product product, Integer quantity) {
        if (quantity > product.getStock()) {
            throw new BusinessException(ResultCode.BIZ_ERROR,
                    "库存不足，当前库存：" + product.getStock());
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