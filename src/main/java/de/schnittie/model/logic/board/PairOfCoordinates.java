package de.schnittie.model.businesscode.board;

public record PairOfCoordinates(int x, int y) implements Comparable<PairOfCoordinates>{
    @Override
    public String toString() {
        return (x + " " + y);
    }
    @Override
    public int compareTo(PairOfCoordinates other) {
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

        PairOfCoordinates other = (PairOfCoordinates) obj;
        return this.x() == other.x() && this.y() == other.y();
    }
}
