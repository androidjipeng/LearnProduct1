package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean;

import java.util.List;

/**
 * Created by jipeng on 2017/3/23.
 */

public class CartInformationModel {

    /**
     * objList : [{"shenyurenshu":38,"thumb":"http://goodimage.91lelegou.com/shopimg/20161108/20161108011817_60720.png","num":2,"shopid":3926,"title":"(第132期) 百草味 3袋组合装\u2009|\u2009金枕头榴莲干 30g/袋 香浓榴莲 爱我所爱","yunjiage":1}]
     * result : 0
     * info : 查询购物车成功
     */

    private int result;
    private String info;
    private List<ObjListBean> objList;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<ObjListBean> getObjList() {
        return objList;
    }

    public void setObjList(List<ObjListBean> objList) {
        this.objList = objList;
    }

    public static class ObjListBean {
        /**
         * shenyurenshu : 38
         * thumb : http://goodimage.91lelegou.com/shopimg/20161108/20161108011817_60720.png
         * num : 2
         * shopid : 3926
         * title : (第132期) 百草味 3袋组合装 | 金枕头榴莲干 30g/袋 香浓榴莲 爱我所爱
         * yunjiage : 1.0
         */

        private int shenyurenshu;
        private String thumb;
        private int num;
        private int shopid;
        private String title;
        private double yunjiage;

        private boolean ischeck;

        public boolean getIscheck() {
            return ischeck;
        }

        public void setIscheck(boolean ischeck) {
            this.ischeck = ischeck;
        }

        public int getShenyurenshu() {
            return shenyurenshu;
        }

        public void setShenyurenshu(int shenyurenshu) {
            this.shenyurenshu = shenyurenshu;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getShopid() {
            return shopid;
        }

        public void setShopid(int shopid) {
            this.shopid = shopid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getYunjiage() {
            return yunjiage;
        }

        public void setYunjiage(double yunjiage) {
            this.yunjiage = yunjiage;
        }
    }
}
