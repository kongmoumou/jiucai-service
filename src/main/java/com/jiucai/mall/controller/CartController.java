package com.jiucai.mall.controller;

import com.jiucai.mall.common.Constant;
import com.jiucai.mall.common.ResponseStatusCode;
import com.jiucai.mall.common.UniformResponse;
import com.jiucai.mall.entity.UserEntity;
import com.jiucai.mall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jiucai.mall.service.CartService;

import java.util.List;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(Constant.cartRouting.CART)
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 获取购物车所有商品
     *
     * @return
     */
    @GetMapping(Constant.cartRouting.LIST_CART)
    @ResponseBody
    public Object listCart(){
        UniformResponse<CartVo> response= cartService.listCart();
        return  response;
    }

    /**
     * 加入购物车
     *
     * @param session
     * @param productId
     * @param count
     * @return
     */
    @PostMapping(Constant.cartRouting.ADD_CART)
    @ResponseBody
    public Object addCart(HttpSession session, Integer productId, Integer count){
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_LOGIN_USER);
        if(user == null){
            return UniformResponse.ResponseErrorCodeMessage(
                    ResponseStatusCode.NEED_LOGIN.getCode(),ResponseStatusCode.NEED_LOGIN.getDescription());
        }
        UniformResponse<CartVo> response = cartService.addCart(user.getId(),productId,count);
        return response;
    }

    /**
     * 更新购物车
     *
     * @param productId
     * @param count
     * @return
     */
    @PostMapping(Constant.cartRouting.UPDATE)
    @ResponseBody
    public Object update(Integer productId, Integer count){
        UniformResponse<CartVo> response = cartService.update(productId,count);
        return response;
    }

    /**
     * 从购物车删除商品
     *
     * @param productIds
     * @return
     */
    @PostMapping(Constant.cartRouting.DELETE_PRODUCT)
    @ResponseBody
    /*需要附上RequestParam注解*/
    public Object deleteProduct(@RequestParam(required = true) List<Integer> productIds){
        UniformResponse<CartVo> response = cartService.deleteProduct(productIds);
        return response;

    }

    /**
     * 选中购物车某个商品
     *
     * @param productId
     * @return
     */
    @PostMapping(Constant.cartRouting.SELECT)
    @ResponseBody
    public Object select(@RequestParam(required = true) Integer productId){
        UniformResponse<CartVo> response = cartService.select(productId);
        return response;
    }

    /**
     * 取消选中购物车某个商品
     *
     * @param productId
     * @return
     */
    @PostMapping(Constant.cartRouting.UNSELECT)
    @ResponseBody
    public Object unSelect(Integer productId){
        UniformResponse<CartVo> response = cartService.unSelect(productId);
        return response;
    }

    /**
     * 获取商品数量
     *
     * @return
     */
    @GetMapping(Constant.cartRouting.GET_CART_PRODUCT_COUNT)
    @ResponseBody
    public Object getCartProductCount(){
        UniformResponse<Integer> response = cartService.getCartProductCount();
        return response;
    }

    /**
     * 选中购物车全部商品
     *
     * @return
     */
    @GetMapping(Constant.cartRouting.SELECT_ALL)
    @ResponseBody
    public Object selectAll(){
        UniformResponse<CartVo> response = cartService.selectAll();
        return response;
    }

    /**
     * 取消选中购物车全部商品
     *
     * @return
     */
    @GetMapping(Constant.cartRouting.UNSELECT_ALL)
    @ResponseBody
    public Object unSelectAll(){
        UniformResponse<CartVo> response = cartService.unSelectAll();
        return response;
    }
}
