package model.abilities;

import java.util.ArrayList;

import model.world.Damageable;

public abstract class Ability {
	private final String name;
	private final int manaCost;
	private final int baseCooldown;
	private int currentCooldown;
	private final int castRange;
	private final int requiredActionPoints;
	private final AreaOfEffect castArea;
	
	public Ability(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required) {
		
		this.name = name;
		this.manaCost = cost;
		this.baseCooldown = baseCoolDown;
		this.castRange = castRange;
		this.castArea = area;
		this.requiredActionPoints = required;
		this.currentCooldown = 0;
		
	}
	
	
	public String toString() {
		return "Name: " + this.name + "\n"
			+  "Mana Cost: " + this.manaCost + "\n" 
			+  "Base Cooldown: " + this.baseCooldown + "\n" 
			+  "Cast Range: " + this.castRange + "\n" 
			+  "Cast Area: " + this.castArea + "\n" 
			+  "Required Action Points: " + this.requiredActionPoints 
			+   "\n";
	}
	
	
	public String getName() {
		return name;
	}
	public int getCurrentCooldown() {
		return currentCooldown;
	}
	
	public void setCurrentCooldown(int currentCooldown) {
		
		if (currentCooldown <= 0)
			this.currentCooldown = 0;
		else this.currentCooldown = Math.min(currentCooldown, baseCooldown);
		
	}
	public int getManaCost() {
		return manaCost;
	}
	public int getBaseCooldown() {
		return baseCooldown;
	}
	public int getCastRange() {
		return castRange;
	}
	public int getRequiredActionPoints() {
		return requiredActionPoints;
	}
	public AreaOfEffect getCastArea() {
		return castArea;
	}
			
	public abstract void execute (ArrayList<Damageable> targets) throws CloneNotSupportedException;
	
	
}
