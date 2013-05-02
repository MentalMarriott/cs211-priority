package uk.ac.aber.rcs.cs211.schedulersim.gui;

import java.awt.*;
import java.lang.reflect.*;
import javax.swing.*;

import uk.ac.aber.rcs.cs211.schedulersim.*;

public class QueuePanel extends JPanel {
	
	public static final long serialVersionUID=0;

	JTextArea ta;
	Method method;
	
	
	
	public QueuePanel(String title, String methodName) {
		method = getMethod("uk.ac.aber.rcs.cs211.schedulersim.Job",
							methodName);
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(),
				title));
		ta = new JTextArea("");
		ta.setMinimumSize(new Dimension(110,125));
		ta.setPreferredSize(new Dimension(110,125));
		
		ta.setLineWrap(true);
		ta.setEditable(false);
		
		this.add(ta,"Center");
	}

	private Method getMethod(String className, String methodName) {
		Method method=null;
		Class job=null;
		try {
			job = Class.forName(className);
		} catch (ClassNotFoundException e) {
			System.err.println("Erk, can't find the Job class");
			System.exit(0);
		}
		
		try {
			method = job.getMethod(methodName);
		} catch (NoSuchMethodException e) {
			System.err.println("Erk, can't find the method "+methodName);
			System.exit(0);			
		}
		return method;
	}
	
	
	public void update(Job[] contents) {
//		System.out.println("QueuePanel.update called "+method.getName()+" with "+contents.length);
		StringBuffer content=new StringBuffer();
		Object temp=null;
		for (int i=0; i<contents.length; i++) {
			content.append(contents[i].getName());
			content.append(":");
			content.append(contents[i].getPriority());
			content.append(" ");
			try {
				temp = method.invoke(contents[i]);
			} catch (IllegalAccessException e) {
				System.err.println("Erk!");
			} catch (InvocationTargetException e) {
				System.err.println("Eek!");
			}
			content.append(temp);
			
			content.append("\n");
		}
		ta.setText(content.toString());
//		System.out.println(content);
	}
}

