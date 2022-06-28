package model.world;

import java.awt.Point;
import java.util.Random;

public class Cover implements Damageable {
	
	      private int currentHP; // The Cover's current health points, must be >= 0
	      private final Point location; // The Cover's current location in Cartesian coordinates.
	
	public Cover(int x, int y) {
		
		Random rand = new Random();
		
		this.location = new Point(x, y);
		this.currentHP = rand.nextInt(100, 1000);
		
	}
	
	public String toString() {
		return "HP: " + currentHP + "\n"
			 + "Location: " + location + "\n";
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int currentHP) {
		this.currentHP = Math.max(currentHP, 0);
	}

	public Point getLocation() {
		return location;
	}
	
	
}


