package core;
import entities.trainCar;

public class stationUtils {
    public static int compareCapacity(trainCar a, trainCar b) {
        return Integer.compare(a.getCapacity(), b.getCapacity());
    }

    public static String carInfo(trainCar car) {
        return "Car Type: " + car.getType().toString() + ", Capacity: " + car.getCapacity();
    }
}
