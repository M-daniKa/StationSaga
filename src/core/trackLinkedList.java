// Language: java
package core;

import entities.trainCar;
import java.util.*;

public class trackLinkedList implements Iterable<trainCar> {

    private trainNode head;
    private trainNode tail;
    private int size;

    public trackLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // -------------------- BASIC INFO --------------------

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    // -------------------- ADD --------------------

    public void addCar(trainCar car) {
        trainNode newNode = new trainNode(car);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    // -------------------- INSERT --------------------

    public boolean insertAt(int index, trainCar car) {
        if (index < 0 || index > size) {
            return false;
        }

        trainNode newNode = new trainNode(car);

        // insert at head
        if (index == 0) {
            newNode.setNext(head);
            head = newNode;
            if (size == 0) tail = newNode;
            size++;
            return true;
        }

        trainNode current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.getNext();
        }

        newNode.setNext(current.getNext());
        current.setNext(newNode);

        if (newNode.getNext() == null) {
            tail = newNode;
        }

        size++;
        return true;
    }

    // -------------------- GET --------------------

    public trainCar getCarAt(int index) {
        if (index < 0 || index >= size) return null;

        trainNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    public trainCar getFirstCar() {
        return head != null ? head.getData() : null;
    }

    public trainCar getLastCar() {
        return tail != null ? tail.getData() : null;
    }

    public int indexOf(trainCar car) {
        trainNode current = head;
        int index = 0;
        while (current != null) {
            if (current.getData() == car) return index;
            current = current.getNext();
            index++;
        }
        return -1;
    }

    // -------------------- SEARCH --------------------

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

    public List<Integer> getOverloadedCarIndices() {
        List<Integer> indices = new ArrayList<>();
        trainNode current = head;
        int index = 0;

        while (current != null) {
            if (current.getData().getCapacity() > 50) {
                indices.add(index);
            }
            current = current.getNext();
            index++;
        }

        return indices;
    }


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

    // -------------------- REMOVE --------------------

    public boolean removeByIndex(int index) {
        if (index < 0 || index >= size || head == null) return false;

        if (index == 0) {
            head = head.getNext();
            if (head == null) tail = null;
            size--;
            return true;
        }

        trainNode prev = head;
        for (int i = 0; i < index - 1; i++) {
            prev = prev.getNext();
        }

        trainNode toRemove = prev.getNext();
        prev.setNext(toRemove.getNext());

        if (toRemove == tail) {
            tail = prev;
        }

        size--;
        return true;
    }

    public void removeByIndexList(List<Integer> indices) {
        if (indices == null || indices.isEmpty()) return;
        indices.sort(Collections.reverseOrder());
        for (int index : indices) {
            removeByIndex(index);
        }
    }

    // -------------------- SWAP --------------------

    public boolean swapByIndex(int i, int j) {
        if (i < 0 || j < 0 || i >= size || j >= size || i == j) {
            return false;
        }

        if (i > j) { int tmp = i; i = j; j = tmp; }

        trainNode prevI = null, currI = head;
        for (int k = 0; k < i; k++) {
            prevI = currI;
            currI = currI.getNext();
        }

        trainNode prevJ = null, currJ = head;
        for (int k = 0; k < j; k++) {
            prevJ = currJ;
            currJ = currJ.getNext();
        }

        if (prevI != null) prevI.setNext(currJ);
        else head = currJ;

        if (prevJ != null) prevJ.setNext(currI);

        trainNode temp = currI.getNext();
        currI.setNext(currJ.getNext());
        currJ.setNext(temp);

        if (currI.getNext() == null) tail = currI;
        if (currJ.getNext() == null) tail = currJ;

        return true;
    }

    // -------------------- SORT --------------------

    public void sortByCapacityAscending() {
        if (size < 2) return;

        boolean swapped;
        do {
            swapped = false;
            trainNode prev = null;
            trainNode curr = head;

            while (curr != null && curr.getNext() != null) {
                trainNode next = curr.getNext();

                if (curr.getData().getCapacity() > next.getData().getCapacity()) {

                    if (prev != null) prev.setNext(next);
                    else head = next;

                    curr.setNext(next.getNext());
                    next.setNext(curr);

                    if (curr.getNext() == null) tail = curr;

                    swapped = true;
                    prev = next;
                } else {
                    prev = curr;
                    curr = curr.getNext();
                }
            }
        } while (swapped);
    }

    // -------------------- UTIL --------------------

    public List<trainCar> toList() {
        List<trainCar> result = new ArrayList<>(size);
        trainNode current = head;
        while (current != null) {
            result.add(current.getData());
            current = current.getNext();
        }
        return result;
    }

    @Override
    public Iterator<trainCar> iterator() {
        return new Iterator<>() {
            private trainNode current = head;

            public boolean hasNext() {
                return current != null;
            }

            public trainCar next() {
                if (current == null) throw new NoSuchElementException();
                trainCar data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }
}
