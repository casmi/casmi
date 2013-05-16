# casmi

casmi is an opensource free software and is Java based library with OpenGL that is using in production or development of 2D/3D visualization systems. It can support programmers with simple and intuitive interfaces for their implementation.
It can work on cross platform (GNU/Linux, Mac OS X, and Windows).
casmi is sutable for visualization, because it originaly desinged for information visualization, especially with huge data set.

# Getting Started

## Setup

### Using Maven (we strongly recommend to use Maven)

Add following settings to your `pom.xml`.

```xml
<dependency>
    <groupId>com.github.casmi</groupId>
	<artifactId>casmi</artifactId>
	<version>0.3.2</version>
</dependency>
```

All dependent libraries will be downloaded automatically if using maven.

### Manual setup

You can download binary jar from <http://casmi.github.com/download/>.

## Project Template

### Maven Archetype

You can use maven archetype for casmi project.
Archetype catalog is <http://casmi.github.com/maven-repo/archetype-catalog.xml>

You can create Eclipse project by the following command.

```
$ mvn archetype:generate -DarchetypeCatalog=http://casmi.github.com/maven-repo/archetype-catalog.xml
```

### Quickstart Project

You can download quickstart project template from <http://casmi.github.com/download/>.

# License

casmi is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
