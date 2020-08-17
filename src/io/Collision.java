package io;

import templates.*;
import templates.AABB.*;

public class Collision {
	private Model first, second;
	private Edge firstEdge, secondEdge;
	private boolean isColliding = false;
	
	public Collision setCollision(boolean isColliding) {
		this.isColliding = isColliding;
		return this;
	}
	
	public Collision setFirst(Model first, Edge firstEdge) {
		this.first = first;
		this.firstEdge = firstEdge;
		return this;
	}
	
	public Collision setSecond(Model second, Edge secondEdge) {
		this.second = second;
		this.secondEdge = secondEdge;
		return this;
	}
	
	public Model getFirst() { return first; }
	public Model getSecond() { return second; }
	public Edge getFirstEdge() { return firstEdge; }
	public Edge getSecondEdge() { return secondEdge; }
	public boolean isColliding() { return isColliding; }
}