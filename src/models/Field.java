package models;

import enums.Solid;
import main.Model;
import main.Rect;
import render.Texture;

public class Field extends Model {
	public Field(int width, int height) {
		super(new Rect(0, 0, width, height), new Texture("field"), Solid.REVERSED_SOLID);
	}
}