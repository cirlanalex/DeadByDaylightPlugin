package org.rednero.deadbydaylight.utils.structs;

public class Coordinates {
    private double x;
    private double y;
    private double z;

    public Coordinates() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Coordinates(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public double getZ(){
        return this.z;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setZ(double z){
        this.z = z;
    }

    public String saveToString() {
        return this.x + "::" + this.y + "::" + this.z;
    }

    public void loadFromString(String string) {
        String[] split = string.split("::");
        this.x = Double.parseDouble(split[0]);
        this.y = Double.parseDouble(split[1]);
        this.z = Double.parseDouble(split[2]);
    }
}
