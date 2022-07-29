package jbox.skillz.medic.Model;

public class Products {

//    private int image;
//    private String offer;

      public String price;
      public String image;
      public String name;
      public String description;
      public String count;
      public String quantity;
      public String uid;

      public Products() {
      }

      public Products(String price, String image, String name, String description, String count, String quantity, String uid) {
            this.price = price;
            this.image = image;
            this.name = name;
            this.description = description;
            this.count = count;
            this.quantity = quantity;
            this.uid = uid;
      }

      public String getPrice() {
            return price;
      }

      public void setPrice(String price) {
            this.price = price;
      }

      public String getImage() {
            return image;
      }

      public void setImage(String image) {
            this.image = image;
      }

      public String getName() {
            return name;
      }

      public void setName(String name) {
            this.name = name;
      }

      public String getDescription() {
            return description;
      }

      public void setDescription(String description) {
            this.description = description;
      }

      public String getCount() {
            return count;
      }

      public void setCount(String count) {
            this.count = count;
      }

      public String getQuantity() {
            return quantity;
      }

      public void setQuantity(String quantity) {
            this.quantity = quantity;
      }

      public String getUid() {
            return uid;
      }

      public void setUid(String uid) {
            this.uid = uid;
      }
}
