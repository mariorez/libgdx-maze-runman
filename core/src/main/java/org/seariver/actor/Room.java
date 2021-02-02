package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;

import java.util.ArrayList;

public class Room extends BaseActor {

    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;
    public static int[] directionArray = {NORTH, SOUTH, EAST, WEST};

    private Wall[] wallArray = new Wall[4];
    private Room[] neighborArray = new Room[4];

    private boolean connected = false;
    private boolean visited = false;
    private Room previousRoom;

    public Room(float x, float y, Stage stage) {
        super(x, y, stage);
        loadTexture("dirt.png");

        float thickness = 6;

        wallArray[SOUTH] = new Wall(x, y, getWidth(), thickness, stage);
        wallArray[WEST] = new Wall(x, y, thickness, getHeight(), stage);
        wallArray[NORTH] = new Wall(x, y + getHeight() - thickness, getWidth(), thickness, stage);
        wallArray[EAST] = new Wall(x + getWidth() - thickness, y, thickness, getHeight(), stage);
    }

    public void setNeighbor(int direction, Room neighbor) {
        neighborArray[direction] = neighbor;
    }

    public boolean hasNeighbor(int direction) {
        return neighborArray[direction] != null;
    }

    public Room getNeighbor(int direction) {
        return neighborArray[direction];
    }

    // check if wall in this direction still exists (has not been removed from stage)
    public boolean hasWall(int direction) {
        return wallArray[direction].getStage() != null;
    }

    public void removeWalls(int direction) {
        removeWallsBetween(neighborArray[direction]);
    }

    public void removeWallsBetween(Room other) {

        if (other == neighborArray[NORTH]) {
            this.wallArray[NORTH].remove();
            other.wallArray[SOUTH].remove();
        } else if (other == neighborArray[SOUTH]) {
            this.wallArray[SOUTH].remove();
            other.wallArray[NORTH].remove();
        } else if (other == neighborArray[EAST]) {
            this.wallArray[EAST].remove();
            other.wallArray[WEST].remove();
        } else { // (other == neighborArray[WEST])
            this.wallArray[WEST].remove();
            other.wallArray[EAST].remove();
        }
    }

    public void setConnected(boolean b) {
        connected = b;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean hasUnconnectedNeighbor() {

        for (int direction : directionArray) {
            if (hasNeighbor(direction) && !getNeighbor(direction).isConnected()) {
                return true;
            }
        }

        return false;
    }

    public Room getRandomUnconnectedNeighbor() {

        ArrayList<Integer> directionList = new ArrayList<>();

        for (int direction : directionArray) {
            if (hasNeighbor(direction) && !getNeighbor(direction).isConnected()) {
                directionList.add(direction);
            }
        }

        int directionIndex = (int) Math.floor(Math.random() * directionList.size());
        int direction = directionList.get(directionIndex);

        return getNeighbor(direction);
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setPreviousRoom(Room room) {
        previousRoom = room;
    }

    public Room getPreviousRoom() {
        return previousRoom;
    }

    // Used in pathfinding: locate accessible neighbors that have not yet been visited
    public ArrayList<Room> unvisitedPathList() {

        ArrayList<Room> list = new ArrayList<>();

        for (int direction : directionArray) {
            if (hasNeighbor(direction) && !hasWall(direction) && !getNeighbor(direction).isVisited()) {
                list.add(getNeighbor(direction));
            }
        }
        return list;
    }
}

