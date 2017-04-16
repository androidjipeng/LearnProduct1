package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean;

import java.util.List;

/**
 * Created by jipeng on 2017/4/1.
 */

public class SettingAddressModel {

    /**
     * status : 1
     * info : 所有地址
     * jsonMsg : [{"addressId":"70","consignee":"qqq","phoneNumber":"12345678909","provinceBean":{"provinceId":"826","provinceName":"河北省"},"cityBean":{"cityId":"867","cityName":"邯郸市"},"districtBean":{"districtId":"872","districtName":"邯郸县"},"address":"11111","isDefault":1},{"addressId":"69","consignee":"qqq","phoneNumber":"18501755867","provinceBean":{"provinceId":"826","provinceName":"河北省"},"cityBean":{"cityId":"859","cityName":"秦皇岛市"},"districtBean":{"districtId":"861","districtName":"山海关区"},"address":"1111","isDefault":0},{"addressId":"68","consignee":"qqq","phoneNumber":"18501755867","provinceBean":{"provinceId":"826","provinceName":"河北省"},"cityBean":{"cityId":"867","cityName":"邯郸市"},"districtBean":{"districtId":"873","districtName":"临漳县"},"address":"qqqqqq","isDefault":0},{"addressId":"67","consignee":"ee","phoneNumber":"12345678909","provinceBean":{"provinceId":"826","provinceName":"河北省"},"cityBean":{"cityId":"867","cityName":"邯郸市"},"districtBean":{"districtId":"873","districtName":"临漳县"},"address":"wwwww","isDefault":0},{"addressId":"66","consignee":"1111","phoneNumber":"18501755867","provinceBean":{"provinceId":"826","provinceName":"河北省"},"cityBean":{"cityId":"859","cityName":"秦皇岛市"},"districtBean":{"districtId":"862","districtName":"北戴河区"},"address":"1111111","isDefault":0}]
     */

    private int status;
    private String info;
    private List<JsonMsgBean> jsonMsg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<JsonMsgBean> getJsonMsg() {
        return jsonMsg;
    }

    public void setJsonMsg(List<JsonMsgBean> jsonMsg) {
        this.jsonMsg = jsonMsg;
    }

    public static class JsonMsgBean {
        /**
         * addressId : 70
         * consignee : qqq
         * phoneNumber : 12345678909
         * provinceBean : {"provinceId":"826","provinceName":"河北省"}
         * cityBean : {"cityId":"867","cityName":"邯郸市"}
         * districtBean : {"districtId":"872","districtName":"邯郸县"}
         * address : 11111
         * isDefault : 1
         */

        private String addressId;
        private String consignee;
        private String phoneNumber;
        private ProvinceBeanBean provinceBean;
        private CityBeanBean cityBean;
        private DistrictBeanBean districtBean;
        private String address;
        private int isDefault;

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public ProvinceBeanBean getProvinceBean() {
            return provinceBean;
        }

        public void setProvinceBean(ProvinceBeanBean provinceBean) {
            this.provinceBean = provinceBean;
        }

        public CityBeanBean getCityBean() {
            return cityBean;
        }

        public void setCityBean(CityBeanBean cityBean) {
            this.cityBean = cityBean;
        }

        public DistrictBeanBean getDistrictBean() {
            return districtBean;
        }

        public void setDistrictBean(DistrictBeanBean districtBean) {
            this.districtBean = districtBean;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        public static class ProvinceBeanBean {
            /**
             * provinceId : 826
             * provinceName : 河北省
             */

            private String provinceId;
            private String provinceName;

            public String getProvinceId() {
                return provinceId;
            }

            public void setProvinceId(String provinceId) {
                this.provinceId = provinceId;
            }

            public String getProvinceName() {
                return provinceName;
            }

            public void setProvinceName(String provinceName) {
                this.provinceName = provinceName;
            }
        }

        public static class CityBeanBean {
            /**
             * cityId : 867
             * cityName : 邯郸市
             */

            private String cityId;
            private String cityName;

            public String getCityId() {
                return cityId;
            }

            public void setCityId(String cityId) {
                this.cityId = cityId;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }
        }

        public static class DistrictBeanBean {
            /**
             * districtId : 872
             * districtName : 邯郸县
             */

            private String districtId;
            private String districtName;

            public String getDistrictId() {
                return districtId;
            }

            public void setDistrictId(String districtId) {
                this.districtId = districtId;
            }

            public String getDistrictName() {
                return districtName;
            }

            public void setDistrictName(String districtName) {
                this.districtName = districtName;
            }
        }
    }
}
