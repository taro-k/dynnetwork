package org.cytoscape.dyn.internal.io.write.text;

import java.io.File;

import org.cytoscape.dyn.internal.io.write.AbstractDynNetworkViewWriterFactory;
import org.cytoscape.dyn.internal.model.DynNetwork;

/**
 * <code> SRTWriterFactory </code> extends {@link AbstractDynNetworkViewWriterFactory}. 
 * Is used to create instance of the image writer {@link SRTWriter}.
 * 
 * @author Sabina Sara Pfister
 *
 */
public class SRTWriterFactory<T> extends AbstractDynNetworkViewWriterFactory<T>
{
	
	private final String fileName;
	private final SRTWriter writer;
	
	/**
	 * <code> SRTWriterFactory </code> constructor.
	 * @param stream
	 */
	public SRTWriterFactory(
			final File file) 
	{

		this.fileName = trim(file.getAbsolutePath());
		writer = new SRTWriter(new File(fileName + ".srt"));

	}

	@Override
	public void dispose() 
	{
		writer.dispose();
	}

	@Override
	public void updateView(DynNetwork<T> dynNetwork, double currentTime, int iteration, int iterations) 
	{
		if (iteration==0) 
		{
			writer.export(currentTime, iterations);
		}
	}
	
	private String trim(String str)
	{
		if (str.lastIndexOf('.')>0)
			return str.substring(0, str.lastIndexOf('.'));
		else
			return str;
	}

}
