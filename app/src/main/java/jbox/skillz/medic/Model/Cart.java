package jbox.skillz.medic.Model;

import java.util.ArrayList;

public class Cart {
      public String name;
      public String  price;
      public String  totalPrice;
      public String image;
      public String quantity;
      public String description;

      public String address;
      public ArrayList<Cart> ordersList;

      public Cart() {
      }

      public Cart(String name, String price, String totalPrice, String image, String quantity, String description) {
            this.name = name;
            this.price = price;
            this.totalPrice = totalPrice;
            this.image = image;
            this.quantity = quantity;
            this.description = description;
      }

      public Cart(String name, String price, String totalPrice, String image, String quantity) {
            this.name = name;
            this.price = price;
            this.totalPrice = totalPrice;
            this.image = image;
            this.quantity = quantity;
      }

      public Cart(String name, String price, String image, String quantity) {
            this.name = name;
            this.price = price;
            this.image = image;
            this.quantity = quantity;
      }

      public Cart(String address, ArrayList<Cart> ordersList) {
            this.address = address;
            this.ordersList = ordersList;
      }

      public String getDescription() {
            return description;
      }

      public void setDescription(String description) {
            this.description = description;
      }

      public String getName() {
            return name;
      }

      public void setName(String name) {
            this.name = name;
      }

      public String getPrice() {
            return price;
      }

      public void setPrice(String price) {
            this.price = price;
      }

      public String getTotalPrice() {
            return totalPrice;
      }

      public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
      }

      public String getImage() {
            return image;
      }

      public void setImage(String image) {
            this.image = image;
      }

      public String getQuantity() {
            return quantity;
      }

      public void setQuantity(String quantity) {
            this.quantity = quantity;
      }

      public String getAddress() {
            return address;
      }

      public void setAddress(String address) {
            this.address = address;
      }

      public ArrayList<Cart> getOrdersList() {
            return ordersList;
      }

      public void setOrdersList(ArrayList<Cart> ordersList) {
            this.ordersList = ordersList;
      }
}
