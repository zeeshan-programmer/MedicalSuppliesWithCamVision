package jbox.skillz.medic.Model;

public class Order {
      public String  items;
      public String price;
      public String time;
      public String status;
      public String uid;
      public String orderId;
      public String payment;
      public String address;
      public String mobile;



      public Order() {
      }

      public Order(String items, String price, String time, String status, String address) {
            this.items = items;
            this.price = price;
            this.time = time;
            this.status = status;
            this.address = address;
      }

      public Order(String items, String price, String time, String status, String address, String uid) {
            this.items = items;
            this.price = price;
            this.time = time;
            this.status = status;
            this.address = address;
            this.uid = uid;
      }

      public Order(String items, String price, String time, String status, String uid, String orderId, String payment, String address) {
            this.items = items;
            this.price = price;
            this.time = time;
            this.status = status;
            this.uid = uid;
            this.orderId = orderId;
            this.payment = payment;
            this.address = address;
      }

      public Order(String items, String price, String time, String status, String uid, String orderId, String payment, String address, String mobile) {
            this.items = items;
            this.price = price;
            this.time = time;
            this.status = status;
            this.uid = uid;
            this.orderId = orderId;
            this.payment = payment;
            this.address = address;
            this.mobile = mobile;
      }

      public String getMobile() {
            return mobile;
      }

      public void setMobile(String mobile) {
            this.mobile = mobile;
      }

      public String getOrderId() {
            return orderId;
      }

      public void setOrderId(String orderId) {
            this.orderId = orderId;
      }

      public String getPayment() {
            return payment;
      }

      public void setPayment(String payment) {
            this.payment = payment;
      }

      public String getUid() {
            return uid;
      }

      public void setUid(String uid) {
            this.uid = uid;
      }

      public String getItems() {
            return items;
      }

      public void setItems(String items) {
            this.items = items;
      }

      public String getPrice() {
            return price;
      }

      public void setPrice(String price) {
            this.price = price;
      }

      public String getTime() {
            return time;
      }

      public void setTime(String time) {
            this.time = time;
      }

      public String getStatus() {
            return status;
      }

      public void setStatus(String status) {
            this.status = status;
      }

      public String getAddress() {
            return address;
      }

      public void setAddress(String address) {
            this.address = address;
      }
}
