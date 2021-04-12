package com.jiucai.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jiucai.mall.common.Constant;
import com.jiucai.mall.common.ResponseStatusCode;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.dao.ProductMapper;
import com.jiucai.mall.entity.ProductEntity;
import com.jiucai.mall.entity.UserEntity;
import com.jiucai.mall.utils.BigDecimalUtil;
import com.jiucai.mall.vo.CartProductVo;
import com.jiucai.mall.vo.CartVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.jiucai.mall.dao.CartMapper;
import com.jiucai.mall.entity.CartEntity;
import com.jiucai.mall.service.CartService;

import javax.servlet.http.HttpSession;

@Service("cartService")
public class CartServiceImpl extends ServiceImpl<CartMapper, CartEntity> implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    HttpSession session;

    /**
     * 获取购物车所有商品
     *
     * @return
     */
    @Override
    public UniformResponse<CartVo> listCart() {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(
                    ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }

        CartVo cartVo = getCartVoLimit(user.getId());
        if (cartVo.getCartProductVoList() != null) {
            return UniformResponse.ResponseForSuccess(cartVo);
        } else return UniformResponse.ResponseForFail("当前的购物车为空！");

    }

    /**
     * 加入购物车
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public UniformResponse<CartVo> addCart(Integer userId, Integer productId, Integer count) {
        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("product_id",productId);
        //查看购物车里是否已经有这个商品
        CartEntity cartEntity = cartMapper.selectOne(queryWrapper);
        /* count大于0 */
        if (count > 0) {
            if(cartEntity == null){
                //如果没有就新增一个
                CartEntity newCart = new CartEntity();
                newCart.setQuantity(count);
                newCart.setChecked(Constant.Cart.CHECKED);
                newCart.setProductId(productId);
                newCart.setUserId(userId);
                cartMapper.insert(newCart);
            }else{
                //有的话就加数量
                count = cartEntity.getQuantity() + count;
                cartEntity.setQuantity(count);
                cartMapper.updateById(cartEntity);
            }

        }else{
            /* 小于0的情况，不为空且相减大于0，否则删除该购物车的商品 */
            if (cartEntity != null && (cartEntity.getQuantity()+count)>0) {
                count = cartEntity.getQuantity() + count;
                cartEntity.setQuantity(count);
                cartMapper.updateById(cartEntity);
            }else {
                /*删除商品*/
                QueryWrapper<CartEntity> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("product_id",productId).eq("user_id",userId);
                cartMapper.delete(queryWrapper1);
            }
        }
        CartVo cartVo = getCartVoLimit(userId);
        if (cartVo.getCartProductVoList() != null) {
            return UniformResponse.ResponseForSuccess(cartVo);
        } else return UniformResponse.ResponseForFail("当前的购物车为空！");
    }

    /**
     * 更新购物车
     *
     * @param productId
     * @param count
     * @return
     */
    @Override
    /*获取更新后购物车商品的数量,去除count参数*/
    public UniformResponse<CartVo> update(Integer productId, Integer count) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(
                    ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }
        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        queryWrapper.eq("product_id",productId);
        CartEntity cartEntity = cartMapper.selectOne(queryWrapper);

        /* 更新购物车商品数量 */
        /* 先判断count是否合法，不合法则不插入 */
        if (count > 0) {
            cartEntity.setQuantity(count);
            cartMapper.updateById(cartEntity);
        }
        CartVo cartVo = getCartVoLimit(user.getId());
        return UniformResponse.ResponseForSuccess(cartVo);

    }

    /**
     * 删除购物车商品
     *
     * @param productIds
     * @return
     */
    @Override
    public UniformResponse<CartVo> deleteProduct(List<Integer> productIds) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(
                    ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }
        //删除
        productIds.forEach(integer -> {
            /*这种用法有问题，integer是product_id,不是seriesID*/
//            cartMapper.deleteById(integer);
            QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("product_id",integer).eq("user_id",user.getId());
            cartMapper.delete(queryWrapper);

        });
        CartVo cartVo = getCartVoLimit(user.getId());
        if (cartVo.getCartProductVoList() != null) {
            return UniformResponse.ResponseForSuccess(cartVo);
        } else return UniformResponse.ResponseForFail("当前的购物车为空！");

    }

    /**
     * 选中购物车某个商品
     *
     * @param productId
     * @return
     */
    @Override
    public UniformResponse<CartVo> select(Integer productId) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(
                    ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }

        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        queryWrapper.eq("product_id",productId);
        CartEntity cartEntity = cartMapper.selectOne(queryWrapper);
        if (cartEntity != null) {
            cartEntity.setChecked(Constant.Cart.CHECKED);
            cartMapper.updateById(cartEntity);
            CartVo cartVo = getCartVoLimit(user.getId());

            return UniformResponse.ResponseForSuccess(cartVo);
        } else return UniformResponse.ResponseForFail("购物车中不存在该商品！");

    }

    /**
     * 取消选中购物车某个商品
     *
     * @param productId
     * @return
     */
    @Override
    public UniformResponse<CartVo> unSelect(Integer productId) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(
                    ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }

        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        queryWrapper.eq("product_id",productId);
        CartEntity cartEntity = cartMapper.selectOne(queryWrapper);
        cartEntity.setChecked(Constant.Cart.UN_CHECKED);
        cartMapper.updateById(cartEntity);
        CartVo cartVo = getCartVoLimit(user.getId());

        if (cartVo.getCartProductVoList() != null) {
            return UniformResponse.ResponseForSuccess(cartVo);
        } else return UniformResponse.ResponseForFail("当前的购物车为空！");
    }

    /**
     * 获取商品数量
     *
     * @return
     */
    @Override
    public UniformResponse<Integer> getCartProductCount() {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(
                    ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }

        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        AtomicReference<Integer> count = new AtomicReference<>(0);
        List<CartEntity> cartEntityList = cartMapper.selectList(queryWrapper);
        /*fix BUG，不是统计多少个商品类在购物车中*/
        cartEntityList.forEach(item ->{
            count.set(count.get() + item.getQuantity());
        });
        return UniformResponse.ResponseForSuccess(count.get());
    }

    /**
     * 选中购物车全部商品
     *
     * @return
     */
    @Override
    public UniformResponse<CartVo> selectAll() {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(
                    ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }
        //获取当前用户的购物车
        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        List<CartEntity> cartList = cartMapper.selectList(queryWrapper);
        //全选
        cartList.forEach(cartEntity -> {
            cartEntity.setChecked(Constant.Cart.CHECKED);
            cartMapper.updateById(cartEntity);
        });

        CartVo cartVo = getCartVoLimit(user.getId());

        if (cartVo.getCartProductVoList() != null) {
            return UniformResponse.ResponseForSuccess(cartVo);
        } else return UniformResponse.ResponseForFail("当前的购物车为空！");
    }

    /**
     * 取消选中购物车全部商品
     *
     * @return
     */
    @Override
    public UniformResponse<CartVo> unSelectAll() {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            //没登录
            return UniformResponse.ResponseErrorCodeMessage(
                    ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }
        //获取当前用户的购物车
        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        List<CartEntity> cartList = cartMapper.selectList(queryWrapper);
        //全选
        cartList.forEach(cartEntity -> {
            cartEntity.setChecked(Constant.Cart.UN_CHECKED);
            cartMapper.updateById(cartEntity);
        });

        CartVo cartVo = getCartVoLimit(user.getId());

        if (cartVo.getCartProductVoList() != null) {
            return UniformResponse.ResponseForSuccess(cartVo);
        } else return UniformResponse.ResponseForFail("当前的购物车为空！");
    }


    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        //获取用户购物车
        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<CartEntity> cartList = cartMapper.selectList(queryWrapper);

        BigDecimal cartTotalPrice = new BigDecimal("0");

        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        if(CollectionUtils.isNotEmpty(cartList)){
            for(CartEntity cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());

                ProductEntity product = productMapper.selectById(cartItem.getProductId());

                if(product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());

                    int buyLimitCount = 0;

                    if(product.getStock() >= cartItem.getQuantity()){
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Constant.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Constant.Cart.LIMIT_NUM_FAILURE);

                        CartEntity cartForQuantity = new CartEntity();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        int rows = cartMapper.updateById(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if(cartItem.getChecked() == Constant.Cart.CHECKED){
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
            cartVo.setCartTotalPrice(cartTotalPrice);
            cartVo.setCartProductVoList(cartProductVoList);
            cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        }
        return cartVo;


    }

    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(("user_id"),userId);
        queryWrapper.eq("checked",0);
        int result = cartMapper.selectCount(queryWrapper);
        return result == 0;
    }
}
