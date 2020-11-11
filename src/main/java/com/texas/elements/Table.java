package com.texas.elements;

public class Table {
    String roomId;
    int size;

    public Table(String roomId, int size) {
        this.roomId = roomId;
        this.size = size;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
