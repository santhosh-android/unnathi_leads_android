package com.leadapplication.app.Model;

public class HomeDashBoardRvModel {
    private int image_dashBoard;
    private String name_dashBoard;

    public HomeDashBoardRvModel(int image_dashBoard, String name_dashBoard) {
        this.image_dashBoard = image_dashBoard;
        this.name_dashBoard = name_dashBoard;
    }

    public int getImage_dashBoard() {
        return image_dashBoard;
    }

    public void setImage_dashBoard(int image_dashBoard) {
        this.image_dashBoard = image_dashBoard;
    }

    public String getName_dashBoard() {
        return name_dashBoard;
    }

    public void setName_dashBoard(String name_dashBoard) {
        this.name_dashBoard = name_dashBoard;
    }
}
