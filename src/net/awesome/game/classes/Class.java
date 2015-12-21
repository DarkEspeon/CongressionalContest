package net.awesome.game.classes;

public class Class {
	
	public static Class warrior = new Class("Warrior", "", 0, 0, 0, 0, 0, 0);
	
	private String name;
	private String mpName;
	
	private int baseHP;
	private int baseMP;
	private int baseSpeed;
	private int baseDamage;
	private int baseMagicDamage;
	private int baseRangeDamage;
	
	public Class(String name, String mpName, int baseHP, int baseMP, int baseSpeed, int baseDamage, int baseMagicDamage, int baseRangeDamage){
		this.name = name;
		this.mpName = mpName;
		this.baseHP = baseHP;
		this.baseMP = baseMP;
		this.baseSpeed = baseSpeed;
		this.baseDamage = baseDamage;
		this.baseMagicDamage = baseMagicDamage;
		this.baseRangeDamage = baseRangeDamage;
	}
	
	public String getName(){return name;}
	public String getMPName(){return mpName;}
	public int getBaseHP(){return baseHP;}
	public int getBastMP(){return baseMP;}
	public int getBaseSpeed(){return baseSpeed;}
	public int getBaseDamage(){return baseDamage;}
	public int getBaseMagicDamage(){return baseMagicDamage;}
	public int getBaseRangeDamage(){return baseRangeDamage;}
}
