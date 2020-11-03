package es.uniovi.eii.sdm.favmovies.model;

public class MovieStaff {

	private int idStaff;
	private int idMovie;
	private String character;

	public MovieStaff() {
	}

	public MovieStaff(int idStaff, int idMovie, String character) {
		this.idStaff = idStaff;
		this.idMovie = idMovie;
		this.character = character;
	}

	public int getIdStaff() {
		return idStaff;
	}

	public void setIdStaff(int idStaff) {
		this.idStaff = idStaff;
	}

	public int getIdMovie() {
		return idMovie;
	}

	public void setIdMovie(int idMovie) {
		this.idMovie = idMovie;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}
}