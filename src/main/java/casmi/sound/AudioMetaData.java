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
package casmi.sound;

/**
 * AudioMetaData provides information commonly found in ID3 tags. 
 * However, other audio formats, such as Ogg, can contain
 * similar information. So rather than refer to this information
 * as ID3Tags or similar, I simply call it metadata. This base 
 * class returns the empty string or -1 from all methods and
 * derived classes are expected to simply override the methods
 * that they have information for. This is a little less brittle
 * than using an interface because later on new properties can 
 * be added without breaking existing code.
 */

public abstract class AudioMetaData
{
	/**
	 * @return the length of the recording in milliseconds.
	 */
	public int length()
	{
		return -1;
	}
	
	/**
	 * @return the name of the file / URL of the recording.
	 */
	public String fileName()
	{
		return ""; 
	}
	
	/**
	 * @return the title of the recording
	 */
	public String title()
	{
		return "";
	}
	
	/**
	 * @return the author or the recording
	 */
	public String author()
	{
		return "";
	}
	
	public String album()
	{
		return "";
	}
	
	public String date()
	{
		return "";
	}
	
	public String comment()
	{
		return "";
	}
	
	public int track()
	{
		return -1;
	}
	
	public String genre()
	{
		return "";
	}
	
	public String copyright()
	{
		return "";
	}
	
	public String disc()
	{
		return "";
	}
	
	public String composer()
	{
		return "";
	}
	
	public String orchestra()
	{
		return "";
	}
	
	public String publisher()
	{
		return "";
	}
	
	public String encoded()
	{
		return "";
	}
}
