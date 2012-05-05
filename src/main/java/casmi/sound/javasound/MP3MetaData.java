/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 *  casmi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package casmi.sound.javasound;

import java.util.Map;

class MP3MetaData extends BasicMetaData
{
	private Map<String,Object> mTags;
	
	MP3MetaData(String filename, long length, Map<String, Object> tags)
	{
		super(filename, length);
		mTags = tags;
	}
	
	private String getTag(String tag)
	{
		if ( mTags.containsKey(tag) )
		{
			return (String)mTags.get(tag);
		}
		return "";
	}
	
	public String title()
	{
		return getTag("title");
	}
	
	public String author()
	{
		return getTag("author");
	}
	
	public String album()
	{
		return getTag("album");
	}
	
	public String date()
	{
		return getTag("date");
	}
	
	public String comment()
	{
		return getTag("comment");
	}
	
	public int track()
	{
		String t = getTag("mp3.id3tag.track");
		if ( t == "" )
		{
			return -1;
		}
		return Integer.parseInt(t);
	}
	
	public String genre()
	{
		return getTag("mp3.id3tag.genre");
	}
	
	public String copyright()
	{
		return getTag("copyright");
	}
	
	public String disc()
	{
		return getTag("mp3.id3tag.disc");
	}
	
	public String composer()
	{
		return getTag("mp3.id3tag.composer");
	}
	
	public String orchestra()
	{
		return getTag("mp3.id3tag.orchestra");
	}
	
	public String publisher()
	{
		return getTag("mp3.id3tag.publisher");
	}
	
	public String encoded()
	{
		return getTag("mp3.id3tag.encoded");
	}
}
