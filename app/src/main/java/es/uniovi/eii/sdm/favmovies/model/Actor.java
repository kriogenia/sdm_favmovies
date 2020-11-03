package es.uniovi.eii.sdm.favmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Actor implements Parcelable {

	private int id;
	private String name;
	private String image;
	private String urlImdb;

	private String character;

	public Actor() {
		this.id = -1;
		this.name = "";
		this.image = "";
		this.urlImdb = "";
		this.character = "";
	}

	public Actor(int id, String name, String image, String urlImdb) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.urlImdb = urlImdb;
	}

	public Actor(String name, String character, String image, String urlImdb) {
		this.name = name;
		this.image = image;
		this.urlImdb = urlImdb;
		this.character = character;
	}

	protected Actor(Parcel in) {
		id = in.readInt();
		name = in.readString();
		image = in.readString();
		urlImdb = in.readString();
		character = in.readString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrlImdb() {
		return urlImdb;
	}

	public void setUrlImdb(String urlImdb) {
		this.urlImdb = urlImdb;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(image);
		dest.writeString(urlImdb);
		dest.writeString(character);
	}

	public static final Creator<Actor> CREATOR = new Creator<Actor>() {
		@Override
		public Actor createFromParcel(Parcel in) {
			return new Actor(in);
		}

		@Override
		public Actor[] newArray(int size) {
			return new Actor[size];
		}
	};
}
