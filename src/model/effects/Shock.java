package model.effects;

import model.world.Champion;

public class Shock extends Effect {

	public Shock (int duration) {
		super("Shock", duration, EffectType.DEBUFF);
	}

	public void apply (Champion c) throws CloneNotSupportedException  {

		c.setSpeed((int) (c.getSpeed() * 0.90));
		c.setAttackDamage((int) (c.getAttackDamage() * 0.90));
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() - 1);
		c.setCurrentActionPoints(c.getCurrentActionPoints() - 1);
		
	}
	
	public void remove (Champion c) {

		c.setSpeed((int) (c.getSpeed() / 0.90));
		c.setAttackDamage((int) (c.getAttackDamage() / 0.90));
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() + 1);
		c.setCurrentActionPoints(c.getCurrentActionPoints() + 1);
	}
	
}
