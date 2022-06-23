package model.world;

import java.awt.Point;
import java.util.ArrayList;
import model.abilities.Ability;
import model.effects.*;

public abstract class Champion implements Damageable, Comparable<Object> {

		  private String name;
	      private int maxHP;
		  private int currentHP;
	      private int mana;
		  private int maxActionPointsPerTurn;
	      private int currentActionPoints;
	      private int attackRange;
		  private int attackDamage;
		  private int speed;
		  
          private ArrayList<Ability> abilities = new ArrayList<>();
	      private ArrayList<Effect> appliedEffects = new ArrayList<>();

	      private Condition condition;
	      private Point location;
	      
	      
    public Champion (String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) throws Exception {
    	
	      this.name = name;
	      this.maxHP = maxHP;
	      this.currentHP = maxHP;
	      this.mana = mana;
	      this.maxActionPointsPerTurn = maxActions;
	      this.currentActionPoints = maxActions;
	      this.attackRange = attackRange;
	      this.attackDamage = attackDamage;
	      this.speed = speed;
	           
	      this.condition = Condition.ACTIVE;

	     
	}
    
    
    public String toString() {
    	return "Name: " + this.name + "\n"
    	+ "Location: " + this.getLocation() + "\n"
    	+ "Max HP: " + this.maxHP + "\n" 
    	+ "Current HP: " + this.currentHP + "\n"
    	+ "Mana: " + this.mana + "\n" 
    	+ "Max Action Points: " + this.maxActionPointsPerTurn  + "\n"
    	+ "Current Action Points: " + this.currentActionPoints + "\n"
    	+ "Attack Range: " + this.attackRange + "\n" 
    	+ "Attack Damage: " + this.attackDamage + "\n" 
    	+ "Speed: " + this.speed + "\n" 
    	+ "Condition: " + this.condition + "\n"
    	+ "Abilities: " + abilities.toString() + "\n"
    	+ "\n";
    }
    
	public int compareTo(Object o) {
		Champion c = (Champion) o;
		int r = c.speed - this.speed;
		
		if (r == 0)
			r = this.name.compareTo(c.name);
			
		return r;
	}
	
	

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int currentHP) {
		
		if (currentHP > maxHP)
			this.currentHP = maxHP;
		else if (currentHP < 0)
			this.currentHP = 0;
		else
			this.currentHP = currentHP;
		
	}

	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
	}

	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		if (maxActionPointsPerTurn < 0)
			return;
		
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
		
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		if (this.attackDamage < 0)
			return;
		
		this.attackDamage = attackDamage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		if(this.speed < 0)
			return;
		
		this.speed = speed;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		
		this.location = location;
		
	}

	public String getName() {
		return name;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getMana() {
		return mana;
	}
	
	public void setMana(int mana) {
		if (this.mana < 0)
			return;
		
		this.mana = mana;
	}

	public int getCurrentActionPoints() {
		return currentActionPoints;
	}
	
	public void setCurrentActionPoints(int currentActionPoints) {
		if (currentActionPoints < 0)
			this.currentActionPoints = 0;
		else if (currentActionPoints >= maxActionPointsPerTurn) 
			this.currentActionPoints = maxActionPointsPerTurn;
		else 
			this.currentActionPoints = currentActionPoints;

	}

	public int getAttackRange() {
		return attackRange;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}	

	public ArrayList<Effect> getAppliedEffects() {
		return appliedEffects;
	}
		
	
	public boolean hasEffect (String name) {
		
		for (Effect e : appliedEffects) {
			if (e.getName().equals(name))
					return true;
		}
			return false;
	}
	
	
	public void removeEffectByName (String name) throws CloneNotSupportedException {
		for (Effect e : appliedEffects) {
			if (e.getName().equals(name)) {
				e.remove(this);
				this.getAppliedEffects().remove(e);
				return;
			}
		}
	}
	

	public void removeAbilityByName(String abilityName) {
		for (Ability a : abilities) {
			if (a.getName().equals(abilityName)) {
				abilities.remove(a);
				return;
			}
		}
	}
	
	public void updateEffects () throws CloneNotSupportedException {
		ArrayList<Effect> effectsToRemove = new ArrayList<>();
		for (Effect e : getAppliedEffects()) {
			e.setDuration(e.getDuration() - 1);
			
			if (e.getDuration() <= 0)
				effectsToRemove.add(e);
		}
		
		for (Effect er : effectsToRemove) {
			removeEffectByName(er.getName());
		}
		
	}
	
	
	public void updateAbilities () {
		for (Ability a : getAbilities()) {
			if (a.getCurrentCooldown() > 0)
				a.setCurrentCooldown(a.getCurrentCooldown() - 1);
		}
	}
	
	
	public abstract void useLeaderAbility(ArrayList<Champion> targets) throws Exception;
		
}
