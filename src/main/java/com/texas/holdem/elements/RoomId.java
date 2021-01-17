package com.texas.holdem.elements;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomId {
    private String id;

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
