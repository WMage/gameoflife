/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import javax.swing.JButton;

/**
 *
 * @author WMage
 */
public final class gomb extends JButton {
	private static final long serialVersionUID = 1L;
	private int state, x, y;

	gomb(int state, int x, int y) {
		this.x = x;
		this.y = y;
		this.state(state);
	}

	public int state() {
		return state;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public void state(int s) {
		if (s > 1) {
			s = 0;
		}
		if (s == 1) {
			// in live
			this.setBackground(Color.green);
		} else {
			// none
			// this.setBackground(Color.red);
			this.setBackground(Color.white);
		}
		//this.repaint();
		//this.revalidate();
		this.state = s;
	}

	public void state_increse() {
		this.state(this.state + 1);
	}

	public void x(int x) {
		this.x = x;
	}

	public void y(int y) {
		this.y = y;
	}
}
