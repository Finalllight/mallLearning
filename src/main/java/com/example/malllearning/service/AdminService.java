package com.example.malllearning.service;

import com.example.malllearning.common.ResultCode;
import com.example.malllearning.dto.admin.AdminCouponRequest;
import com.example.malllearning.dto.admin.AdminProductRequest;
import com.example.malllearning.dto.admin.AdminUserResponse;
import com.example.malllearning.entity.Coupon;
import com.example.malllearning.entity.Product;
import com.example.malllearning.entity.User;
import com.example.malllearning.enums.CouponType;
import com.example.malllearning.exception.BusinessException;
import com.example.malllearning.repository.CouponRepository;
import com.example.malllearning.repository.OrderItemRepository;
import com.example.malllearning.repository.ProductRepository;
import com.example.malllearning.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final OrderItemRepository orderItemRepository;

    public AdminService(ProductRepository productRepository,
                        UserRepository userRepository,
                        CouponRepository couponRepository,
                        OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // ======================== 商品管理 ========================

    @Transactional
    public Product createProduct(AdminProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, AdminProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "商品不存在"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException(ResultCode.NOT_FOUND, "商品不存在");
        }

        // ✅ 检查是否有订单引用
        if (orderItemRepository.existsByProduct_Id(id)) {
            throw new BusinessException(ResultCode.BIZ_ERROR, "该商品已有订单记录，无法删除，建议下架");
        }

        productRepository.deleteById(id);
    }

    // ======================== 用户管理 ========================

    @Transactional(readOnly = true)
    public List<AdminUserResponse> listAllUsers() {
        List<User> users = userRepository.findAll();
        List<AdminUserResponse> result = new ArrayList<>();
        for (User user : users) {
            result.add(toUserResponse(user));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public AdminUserResponse getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "用户不存在"));
        return toUserResponse(user);
    }

    @Transactional
    public AdminUserResponse updateUserBalance(Long userId, BigDecimal newBalance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "用户不存在"));

        user.setBalance(newBalance);
        userRepository.save(user);
        return toUserResponse(user);
    }

    // ======================== 优惠券管理 ========================

    @Transactional
    public Coupon createCoupon(AdminCouponRequest request) {
        // 校验类型是否合法
        CouponType type;
        try {
            type = CouponType.valueOf(request.getType());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ResultCode.BAD_REQUEST,
                    "无效的优惠券类型，可选值：FULL_REDUCTION, DISCOUNT");
        }

        Coupon coupon = new Coupon();
        coupon.setName(request.getName());
        coupon.setType(type);
        coupon.setAmount(request.getAmount());
        coupon.setMinOrder(request.getMinOrder());
        coupon.setQuantity(request.getQuantity());
        return couponRepository.save(coupon);
    }

    @Transactional
    public Coupon updateCoupon(Long id, AdminCouponRequest request) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "优惠券不存在"));

        CouponType type;
        try {
            type = CouponType.valueOf(request.getType());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ResultCode.BAD_REQUEST,
                    "无效的优惠券类型，可选值：FULL_REDUCTION, DISCOUNT");
        }

        coupon.setName(request.getName());
        coupon.setType(type);
        coupon.setAmount(request.getAmount());
        coupon.setMinOrder(request.getMinOrder());
        coupon.setQuantity(request.getQuantity());
        return couponRepository.save(coupon);
    }

    @Transactional
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new BusinessException(ResultCode.NOT_FOUND, "优惠券不存在");
        }

//        if (orderItemRepository.existsByCoupon_Id(id)) {
//            throw new BusinessException(ResultCode.BIZ_ERROR, "该优惠券已有订单记录，无法删除");
//        }

        couponRepository.deleteById(id);
    }

    // ======================== 私有方法 ========================

    private AdminUserResponse toUserResponse(User user) {
        AdminUserResponse resp = new AdminUserResponse();
        resp.setId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setEmail(user.getEmail());
        resp.setBalance(user.getBalance());
        resp.setRole(user.getRole().name());
        resp.setCreatedAt(user.getCreatedAt());
        return resp;
    }
}
