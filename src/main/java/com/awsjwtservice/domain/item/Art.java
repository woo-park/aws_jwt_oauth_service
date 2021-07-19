package com.awsjwtservice.domain.item;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("R")
public class Art extends Item {

    private String author;
    private String size;
    private Integer height;
    private Integer width;
    private Integer depth;



    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public int getHeight() { return height; }
    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() { return width; }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getDepth() {return depth; }
    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getSize() {
        this.size = this.height + "x" + this.width + (this.depth != 0 ? "x" + this.depth : "");
        return size;
    }


    @Override
    public String toString() {
        return "Art{}";
    }
}
