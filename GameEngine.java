import java.util.*;

public class GameEngine {

    @SuppressWarnings("FieldMayBeFinal")
    private Player player;
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Room> map;
    @SuppressWarnings("FieldMayBeFinal")
    private Queue<String> hintQueue;
    private int turnCount;
    @SuppressWarnings("FieldMayBeFinal")
    private Scanner sc;

    public GameEngine(Player p, ArrayList<Room> map) {
        this.player = p;
        this.map = map;
        hintQueue = new LinkedList<>();
        sc = new Scanner(System.in);
    }

    @SuppressWarnings("UseSpecificCatch")
    public void start() {
        System.out.println("=== ESCAPE ROOM GAME ===");

        while (true) {
            try {
                printStatus();
                String cmd = sc.nextLine();
                processCommand(cmd);
                turnCount++;

                if (turnCount % 3 == 0 && !hintQueue.isEmpty()) {
                    System.out.println("HINT: " + hintQueue.poll());
                }

                if (winConditionCheck())
                    break;

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void processCommand(String cmd)
            throws InvalidCommandException, LockedRoomException {

        String[] parts = cmd.split(" ");

        switch (parts[0]) {
            case "look" -> player.getCurrentRoom().inspect();

            case "move" -> {
                for (Room r : player.getCurrentRoom().getConnectedRooms()) {
                    if (r.getName().equalsIgnoreCase(parts[1])) {
                        player.moveTo(r);
                        return;
                    }
                }
                throw new InvalidCommandException("Room not found");
            }

            case "back" -> player.goBack();

            case "inventory" -> {
                for (Item i : player.getInventory())
                    i.inspect();
            }

            case "map" -> map.get(0).exploreRecursive(0);

            case "pickup" -> {
                if (parts.length < 2) {
                    throw new InvalidCommandException("Pickup what?");
                }
                
                boolean found = false;
                Iterator<GameComponent> it =
                        player.getCurrentRoom().getContents().iterator();
                
                while (it.hasNext()) {
                    GameComponent gc = it.next();
                    
                    if (gc instanceof Item &&
                            gc.getName().equalsIgnoreCase(parts[1])) {
                        
                        Item item = (Item) gc;
                        item.collect(player);
                        it.remove(); // xóa item khỏi phòng
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    throw new InvalidCommandException("Item not found");
                }       }


            default -> throw new InvalidCommandException("Unknown command");
        }
    }

    private void printStatus() {
        System.out.println("\nCurrent room: " +
                player.getCurrentRoom().getName());
        System.out.print("> ");
    }

    private boolean winConditionCheck() {
        if (player.getCurrentRoom().isExit()) {
            System.out.println("🎉 YOU ESCAPED!");
            return true;
        }
        return false;
    }
}
