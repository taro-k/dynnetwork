package org.cytoscape.dyn.internal.io.write.text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * <code> SRTWriter </code> is a SRT exporter class. SRT files are used to display
 * subtitles for avi movies. We assume that the generated movie will be played at 25
 * frame/sec (PAL standard).
 * 
 * @author Sabina Sara Pfister
 *
 */
public class SRTWriter {
	
	private final PrintStream stream;
	
	private int count;
	private double time;
	
	private DecimalFormatSymbols fts;
	private DecimalFormat formatter;
	private DecimalFormat formatter2;
	private DecimalFormat formatter3;

	/**
	 * <code> SRTWriter </code> constructor.
	 * @param file
	 */
	public SRTWriter(final File outputFile)
	{
		fts = new DecimalFormatSymbols();
		fts.setDecimalSeparator(',');
		formatter = new DecimalFormat("#.00");
		formatter2 = new DecimalFormat("00");
		formatter3 = new DecimalFormat("00.000",fts);
		
		OutputStream stream;
		try {
			stream = new FileOutputStream(outputFile,false);
		} catch (FileNotFoundException e) {
			throw new NullPointerException("Stream is null.");
		}
		
		this.stream = new PrintStream(stream);
		this.count = 1;
		this.time = 0;
	}

	public void export(double currentTime, int iterations)
	{
		int hours = 0;
		int minutes = (int) (this.time / 60);
		double seconds = (this.time % 60);
		
		this.time = this.time + iterations/25.0;
		
		int nexthours = 0;
		int nextminutes = (int) (this.time / 60);
		double nextseconds = (this.time % 60);

		stream.println (this.count);
		stream.println (
				formatter2.format(hours) + ":" + formatter2.format(minutes) + ":" + formatter3.format(seconds) + " --> " + 
				formatter2.format(nexthours) + ":" + formatter2.format(nextminutes) + ":" + formatter3.format(nextseconds));
		stream.println ("<font color=\"#000000\">" + formatter.format(currentTime) + "</font>");
		stream.println ("");
		
		this.count += 1;

	}
	
	public void dispose()
	{
		stream.close();
	}

}
