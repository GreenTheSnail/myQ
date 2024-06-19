import java.util.Objects;

public class Coordinates implements Comparable<Coordinates> {
    private Integer x;
    private Integer y;

    public Coordinates(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "{ \"X\" : " + x + ", \"Y\" : " + y + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinates coordinates = (Coordinates) obj;
        return Objects.equals(x, coordinates.x) &&
                Objects.equals(y, coordinates.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Coordinates coordinates) {
        int compareX = this.x.compareTo(coordinates.x);
        if (compareX != 0) {
            return compareX;
        }
        return this.y.compareTo(coordinates.y);
    }
}
