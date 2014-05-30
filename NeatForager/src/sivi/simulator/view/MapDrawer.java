package sivi.simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MapDrawer extends JFrame {

	// Info about the map
	public static final int PREFERRED_GRID_SIZE_PIXELS = 10;

	private Color[][] colorGrid;

	private int numRows;
	private int numCols;
	private MapPanel panel;

	public MapDrawer(Color[][] map) {
		// Read values
		this.colorGrid = map;
		this.numCols = map.length;
		this.numRows = map[0].length;

		// Set size of map
		int preferredWidth = numCols * PREFERRED_GRID_SIZE_PIXELS;
		int preferredHeight = numRows * PREFERRED_GRID_SIZE_PIXELS;
		setPreferredSize(new Dimension(preferredWidth, preferredHeight));

		// Add MapPanel
		panel = new MapPanel();
		add(panel);
	}

	/**
	 * Updates the colorGrid Has to be called before .repaint() Else no effect
	 * on the map
	 * 
	 * @param newColorGrid
	 */
	public void updateColorGrid(Color[][] newColorGrid) {
		this.colorGrid = newColorGrid;
		this.panel.revalidate();
		this.panel.repaint();
	}

	class MapPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			// Clear the map
			g.clearRect(0, 0, getWidth(), getHeight());

			// Draw the grid
			int tileWidth = getWidth() / numCols;
			int tileHeight = getHeight() / numRows;

			for (int i = 0; i < numRows; i++) {
				for (int j = 0; j < numCols; j++) {
					// Upper left corner of the tile
					int x = i * tileWidth;
					int y = j * tileHeight;
					Color tileColor = colorGrid[i][j];
					g.setColor(tileColor);
					g.fillRect(x, y, tileWidth, tileHeight);
				}
			}
		}
	}

}
