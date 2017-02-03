package engine;

import org.json.JSONException;

/**
 *
 * @author WMage
 */
public class generation {
	private int[][] gen;
	private int[][] maybe_reborn;

	public generation(int[][] generation) throws Exception {
		if ((!(generation.length > 0)) || (!(generation[0].length > 0))) {
			throw new Exception("Invalid generation data");
		}
		this.gen = generation;
	}
	public void parse_json_generation_in(String json) throws JSONException
	{
		this.gen = api.jsonArray.jsonstring_to_generation(json);
	}
	public String parse_json_nextgeneration_out() throws JSONException
	{
		return api.jsonArray.generation_to_jsonstring(this.next_generation());
	}

	public int[][] next_generation() {
		this.maybe_reborn = new int[this.gen.length][this.gen[0].length];
		this.mark_die();
		this.mark_reborn();
		this.execute_marks();
		return this.gen;
	}

	/*
	 * return cell live neighbors count and adds empty nbs to list what will be
	 * checked does there any new cell reborned
	 */
	private int get_nb_count(int x, int y, int x_max, int y_max) {
		boolean count_itself = (this.gen[x][y] == 1);

		int x_start = x - 1;
		if (x_start < 0) {
			x_start = 0;
		}
		int y_start = y - 1;
		if (y_start < 0) {
			y_start = 0;
		}
		int x_end = x + 1;
		if (x_end > x_max) {
			x_end = x_max;
		}
		int y_end = y + 1;
		if (y_end > y_max) {
			y_end = y_max;
		}

		int count = 0;
		for (x = x_start; x <= x_end; x++) {
			for (y = y_start; y <= y_end; y++) {
				if (this.gen[x][y] > 0) {
					count++;
				} else {
					if (count_itself) {
						this.maybe_reborn[x][y] = 1;
					}
				}
			}
		}
		return (count_itself) ? (count - 1) : (count);
	}

	private void mark_die() {
		int x, y, x_max, y_max;
		x_max = this.gen.length;
		for (x = 0; x < x_max; x++) {
			y_max = this.gen[x].length;
			for (y = 0; y < y_max; y++) {
				if (this.gen[x][y] == 1) {
					int nb_count = this.get_nb_count(x, y, x_max, y_max);
					if ((nb_count > 3) || (nb_count < 2)) {
						this.gen[x][y] = 10;
					}
				}
			}
		}
	}

	private void mark_reborn() {
		int x, y, x_max, y_max;
		x_max = this.gen.length - 1;
		for (x = 0; x < x_max; x++) {
			y_max = this.gen[x].length - 1;
			for (y = 0; y < y_max; y++) {
				if (this.maybe_reborn[x][y] == 1) {
					int nb_count = this.get_nb_count(x, y, x_max, y_max);
					if (nb_count == 3) {
						this.gen[x][y] = -20;
					}
				}
			}
		}
	}

	private void execute_marks() {
		int x, y;
		for (x = 0; x < this.gen.length; x++) {
			for (y = 0; y < this.gen[x].length; y++) {
				switch (this.gen[x][y]) {
					case 10:
						// die
						this.gen[x][y] = 0;
						break;
					case -20:
						// born
						this.gen[x][y] = 1;
						break;
				}
			}
		}
	}

}
