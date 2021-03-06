package es.uniovi.eii.sdm.favmovies.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

	private static Retrofit instance = null;

	private RetrofitClient() {}

	public static Retrofit getClient(String baseUrl) {
		if (instance == null)
			instance = new Retrofit.Builder()
					.baseUrl(baseUrl)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		return instance;
	}
}
