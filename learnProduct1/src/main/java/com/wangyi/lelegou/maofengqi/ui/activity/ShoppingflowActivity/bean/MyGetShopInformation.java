package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean;

import java.util.List;

/**
 * Created by jipeng on 2017/3/31.
 */

public class MyGetShopInformation {

    /**
     * objList : [{"circleId":299,"endTime":"2017-03-30 17:20:34","isCircle":true,"isPayCircle":false,"isVirtual":false,"shopid":3878,"shopname":"(圈子商品)vivo X9 全网通 4GB+64GB 移动联通电信4G手机 双卡双待","thumb":"http://goodimage.91lelegou.com/shopimg/20170119/81457600838595.jpg","userCode":"10000001"}]
     * totalCount : 1
     * result : 0
     * info : 获得商品成功
     */

    private int totalCount;
    private int result;
    private String info;
    private List<ObjListBean> objList;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

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
         * circleId : 299
         * endTime : 2017-03-30 17:20:34
         * isCircle : true
         * isPayCircle : false
         * isVirtual : false
         * shopid : 3878
         * shopname : (圈子商品)vivo X9 全网通 4GB+64GB 移动联通电信4G手机 双卡双待
         * thumb : http://goodimage.91lelegou.com/shopimg/20170119/81457600838595.jpg
         * userCode : 10000001
         */

        private int circleId;
        private String endTime;
        private boolean isCircle;
        private boolean isPayCircle;
        private boolean isVirtual;
        private int shopid;
        private String shopname;
        private String thumb;
        private String userCode;

        public int getCircleId() {
            return circleId;
        }

        public void setCircleId(int circleId) {
            this.circleId = circleId;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public boolean isIsCircle() {
            return isCircle;
        }

        public void setIsCircle(boolean isCircle) {
            this.isCircle = isCircle;
        }

        public boolean isIsPayCircle() {
            return isPayCircle;
        }

        public void setIsPayCircle(boolean isPayCircle) {
            this.isPayCircle = isPayCircle;
        }

        public boolean isIsVirtual() {
            return isVirtual;
        }

        public void setIsVirtual(boolean isVirtual) {
            this.isVirtual = isVirtual;
        }

        public int getShopid() {
            return shopid;
        }

        public void setShopid(int shopid) {
            this.shopid = shopid;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }
    }
}
