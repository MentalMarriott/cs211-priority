package uk.ac.aber.rcs.cs211.schedulersim;

import java.io.*;
import java.util.*;

public class SimulatorLoader {

	private File file;
	private ArrayList<Job> queue;
	
	private SimulatorLoader (String filename, ArrayList<Job> queue) {
		this.file=new File(filename);
		this.queue=queue;
	}
	
	private void load() throws FileNotFoundException, IOException {
		
		if (!file.exists()) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}

		Job tempJob;
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			String inputLine = br.readLine();
			while(inputLine != null ) {
				if (inputLine.length()>0 &&
					!inputLine.startsWith("#")) {
					tempJob = new Job(inputLine);
					queue.add(tempJob);
//					System.out.println("Loaded "+tempJob);
				}
				inputLine = br.readLine();
			}
		} catch (IOException e) {
			br.close();
			throw e;
		}
		br.close();
	}
	
	public static ArrayList<Job> Load(String filename) throws FileNotFoundException, IOException {
		ArrayList<Job> al = new ArrayList<Job>();
		SimulatorLoader sl = new SimulatorLoader(filename,al);
		sl.load();
		return al;
	}
	
}
