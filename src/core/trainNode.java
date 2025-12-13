package core;
import entities.trainCar;

public class trainNode {
    private trainCar data;
    private  trainNode next;

    public trainNode (trainCar data) {
        this.data = data;
        this.next = null;
    }

    public trainCar getData() {
        return data;
    }

    public trainNode getNext() {
        return next;
    }

    public void setData (trainCar data) {
        this.data = data;
    }

    public void setNext(trainNode next) {
        this.next = next;
    }


}
