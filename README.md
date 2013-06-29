2D/3D visualization for Java

```xml
<dependency>
    <groupId>com.github.casmi</groupId>
    <artifactId>casmi</artifactId>
    <version>0.4.0</version>
</dependency>
```

# Overview

casmi is a visualization library for Java.

casmi supports 2D/3D realtime rendering based on OpenGL.
With assembling graphics primitives (line, circle, sphere, etc.), you can easily design scene layouts.
Smooth tween animations and scene sequencing functions add rich UI/UX on your application.

# Getting Started

## 1. Create a project

We recommend to use [Apache Maven](http://maven.apache.org/).

### Maven Archetype

Run the following command

    mvn archetype:generate -Dfilter=com.github.casmi.archetypes:casmi-quickstart

### Quickstart project

You can download quickstart project template from [download](http://casmi.github.io/download.html) page or [git repository](https://github.com/casmi/casmi-quickstart).

## 2. Build

Change to the your project directory and execute:

    mvn compile

## 3. Run

To run, execute the following **(Required to specify the main class of your project)**

    mvn exec:java -Dexec.mainClass="path.to.MainClass"

## 4. What's next?

Take a [tour](http://casmi.github.io/tour.html).

# License

casmi is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
