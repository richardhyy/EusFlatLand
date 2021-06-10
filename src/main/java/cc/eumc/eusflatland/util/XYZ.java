/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Nordic.
 * 
 * Nordic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Nordic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nordic If not, see <http://www.gnu.org/licenses/>.
 */
package cc.eumc.eusflatland.util;

import org.bukkit.Location;

import java.util.Objects;

/**
 * Used for fast storage, comparison, and recall of block positions. Mutable to
 * avoid creating new objects for simple comparison.
 *
 * @author Nightgunner5
 */
public class XYZ {
	public int x;
	public int y;
	public int z;

	public XYZ(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public XYZ() {
	}

	public XYZ(Location location) {
		this.x = location.getBlockX();
		this.y = location.getBlockY();
		this.z = location.getBlockZ();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final XYZ xyz = (XYZ) o;
		return x == xyz.x &&
				y == xyz.y &&
				z == xyz.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public String toString() {
		return String.format("%s, %s, %s", x, y, z);
	}
}