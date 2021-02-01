package org.seariver.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.actor.Room;

public class Maze {

    private Room[][] roomGrid;

    // maze size constants
    private final int roomCountX = 12;
    private final int roomCountY = 10;
    private final int roomWidth = 64;
    private final int roomHeight = 64;

    public Maze(Stage stage) {

        roomGrid = new Room[roomCountX][roomCountY];

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
    }

    public Room getRoom(int gridX, int gridY) {
        return roomGrid[gridX][gridY];
    }
}
