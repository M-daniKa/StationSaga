// Language: java
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

    public int getSize() {
        return size;
    }

    // Add a car at the end
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

    // Get car by index (0-based), or null if out of range
    public trainCar getCarAt(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        trainNode current = head;
        int i = 0;
        while (current != null && i < index) {
            current = current.getNext();
            i++;
        }
        return current != null ? current.getData() : null;
    }

    // Search by state
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

    // Search by capacity (type parameter is available if you want to extend)
    public List<Integer> searchByCapacity(trainCar.carType type, int capacity) {
        List<Integer> indices = new ArrayList<>();
        trainNode current = head;
        int index = 0;
        while (current != null) {
            if (current.getData().getCapacity() == capacity) {
                indices.add(index);
            }
            current = current.getNext();
            index++;
        }
        return indices;
    }

    // Remove by index (0-based)
    public boolean removeByIndex(int index) {
        if (index < 0 || head == null) {
            return false;
        }

        // remove head
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
                // unlink
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

    // Remove multiple indices (expects 0-based indices)
    public void removeByIndexList(List<Integer> indices) {
        if (indices == null || indices.isEmpty()) {
            return;
        }
        indices.sort(Collections.reverseOrder());
        for (int index : indices) {
            removeByIndex(index);
        }
    }
}
