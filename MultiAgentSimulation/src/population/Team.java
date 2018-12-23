package population;

import java.awt.Color;

public class Team {

	private int code;
	private Color color;
	private boolean predator;

	public Team(int code, Color color, boolean predator) {
		super();
		this.code = code;
		this.color = color;
		this.predator = predator;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isPredator() {
		return predator;
	}

	public void setPredator(boolean predator) {
		this.predator = predator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (code != other.code)
			return false;
		return true;
	}

}
