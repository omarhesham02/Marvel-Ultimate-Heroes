package model.abilities;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Damageable;

public class HealingAbility extends Ability {
	private int healAmount;
	public HealingAbility(String name, int cost, int baseCoolDown,int castRange, AreaOfEffect area, int required,int healAmount) throws Exception {
		
		super(name, cost, baseCoolDown, castRange, area, required);
		this.healAmount=healAmount;
		
	}
	
	public String toString() {
		return super.toString() + "Heal Amount: " + this.healAmount + "\n" + "\n";
	}
	
	
	public int getHealAmount() {
		return healAmount;
	}
	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}
	
	public void execute(ArrayList<Damageable> targets) {
		for (Damageable d : targets) {
			
			int hp = d.getCurrentHP() + this.getHealAmount();
			
				if (d instanceof Champion) {
					Champion c = (Champion) d;
				
					c.setCurrentHP(hp);
					
				
			}
			
			
		}
		
	}
	
	

}
