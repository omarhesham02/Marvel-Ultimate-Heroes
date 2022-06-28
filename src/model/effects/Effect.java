package model.effects;

import model.world.Champion;

public abstract class Effect implements Cloneable {
	private String name;
	private int duration;
	private EffectType type;
	
	
	public Effect() {
		
	}
	
	public Effect(String name, int duration, EffectType type){
		this.name = name;
		this.duration = duration;
		this.type = type;
	}
	
	public String toString() {
		return "Name: " + this.name + "\n"
			 + "Duration: " + this.duration + "\n" 
			 + "Type: " + this.type + "\n"
			 + "\n";
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public String getName() {
		return name;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public EffectType getType() {
		return type;
	}
	
	
	public abstract void apply(Champion c);

	public abstract void remove(Champion c);
	
	
}
