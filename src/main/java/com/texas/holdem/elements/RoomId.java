package com.texas.holdem.elements;

import java.util.Objects;

public class RoomId {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoomId() {
    }

    public RoomId(String id) {
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
        return id.equals(roomId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
