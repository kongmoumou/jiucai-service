package com.jiucai.mall.common;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Set;

@Getter
public class Constant {

    /**
     * session
     */
    public static final String SESSION_LOGIN_USER = "loginUser";

    /**
     * JWT setting
     */
    public interface jwtSetting {
        //JWT Key
        String JWT_KEY = "gajiucai";
        //token expire time
        int minutes = 30;
    }

    /**
     * 用户相关的信息
     */
    @Getter
    public enum userMsg {

        CHECK("用户名已存在！", "用户名不存在！"),
        LOGIN("用户名或密码错误！", "登录成功！"),
        REGISTER("注册失败，请稍后重试！", "注册成功！"),
        GetUserInfo("用户未登录，请登录后再进行相关操作！", "获取用户信息成功！"),
        UpdateUserInfo("更新信息为空，请输入需要更新的信息！", "更新成功！"),
        ResetPassword("您输入的旧密码不正确！", "修改密码成功！"),
        ForgetPassword("无法找回密码，您未设置找回密码的答案！", "用户名存在！"),
        CheckAnswer("答案错误！", "答案正确！"),
        ResetPasswordByToken("Token已经失效，请重新找回密码！", "重设密码成功！"),
        LOGOUT("退出系统异常，请稍后再试！","退出系统成功！"),

        GetCategory("未找到该商品的类别！","查询商品类别成功！"),
        GetChildrenCategory("查询的父类商品ID不存在！","查询商品类别成功！"),
        GetAllChildrenCategoryId("查询失败，请稍后再试！","查询商品类别ID成功！"),
        AddCategory("新增商品类别失败，请稍后再试！","新增商品类别成功！"),
        UpdateCategory("更新商品类别失败，请稍后再试！","更新商品类别成功！"),

        GetProductDetail("未找到该商品的相关信息！","找到相关商品的信息！"),
        GetProductList("未找到该商品的相关信息！","找到相关商品的信息！"),
        AddProduct("新增商品失败！","新增商品成功~"),
        ;

        private String FAIL;
        private String SUCCESS;

        userMsg(String FAIL, String SUCCESS) {
            this.FAIL = FAIL;
            this.SUCCESS = SUCCESS;
        }

    }

    /**
     * 用户角色状态
     */
    public interface roleStatus {

        int CUSTOMER = 1;
        int ADMIN = 0;

    }

    /**
     * 商品分类的使用状态
     */
    public interface categoryStatus {

        int activity = 1;
        int inactivity = 2;

    }

    /**
     * 订单状态
     */
    public interface orderStatus{
        int CANCELED = 0;
        int UNPAID = 10;
        int PAID = 20;
        int SHIPPED = 30;
        int SUCCESSFULDEAL = 40;
        int CANCELEDDEAL = 50;
    }

    public interface productListOrderBy {
        Set<String> ORDER_METHOD = Sets.newHashSet("price_desc", "price_asc","stock_desc","stock_asc");
    }


    /**
     * User Routing
     */
    public interface userRouting {

        String USER = "/users/";
        String LOGIN = "login";
        String CHECK = "check";
        String REGISTER = "register";
        String GET_USER_INFO = "get_user_info";
        String UPDATE_USER_INFO = "update_user_info";
        String RESET_PASSWORD = "reset_password";
        String FORGET_PASSWORD = "forget_password";
        String RESET_PASSWORD_BY_TOKEN = "reset_password_by_token";
        String LOGOUT = "logout";

    }

    /**
     * Category Routing
     */
    public interface categoryRouting {

        String CATEGORY = "/category/";
        String GET_CATEGORY = "get_category";
        String GET_CHILDREN_CATEGORY = "get_children_category";
        String GET_DEEP_CATEGORY = "get_deep_category";
        String ADD_CATEGORY = "add_category";
        String UPDATE_CATEGORY = "update_category";
        String GET_Category_Tree="get_category_tree";

    }

    /**
     * Cart Routing
     */
    public interface cartRouting {

        String CART = "/cart/";
        String LIST_CART = "list_cart";
        String ADD_CART = "add_cart";
        String UPDATE = "update_cart";
        String DELETE_PRODUCT = "delete_product";
        String SELECT = "select";
        String UNSELECT ="unselect";
        String GET_CART_PRODUCT_COUNT="get_cart_product_count";
        String SELECT_ALL="select_all";
        String UNSELECT_ALL="unselect_all";

    }

    /**
     * Order Routing
     */
    public interface orderRouting {

        String ORDER = "/order/";
        String CREATE_ORDER = "create_order";
        String CONFIRM_ORDER = "confirm_order";
        String CANCEL_ORDER = "cancel_order";
        String GET_ORDER_DETAIL = "get_order_detail";
        String GET_ORDER_LIST = "get_order_list";

    }

    /**
     * Product Routing
     */
    public interface productRouting {

        String PRODUCT = "/product/";
        String GET_PRODUCT = "get_product";
        String GET_PRODUCT_LIST = "list";
        String ADD_PRODUCT = "add_product";

    }

    /**
     * 购物车
     */
    public interface Cart {

        Integer CHECKED = 1; //购物车选中
        Integer UN_CHECKED = 0; //购物车未选中

        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
        String LIMIT_NUM_FAILURE = "LIMIT_NUM_FAILURE";
    }

}