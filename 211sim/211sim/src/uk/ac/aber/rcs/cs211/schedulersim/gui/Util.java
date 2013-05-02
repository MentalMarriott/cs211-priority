package uk.ac.aber.rcs.cs211.schedulersim.gui;

import java.awt.GridBagConstraints;

public class Util {
	public static GridBagConstraints makeGBC(int x, int y, int w, int h) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=x;
		gbc.gridy=y;
		gbc.gridwidth=w;
		gbc.gridheight=h;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.anchor=GridBagConstraints.NORTHWEST;
		
		return gbc;
	}

}
