package population;

public class Cell {

	private boolean populated;
	private Team team;

	public Cell(boolean populated) {
		super();
		this.populated = populated;
	}
	
	public Cell(boolean populated, Team team) {
		super();
		this.populated = populated;
		this.team = team;
	}

	public boolean isPopulated() {
		return populated;
	}

	public void setPopulated(boolean populated) {
		this.populated = populated;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}
