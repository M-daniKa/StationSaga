// Language: java
package levels;

import core.trackLinkedList;
import entities.trainCar;

import java.util.List;

public class level1_AddRemove {

    // underlying data structure
    private final trackLinkedList track;

    // flags controlled by dialogue / level logic (what is allowed)
    private boolean canAdd;
    private boolean canDelete;
    private boolean canSearch;
    private boolean canSwap;
    private boolean canSort;

    // progress flags (what the player has already done)
    private boolean addOnceDone;
    private boolean addSecondDone;
    private boolean deleteDone;

    public level1_AddRemove() {
        this.track = new trackLinkedList();
        this.canAdd = false;
        this.canDelete = false;
        this.canSearch = false;
        this.canSwap = false;
        this.canSort = false;

        this.addOnceDone = false;
        this.addSecondDone = false;
        this.deleteDone = false;
    }

    // --- permission setters ---

    public void enableAdd(boolean enable) {
        this.canAdd = enable;
    }

    public void enableDelete(boolean enable) {
        this.canDelete = enable;
    }

    public void enableSearch(boolean enable) {
        this.canSearch = enable;
    }

    public void enableSwap(boolean enable) {
        this.canSwap = enable;
    }

    public void enableSort(boolean enable) {
        this.canSort = enable;
    }

    // --- permission getters (for UI checks / messages) ---

    public boolean isCanAdd() {
        return canAdd;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public boolean isCanSearch() {
        return canSearch;
    }

    public boolean isCanSwap() {
        return canSwap;
    }

    public boolean isCanSort() {
        return canSort;
    }

    // --- progress getters (optional, mainly for testing) ---

    public boolean isAddOnceDone() {
        return addOnceDone;
    }

    public boolean isAddSecondDone() {
        return addSecondDone;
    }

    public boolean isDeleteDone() {
        return deleteDone;
    }

    // --- high level actions, using trackLinkedList ---

    // Called by UI when Add button is pressed
    public ActionResult performAddCar() {
        if (!canAdd) {
            return ActionResult.notAllowed("You can only add a car when the dialogue tells you to.");
        }

        // Example car, you can change this if needed
        trainCar car = new trainCar(
                trainCar.carType.PASSENGER,
                10,
                trainCar.carState.AVAILABLE
        );

        track.addCar(car);
        int index = track.getSize() - 1;

        // Progress logic: first and second adds for Station 1
        if (!addOnceDone) {
            addOnceDone = true;
        } else if (!addSecondDone) {
            addSecondDone = true;
        }

        return ActionResult.successAdded(car, index);
    }

    // Called by UI when Delete button is pressed
    public ActionResult performDeleteByIndex(int index) {
        if (!canDelete) {
            return ActionResult.notAllowed("You can only delete a car when the dialogue tells you to.");
        }
        if (index < 0) {
            return ActionResult.error("Please enter a non-negative index.");
        }

        boolean removed = track.removeByIndex(index);
        if (!removed) {
            return ActionResult.error("No car at index " + index + ".");
        }

        // mark progress
        deleteDone = true;
        return ActionResult.successDeleted(index);
    }

    // Called by UI when Search button is pressed
    public ActionResult performSearchByCapacity(trainCar.carType type, int capacity) {
        if (!canSearch) {
            return ActionResult.notAllowed("You can only search when the dialogue tells you to.");
        }
        if (capacity < 0) {
            return ActionResult.error("Please enter a non-negative capacity.");
        }

        List<Integer> indices = track.searchByCapacity(type, capacity);
        return ActionResult.successSearch(indices, capacity);
    }

    // --- dialogue gating logic ---

    /**
     * Decide whether the player is allowed to advance past a given dialogue index.
     * Return true if it is okay to go to the next line.
     *
     * You can tune the indices based on level1\.txt and how levelDataLoader loads it.
     */
    public boolean canAdvanceFrom(int dialogueIndex) {
        // In Station 1 tutorial:
        // index 3: "To add a train car, click that small add button below!"
        // Player must add at least one car before going past index 4 or 5 (after feedback).

        // After the tutorial text that immediately follows index 3, enforce addOnceDone
        // Choose a range to lock; for example, block leaving indices 3 and 4 until first add.
        if (dialogueIndex >= 3 && dialogueIndex <= 4) {
            return addOnceDone;
        }

        // Later line "To add another train car, click the add button again."
        // In the file this is near the end of Station 1. Assume this ends up near index 15.
        // Block some small range around that until second add is done.
        if (dialogueIndex >= 15 && dialogueIndex <= 16) {
            return addSecondDone;
        }

        // Station 2:
        // "To delete a train car, click the delete button below!"
        // Assume this line is near index 26 overall and block range until deleteDone.
        if (dialogueIndex >= 26 && dialogueIndex <= 27) {
            return deleteDone;
        }

        // Default: no restriction
        return true;
    }

    // --- access to underlying list (if UI wants to show something) ---

    public trackLinkedList getTrack() {
        return track;
    }

    public int getTrainSize() {
        return track.getSize();
    }

    // --- Simple result type so UI only shows messages, no logic ---

    public static class ActionResult {
        public enum Type {
            SUCCESS_ADD,
            SUCCESS_DELETE,
            SUCCESS_SEARCH,
            NOT_ALLOWED,
            ERROR
        }

        private final Type type;
        private final String message;
        private final trainCar addedCar;
        private final Integer index;
        private final List<Integer> searchIndices;
        private final Integer capacity;

        private ActionResult(Type type,
                             String message,
                             trainCar addedCar,
                             Integer index,
                             List<Integer> searchIndices,
                             Integer capacity) {
            this.type = type;
            this.message = message;
            this.addedCar = addedCar;
            this.index = index;
            this.searchIndices = searchIndices;
            this.capacity = capacity;
        }

        public static ActionResult notAllowed(String msg) {
            return new ActionResult(Type.NOT_ALLOWED, msg, null, null, null, null);
        }

        public static ActionResult error(String msg) {
            return new ActionResult(Type.ERROR, msg, null, null, null, null);
        }

        public static ActionResult successAdded(trainCar car, int index) {
            return new ActionResult(Type.SUCCESS_ADD, null, car, index, null, null);
        }

        public static ActionResult successDeleted(int index) {
            return new ActionResult(Type.SUCCESS_DELETE, null, null, index, null, null);
        }

        public static ActionResult successSearch(List<Integer> indices, int capacity) {
            return new ActionResult(Type.SUCCESS_SEARCH, null, null, null, indices, capacity);
        }

        public Type getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }

        public trainCar getAddedCar() {
            return addedCar;
        }

        public Integer getIndex() {
            return index;
        }

        public List<Integer> getSearchIndices() {
            return searchIndices;
        }

        public Integer getCapacity() {
            return capacity;
        }
    }
}
