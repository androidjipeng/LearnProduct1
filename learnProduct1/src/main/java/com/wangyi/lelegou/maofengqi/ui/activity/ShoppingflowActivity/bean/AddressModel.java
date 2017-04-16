package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean;

import java.util.List;

/**
 * Created by jipeng on 2017/3/31.
 */

public class AddressModel {

    /**
     * objList : [{"shouhuoren":"To","mobile":"18638471210","id":63,"addr":"吉林省 长春市 南关区 Ttttttt"}]
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
         * shouhuoren : To
         * mobile : 18638471210
         * id : 63
         * addr : 吉林省 长春市 南关区 Ttttttt
         */

        private String shouhuoren;
        private String mobile;
        private int id;
        private String addr;

        public String getShouhuoren() {
            return shouhuoren;
        }

        public void setShouhuoren(String shouhuoren) {
            this.shouhuoren = shouhuoren;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }
    }
}
