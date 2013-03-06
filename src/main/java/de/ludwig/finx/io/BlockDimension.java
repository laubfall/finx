package de.ludwig.finx.io;

import de.ludwig.finx.ApplicationCodingException;

/**
 * Describes the positioning of a Block in a Property-File. That means: gives you information about
 * the start- and end-line.
 * 
 * @author Daniel
 * 
 */
class BlockDimension implements Comparable<BlockDimension>
{
	private final Integer first;

	private final Integer last;

	/**
	 * @param first
	 *            linenumber this block starts from
	 * @param last
	 *            linenumber this block ends
	 */
	public BlockDimension(Integer first, Integer last)
	{
		super();

		if (last < first) {
			throw new ApplicationCodingException("last is not allowed to be lower then first");
		}

		this.first = first;
		this.last = last;
	}

	/**
	 * @return the first
	 */
	public Integer getFirst()
	{
		return first;
	}

	/**
	 * @return the last
	 */
	public Integer getLast()
	{
		return last;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((last == null) ? 0 : last.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockDimension other = (BlockDimension) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (last == null) {
			if (other.last != null)
				return false;
		} else if (!last.equals(other.last))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("BlockDimension [first=").append(first).append(", last=").append(last).append("]");
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(BlockDimension arg0)
	{
		if (arg0.first > last)
			return -1;

		if (arg0.last < first)
			return 1;
		return 0;
	}
}