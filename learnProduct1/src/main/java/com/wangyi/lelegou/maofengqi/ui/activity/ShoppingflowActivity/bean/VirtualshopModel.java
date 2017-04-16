package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean;

import java.util.List;

/**
 * Created by jipeng on 2017/3/31.
 */

public class VirtualshopModel {

    /**
     * objList : [{"accountCode":"","remarks":""}]
     * result : 0
     * info : 获得地址成功
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
         * accountCode :
         * remarks :
         */

        private String accountCode;
        private String remarks;

        public String getAccountCode() {
            return accountCode;
        }

        public void setAccountCode(String accountCode) {
            this.accountCode = accountCode;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
