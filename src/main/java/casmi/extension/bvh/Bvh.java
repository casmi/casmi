/*   casmi examples
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package casmi.extension.bvh;

import casmi.graphics.group.Group;

/**
 * Bvh Class.
 * 
 * @author Y. Ban
 */

abstract public class Bvh extends Group{
	public BvhParser parser;
	
	public Bvh(String[] data)
	{
		parser = new BvhParser();
		parser.init();
		parser.parse(data);
		addObjects();
	}
	
	public void update(int ms)
	{
		parser.moveMsTo(ms);
		parser.update();
		updateObjects();
	}

	@Override
	public void update() {
		
	}
	
	abstract public void updateObjects();
	
	abstract public void addObjects();
	
	public void addEndCallback(BvhEndCallback endCallback){
		this.parser.addEndCallback(endCallback);
	}
	

}
