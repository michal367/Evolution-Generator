package main;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean precedes(Vector2d v){
        return (this.x != v.x && this.y != v.y && this.x >= v.x && this.y >= v.y);
    }
    public boolean follows(Vector2d v){
        return (this.x != v.x && this.y != v.y && this.x <= v.x && this.y <= v.y);
    }

    public Vector2d upperRight(Vector2d v){
        return new Vector2d(max(this.x, v.x), max(this.y, v.y));
    }
    public Vector2d lowerLeft(Vector2d v){
        return new Vector2d(min(this.y, v.y), min(this.x, v.x));
    }

    public Vector2d add(Vector2d v){
        return new Vector2d(this.x + v.x, this.y + v.y);
    }
    public Vector2d subtract(Vector2d v){
        return new Vector2d(this.x - v.x, this.y - v.y);
    }
    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }


    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return (this.x == that.x && this.y == that.y);
    }
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    public String toString(){
        return "(" + x + "," + y + ")";
    }
}
