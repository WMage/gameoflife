package engine;

/**
 *
 * @author White Mage
 */
public class generation {
	private int[][] gen;
	private int[][] maybe_reborn;

	public generation(int[][] generation) throws Exception
	{
		if((!(generation.length>0))||(!(generation[0].length>0)))
		{
			throw new Exception("Invalid generation data");
		}
		this.gen = generation;
	}
	public int[][] next_generation() {
		this.maybe_reborn = new int[this.gen.length][this.gen[0].length];
		this.mark_die();
		this.mark_reborn();
		return gen;
	}

	/*
	 * return cell live neighbors count and adds empty nbs to list what will be
	 * checked does there any new cell reborned
	 */
	private int get_nb_count(int x, int y, int x_max, int y_max) {
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
		for (x = x_start; x < x_end; x++) {
			for (y = y_start; y < y_end; y++) {
				if (this.gen[x][y] > 0) {
					count++;
				}
				else
				{
					this.maybe_reborn[x][y] = 1;
				}
			}
		}

		return count;
	}

	private void mark_die() {
		int x, y, x_max, y_max;
		x_max = this.gen.length;
		for (x = 0; x < x_max; x++) {
			y_max = this.gen[x].length;
			for (y = 0; y < y_max; y++) {
				int nb_count = this.get_nb_count(x, y, x_max, y_max);
				if ((nb_count > 3) || (nb_count < 2)) {
					this.gen[x][y] = 10;
				}
			}
		}
	}

	private void mark_reborn() {

	}

}
