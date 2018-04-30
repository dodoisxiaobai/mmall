package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author dodo
 * @date 2018/4/28
 * @description
 */
public class CartVo {
    private List<CartProductVo> cartProductList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;
    private String imageHost;

    public List<CartProductVo> getCartProductList() {
        return cartProductList;
    }

    public void setCartProductList(List<CartProductVo> cartProductList) {
        this.cartProductList = cartProductList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
