package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {

	public PowerUp (int duration) {
		super("PowerUp", duration, EffectType.BUFF);
	}
	
	
	public void apply (Champion c) throws CloneNotSupportedException {
		
		ArrayList<Ability> abilities = c.getAbilities();
		
		for (Ability a : abilities) {
			if (a instanceof DamagingAbility) {
				DamagingAbility da = (DamagingAbility) a;
				da.setDamageAmount((int) Math.round((da.getDamageAmount() * 1.2)));
				
			} else if (a instanceof HealingAbility) {
				HealingAbility ha = (HealingAbility) a;
				ha.setHealAmount( (int) Math.round((ha.getHealAmount() * 1.2)));
			}
				
		}
		
		
	}
	
	public void remove (Champion c) {
	
		ArrayList<Ability> abilities = c.getAbilities();
		
		for (Ability a : abilities) {
			if (a instanceof DamagingAbility) {
				DamagingAbility da = (DamagingAbility) a;
				da.setDamageAmount( (int) Math.round((da.getDamageAmount() / 1.2)));
				
			} else if (a instanceof HealingAbility) {
				HealingAbility ha = (HealingAbility) a;
				ha.setHealAmount( (int) Math.round((ha.getHealAmount() / 1.2)));
			}
			

				
		}
		
	}
	

}
