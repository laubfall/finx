package de.ludwig.finx.io;

import org.apache.commons.lang3.StringUtils;

class Line implements Comparable<Line>
{
	/**
	 * actually without a use
	 * 
	 * TODO check if it is really necessary.
	 */
	private final int pos;

	private final String line;

	/**
	 * @param pos
	 * @param line
	 */
	public Line(int pos, String line)
	{
		super();
		this.pos = pos;
		this.line = StringUtils.defaultString(line);
	}

	/**
	 * @return the pos
	 */
	public int getPos()
	{
		return pos;
	}

	/**
	 * @return the line
	 */
	public String getLine()
	{
		return line;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Line [pos=" + pos + ", line=" + line + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Line o)
	{
		return line.compareTo(o.getLine());
	}

}