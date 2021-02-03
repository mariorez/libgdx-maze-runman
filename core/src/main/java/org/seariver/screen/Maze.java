package org.seariver.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;
import org.seariver.actor.Room;

import java.util.ArrayList;

public class Maze {

    // maze size constants
    private final int roomCountX = 12;
    private final int roomCountY = 10;
    private final int roomWidth = 64;
    private final int roomHeight = 64;

    private Room[][] roomGrid = new Room[roomCountX][roomCountY];

    public Maze(Stage stage) {

        for (int gridY = 0; gridY < roomCountY; gridY++) {
            for (int gridX = 0; gridX < roomCountX; gridX++) {
                float pixelX = gridX * roomWidth;
                float pixelY = gridY * roomHeight;
                Room room = new Room(pixelX, pixelY, stage);
                roomGrid[gridX][gridY] = room;
            }
        }

        // neighbor relations
        for (int gridY = 0; gridY < roomCountY; gridY++) {
            for (int gridX = 0; gridX < roomCountX; gridX++) {
                Room room = roomGrid[gridX][gridY];
                if (gridY > 0)
                    room.setNeighbor(Room.SOUTH, roomGrid[gridX][gridY - 1]);
                if (gridY < roomCountY - 1)
                    room.setNeighbor(Room.NORTH, roomGrid[gridX][gridY + 1]);
                if (gridX > 0)
                    room.setNeighbor(Room.WEST, roomGrid[gridX - 1][gridY]);
                if (gridX < roomCountX - 1)
                    room.setNeighbor(Room.EAST, roomGrid[gridX + 1][gridY]);
            }
        }

        // Depth-First Algorithm
        ArrayList<Room> activeRoomList = new ArrayList<>();

        Room currentRoom = roomGrid[0][0];
        currentRoom.setConnected(true);
        activeRoomList.add(0, currentRoom);

        /* chance of returning to a random connected room
           to create a branching path from that room */
        float branchProbability = 0.5f;

        while (activeRoomList.size() > 0) {

            if (Math.random() < branchProbability) {
                // get random previously visited room
                int roomIndex = (int) (Math.random() * activeRoomList.size());
                currentRoom = activeRoomList.get(roomIndex);
            } else {
                // get the most recently visited room
                currentRoom = activeRoomList.get(activeRoomList.size() - 1);
            }

            if (currentRoom.hasUnconnectedNeighbor()) {
                Room nextRoom = currentRoom.getRandomUnconnectedNeighbor();
                currentRoom.removeWallsBetween(nextRoom);
                nextRoom.setConnected(true);
                activeRoomList.add(0, nextRoom);
            } else {
                // this room has no more adjacent unconnected rooms
                //so there is no reason to keep it in the list
                activeRoomList.remove(currentRoom);
            }
        }

        int wallsToRemove = 24;

        while (wallsToRemove > 0) {
            int gridX = (int) Math.floor(Math.random() * roomCountX);
            int gridY = (int) Math.floor(Math.random() * roomCountY);
            int direction = (int) Math.floor(Math.random() * 4);
            Room room = roomGrid[gridX][gridY];

            if (room.hasNeighbor(direction) && room.hasWall(direction)) {
                room.removeWalls(direction);
                wallsToRemove--;
            }
        }
    }

    public Room getRoom(int gridX, int gridY) {
        return roomGrid[gridX][gridY];
    }

    public Room getRoom(BaseActor actor) {
        int gridX = Math.round(actor.getX() / roomWidth);
        int gridY = Math.round(actor.getY() / roomHeight);
        return getRoom(gridX, gridY);
    }

    public void resetRooms() {
        for (int gridY = 0; gridY < roomCountY; gridY++) {
            for (int gridX = 0; gridX < roomCountX; gridX++) {
                roomGrid[gridX][gridY].setVisited(false);
                roomGrid[gridX][gridY].setPreviousRoom(null);
            }
        }
    }
}
