package com.aily.northeastelecstore.bean;

/**
 4  * 购物车实体类
 5  * 测试
 6  */
  public class Product {
      //商品名称
             private String name;
     // 商品数量
             private int num;
     // 该商品总价
             private int price;

            @Override
     public String toString() {
                 return "Product{" +
                                 "name='" + name + '\'' +
                                 ", num=" + num +
                                 ", price=" + price +
                                 '}';
             }

             public void setName(String name) {
                 this.name = name;
             }

             public void setNum(int num) {
                 this.num = num;
             }

             public void setPrice(int price) {
                 this.price = price;
            }

             public String getName() {
                 return name;
             }

            public int getNum() {
                 return num;
             }
            public int getPrice() {
                 return price;
             }
 }