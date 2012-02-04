package casmi.extension.coni;

import org.OpenNI.Point3D;

import casmi.matrix.Vertex;

class CONIUtil {

    static final Vertex toVertex(Point3D p) {
        return new Vertex((double)p.getX(), (double)p.getY(), (double)p.getZ());
    }
    
    static final Point3D toPoint3D(Vertex v) {
        return new Point3D((float)v.getX(), (float)v.getY(), (float)v.getZ());
    }
}
