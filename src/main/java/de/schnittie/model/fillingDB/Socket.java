package de.schnittie.model.fillingDB;

import java.util.Arrays;

public record Socket(int[] colours) implements Comparable<Socket>{
    @Override
    public int compareTo(Socket other) {
        if (this.equals(other)) {
            return 0;
        }
        return 1;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Socket other = (Socket) obj;
        return Arrays.equals(colours, other.colours);
    }
}
