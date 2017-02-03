package api;

import org.json.*;

public class jsonArray {
	public static int[][] json_to_generation(String json) throws JSONException {
		final JSONObject obj = new JSONObject(json);
		final JSONArray gen = obj.getJSONArray("generation");
		int x_len = obj.getInt("x_len");
		int y_len = obj.getInt("y_len");
		int[][] generation = new int[x_len][y_len];
		for (int i = 0; i < x_len; ++i) {
			JSONArray line = gen.getJSONArray(i);
			for (int h = 0; h < y_len; ++h) {
				generation[i][h] = line.getInt(h);
			}
		}
		return generation;
	}

	public static String json_to_generation(int[][] generation) throws JSONException {

		JSONArray mJSONArray = new JSONArray(generation);
		return "{" + "\"x_len\": " + generation.length + ", \"y_len\": " + generation[0].length + ", \"generation\": "
				+ mJSONArray.toString() + "}";
	}
}
