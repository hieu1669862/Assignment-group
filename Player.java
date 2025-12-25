import java.util.ArrayList;
import java.util.Stack;

public class Player {

    @SuppressWarnings("FieldMayBeFinal")
    private Stack<Room> moveHistory;
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Item> inventory;
    private Room currentRoom;

    public Player(Room start) {
        currentRoom = start;
        moveHistory = new Stack<>();
        inventory = new ArrayList<>();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void moveTo(Room r) throws LockedRoomException {
        if (r.requiresKey() && !hasKey(r.getRequiredKey())) {
            throw new LockedRoomException("Room locked! Need key: " + r.getRequiredKey());
        }
        moveHistory.push(currentRoom);
        currentRoom = r;
    }

    public void goBack() {
        if (!moveHistory.isEmpty())
            currentRoom = moveHistory.pop();
    }

    public boolean hasKey(String keyName) {
        for (Item i : inventory) {
            if (i.getName().equalsIgnoreCase(keyName))
                return true;
        }
        return false;
    }
}
