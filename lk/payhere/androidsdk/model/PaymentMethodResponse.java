package lk.payhere.androidsdk.model;

import java.util.HashMap;

/* loaded from: classes7.dex */
public class PaymentMethodResponse {
    private HashMap<String, Data> data;
    private String msg;
    private int status;

    /* loaded from: classes7.dex */
    public class Data {
        private String imageUrl;
        private Size viewSize;

        public Data() {
        }

        public String getImageUrl() {
            return this.imageUrl;
        }

        public Size getViewSize() {
            return this.viewSize;
        }

        public void setImageUrl(String str) {
            this.imageUrl = str;
        }

        public void setViewSize(Size size) {
            this.viewSize = size;
        }
    }

    /* loaded from: classes7.dex */
    public class Size {
        private int height;
        private int width;

        public Size() {
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        public void setHeight(int i) {
            this.height = i;
        }

        public void setWidth(int i) {
            this.width = i;
        }
    }

    public HashMap<String, Data> getData() {
        return this.data;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getStatus() {
        return this.status;
    }

    public void setData(HashMap<String, Data> hashMap) {
        this.data = hashMap;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public void setStatus(int i) {
        this.status = i;
    }
}
