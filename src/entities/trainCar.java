package entities;

public class trainCar {
    public enum carType {PASSENGER, LUGGAGE}
    public enum carState {AVAILABLE, FULL, DAMAGED}

    private carType type;
    private int capacity;
    private carState state;

    public trainCar(carType type, int capacity, carState state) {
        this.type = type;
        this.capacity = capacity;
        this.state = state;
    }

    public carType getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public carState getState() {return state;}

    public void setType(carType type) {
        this.type = type;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setState(carState state) {
        this.state = state;
    }
}
