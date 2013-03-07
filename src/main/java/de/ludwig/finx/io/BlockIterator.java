package de.ludwig.finx.io;

import java.util.ListIterator;

class BlockIterator implements ListIterator<Block>
{
	private Block actualBlock;

	public BlockIterator(final Block actualBlock)
	{
		this.actualBlock = actualBlock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		boolean result = actualBlock != null
				&& (actualBlock.getPursuing() != null || (actualBlock.getPreceding() == null && actualBlock
						.getPursuing() == null));
		if (actualBlock == null || (result == false && actualBlock == null)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#next()
	 */
	@Override
	public Block next()
	{
		Block next = actualBlock;
		actualBlock = actualBlock.getPursuing();
		return next;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#hasPrevious()
	 */
	@Override
	public boolean hasPrevious()
	{
		if (actualBlock != null && actualBlock.getPreceding() != null)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#previous()
	 */
	@Override
	public Block previous()
	{
		Block previous = actualBlock.getPreceding();
		actualBlock = previous;
		return previous;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#nextIndex()
	 */
	@Override
	public int nextIndex()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#previousIndex()
	 */
	@Override
	public int previousIndex()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#remove()
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("remove on Block Iterator is not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#set(java.lang.Object)
	 */
	@Override
	public void set(Block e)
	{
		throw new UnsupportedOperationException("set on Block Iterator is not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#add(java.lang.Object)
	 */
	@Override
	public void add(Block e)
	{
		throw new UnsupportedOperationException("add on Block Iterator is not supported");
	}
}