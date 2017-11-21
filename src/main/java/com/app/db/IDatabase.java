package com.app.db;

import com.app.entities.Entity;

public interface IDatabase {
	public Entity create(Entity e);
	public Entity read(Entity e);
	public Entity update(Entity e);
	public void delete(Entity e);
}
