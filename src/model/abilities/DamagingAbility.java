package model.abilities;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Damageable;

public class DamagingAbility extends Ability {
	private int damageAmount;
	public DamagingAbility(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required,int damageAmount) throws Exception {
		super(name, cost, baseCoolDown, castRange, area, required);
		this.damageAmount = damageAmount;
	}
	
	public String toString() {
		return super.toString() + "Damage Amount: " + this.damageAmount + "\n" + "\n";
	}
	
	
	public int getDamageAmount() {
		return damageAmount;
	}
	
	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}

	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException {
		for (Damageable d : targets) {
			
			if (d instanceof Champion) {
				Champion c = (Champion) d;
				if (c.hasEffect("Shield")) {
					c.removeEffectByName("Shield");
					return;
				}
			}
			
			int hp = d.getCurrentHP() - this.getDamageAmount();
			
			if (hp <= 0)
				d.setCurrentHP(0);
			else
				d.setCurrentHP(d.getCurrentHP() - this.getDamageAmount());
		}
		
	}
	

}
