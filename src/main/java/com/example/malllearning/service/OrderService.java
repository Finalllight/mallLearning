package com.example.malllearning.service;


import com.example.malllearning.common.ResultCode;
import com.example.malllearning.entity.*;
import com.example.malllearning.enums.OrderStatus;
import com.example.malllearning.exception.BusinessException;
import com.example.malllearning.repository.*;
import com.example.malllearning.vo.OrderItemVO;
import com.example.malllearning.vo.OrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CouponService couponService;
    private final UserCouponRepository userCouponRepository;

    // 构造器注入（Spring 会自动调用此构造器）
    public OrderService(CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CouponService couponService,
                        UserCouponRepository userCouponRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.couponService = couponService;
        this.userCouponRepository = userCouponRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderVO submitOrder(Long userId, Long userCouponId) {
        Cart cart = cartRepository.findFirstByUserIdOrderByIdAsc(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "购物车为空"));

        List<CartItem> cartItems = cartItemRepository.findByCart_Id(cart.getId());
        if (cartItems.isEmpty()) {
            throw new BusinessException(ResultCode.NOT_FOUND, "购物车为空");
        }

        // 1) 校验库存 + 计算总价（使用最新商品数据）
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<Long, Product> latestProductMap = new HashMap<>();

        for (CartItem cartItem : cartItems) {
            Long pid = cartItem.getProduct().getId();
            Product latestProduct = productRepository.findById(pid)
            .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "商品不存在，id=" + pid));

            if (cartItem.getQuantity() > latestProduct.getStock()) {
                throw new BusinessException(ResultCode.BAD_REQUEST, latestProduct.getName() + "，库存=" + latestProduct.getStock());
            }

            latestProductMap.put(pid, latestProduct);
            totalAmount = totalAmount.add(
                    latestProduct.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }
        totalAmount = totalAmount.setScale(2, RoundingMode.HALF_UP);

        // 2) 计算优惠
        BigDecimal discount = BigDecimal.ZERO;
        Long couponId = null;

        if (userCouponId != null) {
            discount = couponService.validateAndCalculateDiscount(userId, userCouponId, totalAmount);

            UserCoupon uc = userCouponRepository.findByIdAndUserId(userCouponId, userId)
                    .orElseThrow(() -> new BusinessException(ResultCode.BAD_REQUEST,"用户优惠券不存在"));
            couponId = uc.getCoupon().getId();
        }
        discount = discount.setScale(2, RoundingMode.HALF_UP);

        BigDecimal finalAmount = totalAmount.subtract(discount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }
        finalAmount = finalAmount.setScale(2, RoundingMode.HALF_UP);

        // 3) 校验余额
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.BAD_REQUEST,"用户不存在"));
        if (user.getBalance().compareTo(finalAmount) < 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST,"余额不足，当前余额：" + user.getBalance());
        }

        // 4) 扣库存
        for (CartItem cartItem : cartItems) {
            Product p = latestProductMap.get(cartItem.getProduct().getId());
            p.setStock(p.getStock() - cartItem.getQuantity());
            productRepository.save(p);
        }

        // 5) 扣余额
        user.setBalance(user.getBalance().subtract(finalAmount));
        userRepository.save(user);

        // 6) 创建订单主表
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setDiscount(discount);
        order.setFinalAmount(finalAmount);
        order.setStatus(OrderStatus.PAID);
        order.setCouponId(couponId);
        order = orderRepository.save(order);

        // 7) 创建订单明细
        for (CartItem cartItem : cartItems) {
            Product p = latestProductMap.get(cartItem.getProduct().getId());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(p);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(p.getPrice());
            orderItemRepository.save(orderItem);
        }

        // 8) 标记优惠券已使用
        if (userCouponId != null) {
            couponService.markCouponUsed(userId, userCouponId);
        }

        // 9) 清空购物车
        cartItemRepository.deleteByCart_Id(cart.getId());

        OrderVO vo = toOrderDetailVO(order);
        vo.setBalanceAfterPay(user.getBalance());
        return vo;
    }

    @Transactional(readOnly = true)
    public List<OrderVO> listOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByIdDesc(userId);
        List<OrderVO> result = new ArrayList<>();
        for (Order order : orders) {
            OrderVO vo = new OrderVO();
            vo.setOrderId(order.getId());
            vo.setTotalAmount(order.getTotalAmount());
            vo.setDiscount(order.getDiscount());
            vo.setFinalAmount(order.getFinalAmount());
            vo.setStatus(order.getStatus().name());
            vo.setCouponId(order.getCouponId());
            vo.setCreatedAt(order.getCreatedAt());
            result.add(vo);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public OrderVO getOrderDetail(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new BusinessException(ResultCode.BAD_REQUEST,"订单不存在"));
        return toOrderDetailVO(order);
    }

    private OrderVO toOrderDetailVO(Order order) {
        OrderVO vo = new OrderVO();
        vo.setOrderId(order.getId());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setDiscount(order.getDiscount());
        vo.setFinalAmount(order.getFinalAmount());
        vo.setStatus(order.getStatus().name());
        vo.setCouponId(order.getCouponId());
        vo.setCreatedAt(order.getCreatedAt());

        List<OrderItem> items = orderItemRepository.findByOrder_Id(order.getId());
        List<OrderItemVO> itemVOList = new ArrayList<>();

        for (OrderItem item : items) {
            OrderItemVO itemVO = new OrderItemVO();
            itemVO.setProductId(item.getProduct().getId());
            itemVO.setProductName(item.getProduct().getName());
            itemVO.setPrice(item.getPrice());
            itemVO.setQuantity(item.getQuantity());
            itemVO.setSubTotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            itemVOList.add(itemVO);
        }

        vo.setItems(itemVOList);
        return vo;
    }


}
