package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean;

import java.util.List;

/**
 * Created by jipeng on 2017/3/27.
 */

public class PayMoneyModel {

    /**
     * objList : [{"title":"(第132期) 百草味 3袋组合装\u2009|\u2009金枕头榴莲干 30g/袋 香浓榴莲 爱我所爱","num":10},{"title":"(第2期) 好丽友蛋黄派注芯蛋糕点心营养早餐小面包零食品 2枚x12盒 ","num":1},{"title":"(第28期) 全国通用移动话费充值200元自动快充 ","num":1}]
     * totalMoney : 12.0
     * ledou : 100.0
     * result : 0
     * info : 去结算成功
     */

    private double totalMoney;
    private double ledou;
    private int result;
    private String info;
    private List<ObjListBean> objList;

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getLedou() {
        return ledou;
    }

    public void setLedou(double ledou) {
        this.ledou = ledou;
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
         * title : (第132期) 百草味 3袋组合装 | 金枕头榴莲干 30g/袋 香浓榴莲 爱我所爱
         * num : 10
         */

        private String title;
        private int num;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
