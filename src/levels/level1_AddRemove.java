// Language: java
package levels;

import core.trackLinkedList;
import entities.DialogueEntry;
import entities.trainCar;

import java.util.List;

public class level1_AddRemove {

    // underlying data structure
    private final trackLinkedList track;

    // permissions controlled by dialogue
    private boolean canAdd;
    private boolean canDelete;
    private boolean canSearch;
    private boolean canSwap;
    private boolean canSort;
    private boolean canInsert;

    // progress flags
    private boolean addOnceDone;
    private boolean addSecondDone;
    private boolean deleteDone;
    private boolean searchDone;
    private boolean swapDone;
    private boolean sortDone;
    private boolean insertDone;

    public level1_AddRemove() {
        this.track = new trackLinkedList();
        resetPermissions();
    }

    // -------------------- PERMISSIONS --------------------

    private void resetPermissions() {
        canAdd = false;
        canDelete = false;
        canSearch = false;
        canSwap = false;
        canSort = false;
        canInsert = false;
    }

    /**
     * Called whenever dialogue changes.
     * This is the ONLY place that parses dialogue text.
     */
    public void updateControllerState(DialogueEntry entry) {
        resetPermissions();
        String t = entry.getText().toLowerCase();

        canAdd    = t.contains("add");
        canDelete = t.contains("delete");
        canSearch = t.contains("search");
        canSwap   = t.contains("swap");
        canSort   = t.contains("sort");
        canInsert = t.contains("insert");
    }

    // -------------------- GETTERS --------------------

    public boolean isCanAdd()    { return canAdd; }
    public boolean isCanDelete() { return canDelete; }
    public boolean isCanSearch() { return canSearch; }
    public boolean isCanSwap()   { return canSwap; }
    public boolean isCanSort()   { return canSort; }
    public boolean isCanInsert() { return canInsert; }

    // -------------------- ACTIONS --------------------

    public ActionResult performAddCar() {
        if (!canAdd) return ActionResult.notAllowed("You can only add a car when instructed.");

        trainCar car = new trainCar(
                trainCar.carType.PASSENGER,
                10,
                trainCar.carState.AVAILABLE
        );

        track.addCar(car);
        int index = track.getSize() - 1;

        if (!addOnceDone) addOnceDone = true;
        else addSecondDone = true;

        return ActionResult.successAdded(car, index);
    }

    public ActionResult performDeleteByIndex(int index) {
        if (!canDelete) return ActionResult.notAllowed("You can only delete when instructed.");
        if (index < 0) return ActionResult.error("Index must be non-negative.");

        boolean removed = track.removeByIndex(index);
        if (!removed) return ActionResult.error("No car at index " + index);

        deleteDone = true;
        return ActionResult.successDeleted(index);
    }

    public ActionResult performSearchByCapacity(trainCar.carType type, int capacity) {
        if (!canSearch) return ActionResult.notAllowed("You can only search when instructed.");
        if (capacity < 0) return ActionResult.error("Capacity must be non-negative.");

        List<Integer> result = track.searchByCapacity(type, capacity);
        searchDone = true;
        return ActionResult.successSearch(result, capacity);
    }

    public ActionResult performSwap(int index1, int index2) {
        if (!canSwap) return ActionResult.notAllowed("You can only swap when instructed.");
        if (index1 < 0 || index2 < 0) return ActionResult.error("Invalid index.");

        boolean ok = track.swapByIndex(index1, index2);
        if (!ok) return ActionResult.error("Swap failed.");

        swapDone = true;
        return ActionResult.successGeneric("Cars swapped successfully.");
    }

    public ActionResult performSortAscending() {
        if (!canSort) return ActionResult.notAllowed("You can only sort when instructed.");

        track.sortByCapacityAscending();
        sortDone = true;
        return ActionResult.successGeneric("Train sorted from lightest to heaviest.");
    }

    public ActionResult performInsertAt(int index) {
        if (!canInsert) return ActionResult.notAllowed("You can only insert when instructed.");
        if (index < 0) return ActionResult.error("Invalid index.");

        trainCar car = new trainCar(
                trainCar.carType.PASSENGER,
                5,
                trainCar.carState.AVAILABLE
        );

        boolean ok = track.insertAt(index, car);
        if (!ok) return ActionResult.error("Insert failed.");

        insertDone = true;
        return ActionResult.successGeneric("Car inserted at index " + index);
    }

    // -------------------- DIALOGUE GATING --------------------

    public boolean canAdvanceFrom(int dialogueIndex) {

        if (dialogueIndex >= 3 && dialogueIndex <= 4)
            return addOnceDone;

        if (dialogueIndex >= 15 && dialogueIndex <= 16)
            return addSecondDone;

        if (dialogueIndex >= 26 && dialogueIndex <= 27)
            return deleteDone;

        return true;
    }

    // -------------------- ACCESS --------------------

    public trackLinkedList getTrack() {
        return track;
    }

    public int getTrainSize() {
        return track.getSize();
    }

    // -------------------- RESULT TYPE --------------------

    public static class ActionResult {

        public enum Type {
            SUCCESS_ADD,
            SUCCESS_DELETE,
            SUCCESS_SEARCH,
            SUCCESS_GENERIC,
            NOT_ALLOWED,
            ERROR
        }

        private final Type type;
        private final String message;
        private final trainCar addedCar;
        private final Integer index;
        private final List<Integer> searchIndices;
        private final Integer capacity;

        private ActionResult(Type type, String message,
                             trainCar car, Integer index,
                             List<Integer> searchIndices, Integer capacity) {
            this.type = type;
            this.message = message;
            this.addedCar = car;
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

        public static ActionResult successGeneric(String msg) {
            return new ActionResult(Type.SUCCESS_GENERIC, msg, null, null, null, null);
        }

        public Type getType() { return type; }
        public String getMessage() { return message; }
        public trainCar getAddedCar() { return addedCar; }
        public Integer getIndex() { return index; }
        public List<Integer> getSearchIndices() { return searchIndices; }
        public Integer getCapacity() { return capacity; }
    }
}
