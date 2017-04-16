package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean;

import java.util.List;

/**
 * Created by jipeng on 2017/3/17.
 */

public class HomeShopInformation {


    /**
     * obj : {"canyurenshu":2,"cateid":37,"discountPrice":"","endTime":"","itemId":3596,"money":"54.00","picarr":"[shopimg/20161220/83703803224997.jpg, shopimg/20161220/57847278224997.jpg, shopimg/20161220/64950304224997.jpg, shopimg/20161220/55075576224998.jpg, shopimg/20161220/67628750224998.jpg]","prizeRecord":{"addr":"","endTime":"2016-12-20 17:12:12.768","gonumber":1,"prizeUid":34209,"startTime":"2017-02-24 17:31:00.452","uphoto":"http://headsimage.91lelegou.com/45612471c357f59661903aa1933f7cd2.jpg","userCode":"10000001","username":"Kelly Han"},"qUid":0,"qishu":2,"shaidanCommentNum":0,"shaidanNum":0,"shenyurenshu":52,"shoplistNums":[{"isCurrent":true,"itemId":3596,"qishu":2},{"isCurrent":false,"itemId":3594,"qishu":1}],"sid":3594,"title":"好丽友蛋黄派注芯蛋糕点心营养早餐小面包零食品 2枚x12盒","title2":"","titleStyle":"color:#FF0000;","type":0,"unitMoney":"1.00","yunjiage":1,"zongrenshu":54}
     * result : 0
     * info : 查询商品详情成功
     */

    private ObjBean obj;
    private int result;
    private String info;

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
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

    public static class ObjBean {
        /**
         * canyurenshu : 2
         * cateid : 37
         * discountPrice :
         * endTime :
         * itemId : 3596
         * money : 54.00
         * picarr : [shopimg/20161220/83703803224997.jpg, shopimg/20161220/57847278224997.jpg, shopimg/20161220/64950304224997.jpg, shopimg/20161220/55075576224998.jpg, shopimg/20161220/67628750224998.jpg]
         * prizeRecord : {"addr":"","endTime":"2016-12-20 17:12:12.768","gonumber":1,"prizeUid":34209,"startTime":"2017-02-24 17:31:00.452","uphoto":"http://headsimage.91lelegou.com/45612471c357f59661903aa1933f7cd2.jpg","userCode":"10000001","username":"Kelly Han"}
         * qUid : 0
         * qishu : 2
         * shaidanCommentNum : 0
         * shaidanNum : 0
         * shenyurenshu : 52
         * shoplistNums : [{"isCurrent":true,"itemId":3596,"qishu":2},{"isCurrent":false,"itemId":3594,"qishu":1}]
         * sid : 3594
         * title : 好丽友蛋黄派注芯蛋糕点心营养早餐小面包零食品 2枚x12盒
         * title2 :
         * titleStyle : color:#FF0000;
         * type : 0
         * unitMoney : 1.00
         * yunjiage : 1.0
         * zongrenshu : 54
         */

        private int canyurenshu;
        private int cateid;
        private String discountPrice;
        private String endTime;
        private int itemId;
        private String money;
        private String picarr;
        private PrizeRecordBean prizeRecord;
        private int qUid;
        private int qishu;
        private int shaidanCommentNum;
        private int shaidanNum;
        private int shenyurenshu;
        private int sid;
        private String title;
        private String title2;
        private String titleStyle;
        private int type;
        private String unitMoney;
        private double yunjiage;
        private int zongrenshu;
        private List<ShoplistNumsBean> shoplistNums;

        public int getCanyurenshu() {
            return canyurenshu;
        }

        public void setCanyurenshu(int canyurenshu) {
            this.canyurenshu = canyurenshu;
        }

        public int getCateid() {
            return cateid;
        }

        public void setCateid(int cateid) {
            this.cateid = cateid;
        }

        public String getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getPicarr() {
            return picarr;
        }

