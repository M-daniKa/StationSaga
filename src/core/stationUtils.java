package core;
import entities.trainCar;

public class stationUtils {
    public static String carInfo(trainCar car) {
        return "Car Type: " + car.getType().toString() + ", Capacity: " + car.getCapacity() + ", State: " + car.getState().toString();
    }
    public static String describeState(trainCar.carState state) {
        switch (state) {
            case AVAILABLE:
                return "The car still has available space.";
            case FULL:
                return "The car is full and cannot accept more load.";
            case DAMAGED:
                return "The car is damaged and must be removed.";
            default:
                return "Unknown state.";
        }
    }

}
