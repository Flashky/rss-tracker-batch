package com.flashk.bots.rsstracker.repositories.entities;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemEntity {

	// Required fields at RSS specification
	// At least one of these fields must exist.
	private String title;
	private String link;
	private String description;

	@Override
	public int hashCode() {
		return Objects.hash(link, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemEntity other = (ItemEntity) obj;
		return Objects.equals(link, other.link) && Objects.equals(title, other.title);
	}

	

	
}

