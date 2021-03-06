/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.*;
import java.awt.event.*;
import engine.*;
//import java.util.Random;
import javax.swing.*;

import org.json.JSONException;
//import javax.swing.border.Border;

/**
 *
 * @author WMage
 */
public class gui extends javax.swing.JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	/**
	 * Creates new form gui
	 */
	// JPanel pane = new JPanel();
	protected int x_len = 50, y_len = 40;
	private gomb[][] table_buttons;
	private int[][] table;
	private final int button_sidelenght = 18;
	private final int top_base = 0, left_alap = 0;
	private int _left = left_alap, _top = top_base;
	@SuppressWarnings("unused")
	private boolean progress;

	/*
	 * List, Map, Set
	 * 
	 */

	private int left() {
		int ret = _left;
		_left += button_sidelenght;
		return ret;
	}

	private int top() {
		int ret = _top;
		_top += button_sidelenght;
		return ret;
	}

	public gui() {
		super("Game of Life");
		setBounds(10, 10, 500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponents();
		Container con = this.getContentPane(); // inherit main frame
		con.add(pane); // JPanel containers default to FlowLayout
		setVisible(true); // make frame visible
		pane.setLayout(null);
		setBackground(Color.yellow);
		reset_table_data();
	}

	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		gomb button = ((gomb) s);
		button.state_increse();
		this.table[button.x()][button.y()] = button.state();
	}

	private void start_game() {
		if (table_buttons != null) {
			for (gomb[] tabla_gombok1 : table_buttons) {
				for (gomb tabla_gombok11 : tabla_gombok1) {
					tabla_gombok11.removeActionListener(this);
				}
			}
		}
		// start anim
		this.play();
	}

	private void play() {
		this.progress = true;
		try {
			// ciklussal elsz�ll
			// do {
			generation gen = new generation(this.table);
			this.table = gen.next_generation();
			this.repaint_table();
			// Thread.sleep(100);
			// } while (progress);
		} catch (Exception e) {
			msg(e.getMessage());
		}
	}

	private void repaint_table() {
		int x, y;
		for (x = 0; x < x_len; x++) {
			for (y = 0; y < y_len; y++) {
				this.table_buttons[x][y].state(this.table[x][y]);
				this.table[x][y] = this.table_buttons[x][y].state();
			}
		}
	}

	private void fill_buttons() {
		table_buttons = new gomb[table.length][table[0].length];
		for (int x = 0; x < table.length; x++) {
			for (int y = 0; y < table[x].length; y++) {
				add_button(table[x][y], x, y);
			}
			top();
			_left = left_alap;
		}

		for (gomb[] tabla_gombok1 : table_buttons) {
			for (gomb tabla_gombok11 : tabla_gombok1) {
				tabla_gombok11.addActionListener(this);
				tabla_gombok11.requestFocus();
				tabla_gombok11.repaint();
			}
		}
	}

	private int[][] new_empty_table(int x, int y) {
		int[][] fields = new int[x][y];
		for (x = 0; x < fields.length; x++) {
			for (y = 0; y < fields[x].length; y++) {
				fields[x][y] = 0;
			}
		}
		return fields;
	}

	private void add_button(int state, int x, int y) {
		if (table_buttons[x][y] == null) {
			gomb pressme = new gomb(state, x, y);
			// pressme.setMnemonic('P'); // associate hotkey to button
			// pressme.addActionListener(this); // register button listener
			pressme.setMargin(new Insets(0, 0, 0, 0));
			pressme.setBounds(left(), _top, button_sidelenght, button_sidelenght);
			pane.add(pressme);
			pressme.setVisible(true);
			// pressme.requestFocus();
			table_buttons[x][y] = pressme;
			pressme.setEnabled(true);
			// pressme.repaint();
		}
		// requestFocus();
	}

	private void reset_table_data() {
		this.progress = false;
		if (table_buttons != null) {
			for (gomb[] tabla_gombok1 : table_buttons) {
				for (gomb tabla_gombok11 : tabla_gombok1) {
					pane.remove(tabla_gombok11);
					tabla_gombok11.removeAll();
				}
			}
			table_buttons = null;
		}
		if (y_len < x_len) {
			int v = x_len;
			x_len = y_len;
			y_len = v;
		}
		this.table = this.new_empty_table(x_len, y_len);
		_left = left_alap;
		_top = top_base;
		pane.setEnabled(true);
		int y = (top_base * 2) + (x_len * button_sidelenght);
		int x = (left_alap * 2) + (y_len * button_sidelenght);
		pane.setSize(x, y);
		this.setSize((x + 40), (y + 100));
		fill_buttons();
	}

	private void save_table() {
		String msg = "siker";
		try {
			boolean res = api.communicator.save_object("asd",
					(api.jsonArray.generation_to_jsonstring(this.table)));
			if (!res) {
				msg = "siekrtelen";
			}
		} catch (JSONException e) {
			msg("hiba");
			msg(e.getMessage());
			e.printStackTrace();
		}
		msg(msg);
	}

	private void load_table(String name) {
		try {
			this.table = api.jsonArray.jsonstring_to_generation(api.communicator.get_saved_object(name));
			//TODO: a bet�lt�tt alakzat f�ggetlen legyen az aktu�lis t�blam�rett�l
			this.repaint();
			//TODO: bet�lti az adatokat, de valami�rt grafikusan nem jeleniti meg amit a this.table �tvett
			//�rt�keit akkor sem az eredeti t�bla megegyezett az ujjal
		} catch (JSONException e) {
			msg(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void msg(String t) {
		JOptionPane.showMessageDialog(null, t, "Message Dialog", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		pane = new javax.swing.JPanel();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenu2 = new javax.swing.JMenu();
		jMenu3 = new javax.swing.JMenu();
		jMenu4 = new javax.swing.JMenu();
		jMenu5 = new javax.swing.JMenu();
		jMenu6 = new javax.swing.JMenu();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setBounds(new java.awt.Rectangle(0, 0, 0, 0));
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		pane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
				java.awt.Color.gray, java.awt.Color.darkGray, java.awt.Color.white, java.awt.Color.black));
		pane.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

		javax.swing.GroupLayout paneLayout = new javax.swing.GroupLayout(pane);
		pane.setLayout(paneLayout);
		paneLayout.setHorizontalGroup(paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 433, Short.MAX_VALUE));
		paneLayout.setVerticalGroup(paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0,
				232, Short.MAX_VALUE));

		jMenu1.setText("Start / Step");
		jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jMenu1MouseClicked(evt);
			}
		});
		jMenuBar1.add(jMenu1);

		jMenu2.setText("Reset");
		jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jMenu2MouseClicked(evt);
			}
		});
		jMenuBar1.add(jMenu2);

		jMenu5.setText("LIF");
		jMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jMenu5MouseClicked(evt);
			}
		});
		jMenuBar1.add(jMenu5);

		jMenu3.setText("Glider");
		jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jMenu3MouseClicked(evt);
			}
		});
		jMenuBar1.add(jMenu3);

		jMenu4.setText("LWSS");
		jMenu4.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jMenu4MouseClicked(evt);
			}
		});
		jMenuBar1.add(jMenu4);

		jMenu6.setText("Save");
		jMenu6.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jMenu6MouseClicked(evt);
			}
		});
		jMenuBar1.add(jMenu6);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE))
								.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(pane,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addGap(18, 18, 18).addComponent(pane,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jMenu2MouseClicked
		this.reset_table_data();
	}// GEN-LAST:event_jMenu2MouseClicked

	private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jMenu1MouseClicked
		this.start_game();
	}// GEN-LAST:event_jMenu1MouseClicked

	private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jMenu1MouseClicked
		this.load_table("glinder");
	}// GEN-LAST:event_jMenu1MouseClicked

	private void jMenu4MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jMenu1MouseClicked
		this.load_table("LWSS");
	}// GEN-LAST:event_jMenu1MouseClicked

	private void jMenu5MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jMenu1MouseClicked
		this.load_table("LIF");
	}// GEN-LAST:event_jMenu1MouseClicked

	private void jMenu6MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jMenu1MouseClicked
		this.save_table();
	}// GEN-LAST:event_jMenu1MouseClicked

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting
		// code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.
		 * html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		/*
		 * java.awt.EventQueue.invokeLater(new Runnable() { public void run() {
		 * gui g = new gui(); g.setVisible(true); } });
		 */
		new gui();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenu jMenu2;
	private javax.swing.JMenu jMenu3;
	private javax.swing.JMenu jMenu4;
	private javax.swing.JMenu jMenu5;
	private javax.swing.JMenu jMenu6;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JPanel pane;
	// End of variables declaration//GEN-END:variables
}
