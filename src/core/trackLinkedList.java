package core;
import entities.trainCar;
import java.util.*;

public class trackLinkedList {
    private trainNode head;
    private trainNode tail;
    private int size;

    public trackLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    //Add
    public void addCar(trainCar car) {
        trainNode newNode = new trainNode(car);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }
    //Search
    public List<Integer> searchByState(trainCar.carState state) {
        List<Integer> indices = new ArrayList<>();
        trainNode current = head;
        int index = 0;
        while (current != null) {
            if (current.getData().getState() == state) {
                indices.add(index);
            }
            current = current.getNext();
            index++;
        }
        return indices;
    }
    //Remove
    public boolean removeByIndex(int index) {
        if (index < 0 || head == null) {
            return false;
        }
        if (index == 0) {
            head = head.getNext();
            if (head == null) {
                tail = null;
            }
            size--;
            return true;
        }
        trainNode current = head;
        trainNode prev = null;
        int count = 0;

        while (current != null) {
            if (count == index) {
                prev.setNext(current.getNext());
                if (current == tail) {
                    tail = prev;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.getNext();
            count++;
        }
        return false;
    }
    public void removeAllByState(trainCar.carState state) {
        while (head != null && head.getData().getState() == state) {
            head = head.getNext();
            size--;
        }
        if (head == null) {
            tail = null;
            return;
        }
        trainNode current = head;
        while (current.getNext() != null) {
            if (current.getNext().getData().getState() == state) {
                current.setNext(current.getNext().getNext());
                size--;
            } else {
                current = current.getNext();
            }
        }
        tail = current;
    }
    public void bullshitsomething (){
        
    }
}
