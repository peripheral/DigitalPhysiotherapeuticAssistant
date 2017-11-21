package com.app.entities;

import com.app.db.Attribute;

public class Entity {
	@Attribute(columnName = "name", type = "text")
	protected String name ="Posture name not set";
	@Attribute(columnName = "id", type = "long")
	protected long id = -1;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return name;
	}

}
