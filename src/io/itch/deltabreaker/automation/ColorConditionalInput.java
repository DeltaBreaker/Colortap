package io.itch.deltabreaker.automation;

import java.awt.Color;
import java.awt.Point;

public class ColorConditionalInput {

	private Color color;
	private Point location;
	
	public ColorConditionalInput(Color color, Point location) {
		this.color = color;
		this.location = location;
	}

	public Color getColor() {
		return color;
	}
	
	public int getRed() {
		return color.getRed();
	}
	
	public int getGreen() {
		return color.getGreen();
	}
	
	public int getBlue() {
		return color.getBlue();
	}
	
	public int getX() {
		return location.x;
	}
	
	public int getY() {
		return location.y;
	}
	
}
