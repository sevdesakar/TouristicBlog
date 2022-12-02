package com.example.tasarim.Models;

public class yorum_model{

 private String  yorum,gonderen,postId ;

 public yorum_model(String yorum, String gonderen, String postId) {
  this.yorum = yorum;
  this.gonderen = gonderen;
  this.postId = postId;
 }

 public yorum_model(){}

 public String getYorum() {
  return yorum;
 }

 public void setYorum(String yorum) {
  this.yorum = yorum;
 }

 public String getGonderen() {
  return gonderen;
 }

 public void setGonderen(String gonderen) {
  this.gonderen = gonderen;
 }

 public String getPostId() {
  return postId;
 }

 public void setPostId(String postId) {
  this.postId = postId;
 }
}
