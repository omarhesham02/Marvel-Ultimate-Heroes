package model.world;

import java.awt.Point;
import java.util.Random;

public class Cover implements Damageable {
	
	      private int currentHP; // The Cover's current health points, must be >= 0
	      private Point location; // The Cover's current location in Cartesian coordinates.
	
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
		if (currentHP < 0)
			this.currentHP = 0;
		else
			this.currentHP = currentHP;
	}

	public Point getLocation() {
		return location;
	}
	
	
}


