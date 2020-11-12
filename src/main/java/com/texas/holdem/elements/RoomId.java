package com.texas.holdem.elements;

import java.util.Objects;

public class RoomId {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoomId() {
    }

    public RoomId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RoomId{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomId roomId = (RoomId) o;
        return id == roomId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
