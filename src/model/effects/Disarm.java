package model.effects;

import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect {
	
	public Disarm (int duration) {
		super("Disarm", duration, EffectType.DEBUFF);
	}
	
	public void apply (Champion c) {
		
			try {
				c.getAbilities().add(new DamagingAbility("Punch", 0, 1, 1, AreaOfEffect.SINGLETARGET, 1, 50));
			} catch (Exception e) {
				
			}
		
	}

	public void remove (Champion c) {
	
		c.removeAbilityByName("Punch");
	}

}
