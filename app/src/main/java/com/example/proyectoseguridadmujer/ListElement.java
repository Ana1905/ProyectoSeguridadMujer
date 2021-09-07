package com.example.proyectoseguridadmujer;

public class ListElement {
    public String user_name;
    public String category;
    public String post_content;


    public ListElement(String user_name,String category, String post_content){
        this.user_name= user_name;
        this.category=category;
        this.post_content = post_content;
    }
    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
