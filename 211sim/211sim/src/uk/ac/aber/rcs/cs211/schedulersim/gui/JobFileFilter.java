package uk.ac.aber.rcs.cs211.schedulersim.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class JobFileFilter extends FileFilter {

	public JobFileFilter() {
	
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 &&  i < s.length() - 1) {
			ext = s.substring(i+1).toLowerCase();
		}
		if (ext != null) {
			if (ext.equals("jobs")) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	public String getDescription() {
		return "Simulator job data files";
	}

}
