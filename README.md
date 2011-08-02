# About casmi

casmi is an opensource free software and is Java based library with OpenGL that is using in production or development of 2D/3D visualization systems. It can support programmers with simple and intuitive interfaces for their implementation.
It can work on cross platform (GNU/Linux, Mac OS X, and Windows).
casmi is sutable for visualization, because it originaly desinged for information visualization, especially with huge data set.

# Getting Started

## Download and install JOGL JNI library

You can download JOGL JNI library from http://casmi.github.com/download/

and copy extracted files from this to directory under **java.library.path**.
Typical directories under **java.library.path** are followings.

#### GNU/Linux
- /usr/local/lib
- /usr/lib
- /lib

####  Mac OS X
- /Library/Java/Extensions
- /System/Library/Java/Extensions
- /usr/lib/java

#### Windows
- C:\Windows\system32
- C:\Windows

## Setup casmi

### Using maven (we recommend to use maven)
   
Add following settings to your pom.xml

```xml
<repository>
    <id>casmi</id>
    <name>casmi</name>
    <url>http://casmi.github.com/maven-repo/</url>
</repository>
```

```xml
<dependency>
    <groupId>casmi</groupId>
	<artifactId>casmi</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

Also you can download Quickstart Eclipse project template from http://casmi.github.com/download/

### Others
You can download binary jar from http://casmi.github.com/download/

# Reference
  https://github.com/casmi/casmi/wiki/Reference
  
# Milestones
  https://github.com/casmi/casmi/wiki/Milestone

# Changelog
  https://github.com/casmi/casmi/wiki/ChangeLog
