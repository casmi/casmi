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

package casmi;

import casmi.graphics.canvas.Canvas;
import casmi.graphics.object.Camera;

/**
 * Class for Trackball manipulation.
 *
 * @author T. Takeuchi, Y. Ban
 */
public class Trackball {

    private static final double TRACKBALL_SIZE = 0.8;
    private static final int RENORM_COUNT = 97;

    private static int count = 0;

    private int width, height;
    private Camera camera = null;

    class Quaternion {

        double x = 0.0, y = 0.0, z = 0.0;
        double w = 1.0;

        public Quaternion() {}

        public Quaternion(double x, double y, double z, double w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }
    }

    private Quaternion curQuat = new Quaternion();

    public Trackball(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be more than zero.");
        }

        this.width = width;
        this.height = height;
    }

    public Trackball(Applet applet) {
        this(applet.getWidth(), applet.getHeight());
    }

    public void update(int mouseX, int mouseY, int prevMouseX, int prevMouseY) {
        double normalizedMouseX = (2.0 * mouseX - width) / width;
        double normalizedMouseY = (2.0 * mouseY - height) / height;

        double normalizedPrevMouseX = (2.0 * prevMouseX - width) / width;
        double normalizedPrevMouseY = (2.0 * prevMouseY - height) / height;

        Quaternion lastQuat =
            calcQuat(normalizedPrevMouseX, normalizedPrevMouseY, normalizedMouseX, normalizedMouseY);

        curQuat = addQuats(lastQuat, curQuat);
    }

    public void reset() {
        curQuat = new Quaternion();
    }

    public double[] getRotationMatrix() {
        return calcRotMatrix();
    }

    public void rotate(Canvas obj) {
        rotate(obj, obj.getX(), obj.getY(), obj.getZ());
    }

    public void rotate(Canvas obj, double baseX, double baseY, double baseZ) {
        double[] rotMat = calcRotMatrix();

        double[] mat1 =
            {1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, -baseX, -baseY, -baseZ, 1.0};

        double[] mat2 =
            {1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, baseX, baseY, baseZ, 1.0};

        double[] mat = multMatrix(mat1, multMatrix(rotMat, mat2));

        obj.applyMatrix(mat);
    }



    private final Quaternion calcQuat(double x1, double y1, double x2, double y2) {

        Quaternion quat = new Quaternion();

        double[] a = new double[3];
        double phi;
        double[] p1 = new double[3];
        double[] p2 = new double[3];
        double[] d = new double[3];
        double t;

        if (x1 == x2 && y1 == y2) {
            quat.x = 0.0;
            quat.y = 0.0;
            quat.z = 0.0;
            quat.w = 1.0;
            return quat;
        }

        p1[0] = x1;
        p1[1] = y1;
        p1[2] = tbProjectToSphere(TRACKBALL_SIZE, x1, y1);

        p2[0] = x2;
        p2[1] = y2;
        p2[2] = tbProjectToSphere(TRACKBALL_SIZE, x2, y2);

        a = vcross(p2, p1);
        a = applyCameraMatrix(a);

        d = vsub(p1, p2);
        t = vlength(d) / (2.0 * TRACKBALL_SIZE);

        if (1.0 < t) {
            t = 1.0;
        } else if (t < -1.0) {
            t = -1.0;
        }
        phi = 2.0 * Math.asin(t);
        quat = axisToQuat(a, phi);

        return quat;
    }

    private double[] applyCameraMatrix(double[] mat) {
        if(this.camera!=null){
            double[] axis = {mat[0], mat[1], mat[2], 1};

            double[] eye = this.camera.getViewMatrix();
            double[] inv = inverseMatrix(eye);
            inv[12] = inv[13]= inv[14] = 0;
            axis = multMatrix(axis, inv);
            double[] result = {axis[0], axis[1], axis[2]};
            return result;
        } else
            return mat;
    }

    private final Quaternion addQuats(Quaternion quat1, Quaternion quat2) {

        Quaternion ret = new Quaternion();

        double[] q1 = {quat1.x, quat1.y, quat1.z, quat1.w};
        double[] q2 = {quat2.x, quat2.y, quat2.z, quat2.w};
        double[] t1 = new double[3];
        double[] t2 = new double[3];
        double[] t3 = new double[3];
        double[] tf = new double[4];

        t1[0] = q1[0];
        t1[1] = q1[1];
        t1[2] = q1[2];
        t1 = vscale(t1, q2[3]);

        t2[0] = q2[0];
        t2[1] = q2[1];
        t2[2] = q2[2];
        t2 = vscale(t2, q1[3]);

        t3 = vcross(q2, q1);
        tf[0] = t1[0] + t2[0];
        tf[1] = t1[1] + t2[1];
        tf[2] = t1[2] + t2[2];
        tf[0] += t3[0];
        tf[1] += t3[1];
        tf[2] += t3[2];
        tf[3] = q1[3] * q2[3] - vdot(q1, q2);

        ret.x = tf[0];
        ret.y = tf[1];
        ret.z = tf[2];
        ret.w = tf[3];

        if (RENORM_COUNT < ++count) {
            count = 0;
            ret = normalizeQuat(ret);
        }

        return ret;
    }

    private final double[] calcRotMatrix() {
        double[] rotMatrix = new double[16];

        rotMatrix[0] = 1.0 - 2.0 * (curQuat.y * curQuat.y + curQuat.z * curQuat.z);
        rotMatrix[1] = 2.0 * (curQuat.x * curQuat.y - curQuat.z * curQuat.w);
        rotMatrix[2] = 2.0 * (curQuat.z * curQuat.x + curQuat.y * curQuat.w);
        rotMatrix[3] = 0.0;

        rotMatrix[4] = 2.0 * (curQuat.x * curQuat.y + curQuat.z * curQuat.w);
        rotMatrix[5] = 1.0 - 2.0 * (curQuat.z * curQuat.z + curQuat.x * curQuat.x);
        rotMatrix[6] = 2.0 * (curQuat.y * curQuat.z - curQuat.x * curQuat.w);
        rotMatrix[7] = 0.0;

        rotMatrix[8] = 2.0 * (curQuat.z * curQuat.x - curQuat.y * curQuat.w);
        rotMatrix[9] = 2.0 * (curQuat.y * curQuat.z + curQuat.x * curQuat.w);
        rotMatrix[10] = 1.0 - 2.0 * (curQuat.y * curQuat.y + curQuat.x * curQuat.x);
        rotMatrix[11] = 0.0;

        rotMatrix[12] = 0.0;
        rotMatrix[13] = 0.0;
        rotMatrix[14] = 0.0;
        rotMatrix[15] = 1.0;

        return rotMatrix;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    private final double tbProjectToSphere(double r, double x, double y) {

        double d, t, z;

        d = Math.sqrt(x * x + y * y);
        if (d < r * 0.70710678118654752440) { // inside sphere
            z = Math.sqrt(r * r - d * d);
        } else { // on hyperbola
            t = r / 1.41421356237309504880;
            z = t * t / d;
        }

        return z;
    }

    private final double[] vcross(final double[] v1, final double[] v2) {
        double[] ret = new double[3];

        ret[0] = (v1[1] * v2[2]) - (v1[2] * v2[1]);
        ret[1] = (v1[2] * v2[0]) - (v1[0] * v2[2]);
        ret[2] = (v1[0] * v2[1]) - (v1[1] * v2[0]);

        return ret;
    }

    private final double[] vsub(double[] src1, double[] src2) {
        double[] ret = new double[3];

        ret[0] = src1[0] - src2[0];
        ret[1] = src1[1] - src2[1];
        ret[2] = src1[2] - src2[2];

        return ret;
    }

    private final double[] vnormal(double[] v) {
        return vscale(v, 1.0 / vlength(v));
    }

    private final double[] vscale(final double[] v, final double div) {
        double[] ret = new double[3];

        ret[0] = v[0] * div;
        ret[1] = v[1] * div;
        ret[2] = v[2] * div;

        return ret;
    }

    private final double vlength(final double[] v) {
        return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }

    private final double vdot(final double[] v1, final double[] v2) {
        return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
    }

    private final Quaternion normalizeQuat(Quaternion q) {
        Quaternion ret = new Quaternion();

        double mag = Math.sqrt(q.x * q.x + q.y * q.y + q.z * q.z + q.w * q.w);

        ret.x = q.x / mag;
        ret.y = q.y / mag;
        ret.z = q.z / mag;
        ret.w = q.w / mag;

        return ret;
    }

    private final Quaternion axisToQuat(double[] a, double phi) {

        double[] ret = new double[4];

        a = vnormal(a);
        ret[0] = a[0];
        ret[1] = a[1];
        ret[2] = a[2];

        double[] tmp = vscale(ret, Math.sin(phi / 2.0));

        ret[0] = tmp[0];
        ret[1] = tmp[1];
        ret[2] = tmp[2];
        ret[3] = Math.cos(phi / 2.0);

        return new Quaternion(ret[0], ret[1], ret[2], ret[3]);
    }

    private final double calcDeterminate(final double[] mat) {
        return  mat[12] * mat[9] * mat[6] * mat[3] - mat[8] * mat[13] * mat[6] * mat[3] -
            mat[12] * mat[5] * mat[10] * mat[3] + mat[4] * mat[13] * mat[10] * mat[3] +
            mat[8] * mat[5] * mat[14] * mat[3] - mat[4] * mat[9] * mat[14] * mat[3] -
            mat[12] * mat[9] * mat[2] * mat[7] + mat[8] * mat[13] * mat[2] * mat[7] +
            mat[12] * mat[1] * mat[10] * mat[7] - mat[0] * mat[13] * mat[10] * mat[7] -
            mat[8] * mat[1] * mat[14] * mat[7] + mat[0] * mat[9] * mat[14] * mat[7] +
            mat[12] * mat[5] * mat[2] * mat[11] - mat[4] * mat[13] * mat[2] * mat[11] -
            mat[12] * mat[1] * mat[6] * mat[11] + mat[0] * mat[13] * mat[6] * mat[11] +
            mat[4] * mat[1] * mat[14] * mat[11] - mat[0] * mat[5] * mat[14] * mat[11] -
            mat[8] * mat[5] * mat[2] * mat[15] + mat[4] * mat[9] * mat[2] * mat[15] +
            mat[8] * mat[1] * mat[6] * mat[15] - mat[0] * mat[9] * mat[6] * mat[15] -
            mat[4] * mat[1] * mat[10] * mat[15] + mat[0] * mat[5] * mat[10] * mat[15] ;
    }

    private final double[] inverseMatrix(final double[] mat) {
        double[] inv = new double[16];
        double[] result = new double[16];
        double det = 1.0f / calcDeterminate(mat);

        inv[0]   = mat[6]*mat[11]*mat[13] - mat[7]*mat[10]*mat[13]
        + mat[7]*mat[9]*mat[14] - mat[5]*mat[11]*mat[14]
        - mat[6]*mat[9]*mat[15] + mat[5]*mat[10]*mat[15];

        inv[4]   = mat[3]*mat[10]*mat[13] - mat[2]*mat[11]*mat[13]
        - mat[3]*mat[9]*mat[14] + mat[1]*mat[11]*mat[14]
        + mat[2]*mat[9]*mat[15] - mat[1]*mat[10]*mat[15];

        inv[8]   = mat[2]*mat[7]*mat[13] - mat[3]*mat[6]*mat[13]
        + mat[3]*mat[5]*mat[14] - mat[1]*mat[7]*mat[14]
        - mat[2]*mat[5]*mat[15] + mat[1]*mat[6]*mat[15];

        inv[12]  = mat[3]*mat[6]*mat[9] - mat[2]*mat[7]*mat[9]
        - mat[3]*mat[5]*mat[10] + mat[1]*mat[7]*mat[10]
        + mat[2]*mat[5]*mat[11] - mat[1]*mat[6]*mat[11];

        inv[1]   = mat[7]*mat[10]*mat[12] - mat[6]*mat[11]*mat[12]
        - mat[7]*mat[8]*mat[14] + mat[4]*mat[11]*mat[14]
        + mat[6]*mat[8]*mat[15] - mat[4]*mat[10]*mat[15];

        inv[5]   = mat[2]*mat[11]*mat[12] - mat[3]*mat[10]*mat[12]
        + mat[3]*mat[8]*mat[14] - mat[0]*mat[11]*mat[14]
        - mat[2]*mat[8]*mat[15] + mat[0]*mat[10]*mat[15];

        inv[9]   = mat[3]*mat[6]*mat[12] - mat[2]*mat[7]*mat[12]
        - mat[3]*mat[4]*mat[14] + mat[0]*mat[7]*mat[14]
        + mat[2]*mat[4]*mat[15] - mat[0]*mat[6]*mat[15];

        inv[13]  = mat[2]*mat[7]*mat[8] - mat[3]*mat[6]*mat[8]
        + mat[3]*mat[4]*mat[10] - mat[0]*mat[7]*mat[10]
        - mat[2]*mat[4]*mat[11] + mat[0]*mat[6]*mat[11];

        inv[2]   = mat[5]*mat[11]*mat[12] - mat[7]*mat[9]*mat[12]
        + mat[7]*mat[8]*mat[13] - mat[4]*mat[11]*mat[13]
        - mat[5]*mat[8]*mat[15] + mat[4]*mat[9]*mat[15];

        inv[6]   = mat[3]*mat[9]*mat[12] - mat[1]*mat[11]*mat[12]
        - mat[3]*mat[8]*mat[13] + mat[0]*mat[11]*mat[13]
        + mat[1]*mat[8]*mat[15] - mat[0]*mat[9]*mat[15];

        inv[10]  = mat[1]*mat[7]*mat[12] - mat[3]*mat[5]*mat[12]
        + mat[3]*mat[4]*mat[13] - mat[0]*mat[7]*mat[13]
        - mat[1]*mat[4]*mat[15] + mat[0]*mat[5]*mat[15];

        inv[14]  = mat[3]*mat[5]*mat[8] - mat[1]*mat[7]*mat[8]
        - mat[3]*mat[4]*mat[9] + mat[0]*mat[7]*mat[9]
        + mat[1]*mat[4]*mat[11] - mat[0]*mat[5]*mat[11];

        inv[3]   = mat[6]*mat[9]*mat[12] - mat[5]*mat[10]*mat[12]
        - mat[6]*mat[8]*mat[13] + mat[4]*mat[10]*mat[13]
        + mat[5]*mat[8]*mat[14] - mat[4]*mat[9]*mat[14];

        inv[7]  = mat[1]*mat[10]*mat[12] - mat[2]*mat[9]*mat[12]
        + mat[2]*mat[8]*mat[13] - mat[0]*mat[10]*mat[13]
        - mat[1]*mat[8]*mat[14] + mat[0]*mat[9]*mat[14];

        inv[11]  = mat[2]*mat[5]*mat[12] - mat[1]*mat[6]*mat[12]
        - mat[2]*mat[4]*mat[13] + mat[0]*mat[6]*mat[13]
        + mat[1]*mat[4]*mat[14] - mat[0]*mat[5]*mat[14];

        inv[15]  = mat[1]*mat[6]*mat[8] - mat[2]*mat[5]*mat[8]
        + mat[2]*mat[4]*mat[9] - mat[0]*mat[6]*mat[9]
        - mat[1]*mat[4]*mat[10] + mat[0]*mat[5]*mat[10];

        for (int i = 0; i < 16; i++)
            inv[i] *= det;

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                result[i*4+j] = inv[i+4*j];

        return result;
    }

    private final double[] multMatrix(final double[] mat1, final double[] mat2) {
        double [] ret;
        if(mat1.length == 16){
            ret = new double[16];

            ret[0]  = mat1[0] * mat2[0] + mat1[1] * mat2[4] + mat1[2] * mat2[8] + mat1[3] * mat2[12];
            ret[1]  = mat1[0] * mat2[1] + mat1[1] * mat2[5] + mat1[2] * mat2[9] + mat1[3] * mat2[13];
            ret[2]  = mat1[0] * mat2[2] + mat1[1] * mat2[6] + mat1[2] * mat2[10] + mat1[3] * mat2[14];
            ret[3]  = mat1[0] * mat2[3] + mat1[1] * mat2[7] + mat1[2] * mat2[11] + mat1[3] * mat2[15];

            ret[4]  = mat1[4] * mat2[0] + mat1[5] * mat2[4] + mat1[6] * mat2[8] + mat1[7] * mat2[12];
            ret[5]  = mat1[4] * mat2[1] + mat1[5] * mat2[5] + mat1[6] * mat2[9] + mat1[7] * mat2[13];
            ret[6]  = mat1[4] * mat2[2] + mat1[5] * mat2[6] + mat1[6] * mat2[10] + mat1[7] * mat2[14];
            ret[7]  = mat1[4] * mat2[3] + mat1[5] * mat2[7] + mat1[6] * mat2[11] + mat1[7] * mat2[15];

            ret[8]  = mat1[8] * mat2[0] + mat1[9] * mat2[4] + mat1[10] * mat2[8] + mat1[11] * mat2[12];
            ret[9]  = mat1[8] * mat2[1] + mat1[9] * mat2[5] + mat1[10] * mat2[9] + mat1[11] * mat2[13];
            ret[10] = mat1[8] * mat2[2] + mat1[9] * mat2[6] + mat1[10] * mat2[10] + mat1[11] * mat2[14];
            ret[11] = mat1[8] * mat2[3] + mat1[9] * mat2[7] + mat1[10] * mat2[11] + mat1[11] * mat2[15];

            ret[12] = mat1[12] * mat2[0] + mat1[13] * mat2[4] + mat1[14] * mat2[8] + mat1[15] * mat2[12];
            ret[13] = mat1[12] * mat2[1] + mat1[13] * mat2[5] + mat1[14] * mat2[9] + mat1[15] * mat2[13];
            ret[14] = mat1[12] * mat2[2] + mat1[13] * mat2[6] + mat1[14] * mat2[10] + mat1[15] * mat2[14];
            ret[15] = mat1[12] * mat2[3] + mat1[13] * mat2[7] + mat1[14] * mat2[11] + mat1[15] * mat2[15];
        } else {
            ret = new double[4];

            ret[0]  = mat1[0] * mat2[0] + mat1[1] * mat2[4] + mat1[2] * mat2[8] + mat1[3] * mat2[12];
            ret[1]  = mat1[0] * mat2[1] + mat1[1] * mat2[5] + mat1[2] * mat2[9] + mat1[3] * mat2[13];
            ret[2]  = mat1[0] * mat2[2] + mat1[1] * mat2[6] + mat1[2] * mat2[10] + mat1[3] * mat2[14];
            ret[3]  = mat1[0] * mat2[3] + mat1[1] * mat2[7] + mat1[2] * mat2[11] + mat1[3] * mat2[15];
        }
        return ret;
    }
}