        public void setPicarr(String picarr) {
            this.picarr = picarr;
        }

        public PrizeRecordBean getPrizeRecord() {
            return prizeRecord;
        }

        public void setPrizeRecord(PrizeRecordBean prizeRecord) {
            this.prizeRecord = prizeRecord;
        }

        public int getQUid() {
            return qUid;
        }

        public void setQUid(int qUid) {
            this.qUid = qUid;
        }

        public int getQishu() {
            return qishu;
        }

        public void setQishu(int qishu) {
            this.qishu = qishu;
        }

        public int getShaidanCommentNum() {
            return shaidanCommentNum;
        }

        public void setShaidanCommentNum(int shaidanCommentNum) {
            this.shaidanCommentNum = shaidanCommentNum;
        }

        public int getShaidanNum() {
            return shaidanNum;
        }

        public void setShaidanNum(int shaidanNum) {
            this.shaidanNum = shaidanNum;
        }

        public int getShenyurenshu() {
            return shenyurenshu;
        }

        public void setShenyurenshu(int shenyurenshu) {
            this.shenyurenshu = shenyurenshu;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle2() {
            return title2;
        }

        public void setTitle2(String title2) {
            this.title2 = title2;
        }

        public String getTitleStyle() {
            return titleStyle;
        }

        public void setTitleStyle(String titleStyle) {
            this.titleStyle = titleStyle;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUnitMoney() {
            return unitMoney;
        }

        public void setUnitMoney(String unitMoney) {
            this.unitMoney = unitMoney;
        }

        public double getYunjiage() {
            return yunjiage;
        }

        public void setYunjiage(double yunjiage) {
            this.yunjiage = yunjiage;
        }

        public int getZongrenshu() {
            return zongrenshu;
        }

        public void setZongrenshu(int zongrenshu) {
            this.zongrenshu = zongrenshu;
        }

        public List<ShoplistNumsBean> getShoplistNums() {
            return shoplistNums;
        }

        public void setShoplistNums(List<ShoplistNumsBean> shoplistNums) {
            this.shoplistNums = shoplistNums;
        }

        public static class PrizeRecordBean {
            /**
             * addr :
             * endTime : 2016-12-20 17:12:12.768
             * gonumber : 1
             * prizeUid : 34209
             * startTime : 2017-02-24 17:31:00.452
             * uphoto : http://headsimage.91lelegou.com/45612471c357f59661903aa1933f7cd2.jpg
             * userCode : 10000001
             * username : Kelly Han
             */

            private String addr;
            private String endTime;
            private int gonumber;
            private int prizeUid;
            private String startTime;
            private String uphoto;
            private String userCode;
            private String username;

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public int getGonumber() {
                return gonumber;
            }

            public void setGonumber(int gonumber) {
                this.gonumber = gonumber;
            }

            public int getPrizeUid() {
                return prizeUid;
            }

            public void setPrizeUid(int prizeUid) {
                this.prizeUid = prizeUid;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getUphoto() {
                return uphoto;
            }

            public void setUphoto(String uphoto) {
                this.uphoto = uphoto;
            }

            public String getUserCode() {
                return userCode;
            }

            public void setUserCode(String userCode) {
                this.userCode = userCode;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }

        public static class ShoplistNumsBean {
            /**
             * isCurrent : true
             * itemId : 3596
             * qishu : 2
             */

            private boolean isCurrent;
            private int itemId;
            private int qishu;

            public boolean isIsCurrent() {
                return isCurrent;
            }

            public void setIsCurrent(boolean isCurrent) {
                this.isCurrent = isCurrent;
            }

            public int getItemId() {
                return itemId;
            }

            public void setItemId(int itemId) {
                this.itemId = itemId;
            }

            public int getQishu() {
                return qishu;
            }

            public void setQishu(int qishu) {
                this.qishu = qishu;
            }
        }
    }
}
