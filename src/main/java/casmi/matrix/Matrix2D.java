/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2012, Xcoo, Inc.
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

package casmi.matrix;

import java.text.NumberFormat;

/**
 * 2D Matrix class.
 *
 * @author Y. Ban
 *
 */
public class Matrix2D implements Matrix {

    public double m00, m01, m02;
    public double m10, m11, m12;

    public Matrix2D() {
        reset();
    }

    public Matrix2D(double m00, double m01, double m02,
                    double m10, double m11, double m12) {
        set(m00, m01, m02,
            m10, m11, m12);
    }

    public Matrix2D(Matrix matrix) {
        set(matrix);
    }

    public void reset() {
        set(1, 0, 0,
            0, 1, 0);
    }

    /**
     * Returns a copy of this Matrix.
     */
    public Matrix2D get() {
        Matrix2D outgoing = new Matrix2D();
        outgoing.set(this);
        return outgoing;
    }

    /**
     * Copies the matrix contents into a 6 entry double array.
     * If target is null (or not the correct size), a new array will be created.
     */
    public double[] get(double[] target) {
        if ((target == null) || (target.length != 6)) {
            target = new double[6];
        }
        target[0] = m00;
        target[1] = m01;
        target[2] = m02;

        target[3] = m10;
        target[4] = m11;
        target[5] = m12;

        return target;
    }

    public void set(Matrix matrix) {
        if (matrix instanceof Matrix2D) {
            Matrix2D src = (Matrix2D) matrix;
            set(src.m00, src.m01, src.m02,
                src.m10, src.m11, src.m12);
        } else {
            throw new IllegalArgumentException("Matrix2D.set() only accepts Matrix2D objects.");
        }
    }

    public void set(Matrix3D src) {
    }

    public void set(double[] source) {
        m00 = source[0];
        m01 = source[1];
        m02 = source[2];

        m10 = source[3];
        m11 = source[4];
        m12 = source[5];
    }

    public void set(double m00, double m01, double m02,
                    double m10, double m11, double m12) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
    }

    public void set(double m00, double m01, double m02, double m03,
                    double m10, double m11, double m12, double m13,
                    double m20, double m21, double m22, double m23,
                    double m30, double m31, double m32, double m33) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
    }

    public void translate(double tx, double ty) {
        m02 = tx * m00 + ty * m01 + m02;
        m12 = tx * m10 + ty * m11 + m12;
    }

    public void translate(double x, double y, double z) {
        throw new IllegalArgumentException("Cannot use translate(x, y, z) on a Matrix2D.");
    }

    // Implementation roughly based on AffineTransform.
    public void rotate(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        double temp1 = m00;
        double temp2 = m01;
        m00 =  c * temp1 + s * temp2;
        m01 = -s * temp1 + c * temp2;
        temp1 = m10;
        temp2 = m11;
        m10 =  c * temp1 + s * temp2;
        m11 = -s * temp1 + c * temp2;
    }

    public void rotateX(double angle) {
        throw new IllegalArgumentException("Cannot use rotateX() on a Matrix2D.");
    }

    public void rotateY(double angle) {
        throw new IllegalArgumentException("Cannot use rotateY() on a Matrix2D.");
    }

    public void rotateZ(double angle) {
        rotate(angle);
    }

    public void rotate(double angle, double v0, double v1, double v2) {
        throw new IllegalArgumentException("Cannot use this version of rotate() on a Matrix2D.");
    }

    public void scale(double s) {
        scale(s, s);
    }

    public void scale(double sx, double sy) {
        m00 *= sx;  m01 *= sy;
        m10 *= sx;  m11 *= sy;
    }

    public void scale(double x, double y, double z) {
        throw new IllegalArgumentException("Cannot use this version of scale() on a Matrix2D.");
    }

    public void shearX(double angle) {
        apply(1, 0, 1,  Math.tan(angle), 0, 0);
    }

    public void shearY(double angle) {
        apply(1, 0, 1,  0, Math.tan(angle), 0);
    }

    public void apply(Matrix source) {
        if (source instanceof Matrix2D) {
            apply((Matrix2D) source);
        } else if (source instanceof Matrix3D) {
            apply((Matrix3D) source);
        }
    }

    public void apply(Matrix2D source) {
        apply(source.m00, source.m01, source.m02,
              source.m10, source.m11, source.m12);
    }

    public void apply(Matrix3D source) {
        throw new IllegalArgumentException("Cannot use apply(Matrix3D) on a Matrix2D.");
    }

    public void apply(double n00, double n01, double n02,
                      double n10, double n11, double n12) {
        double t0 = m00;
        double t1 = m01;
        m00  = n00 * t0 + n10 * t1;
        m01  = n01 * t0 + n11 * t1;
        m02 += n02 * t0 + n12 * t1;

        t0 = m10;
        t1 = m11;
        m10  = n00 * t0 + n10 * t1;
        m11  = n01 * t0 + n11 * t1;
        m12 += n02 * t0 + n12 * t1;
    }

    public void apply(double n00, double n01, double n02, double n03,
                      double n10, double n11, double n12, double n13,
                      double n20, double n21, double n22, double n23,
                      double n30, double n31, double n32, double n33) {
        throw new IllegalArgumentException("Cannot use this version of apply() on a Matrix2D.");
    }

    /**
     * Apply another matrix to the left of this one.
     */
    public void preApply(Matrix2D left) {
        preApply(left.m00, left.m01, left.m02,
                 left.m10, left.m11, left.m12);
    }

    public void preApply(Matrix3D left) {
        throw new IllegalArgumentException("Cannot use preApply(Matrix3D) on a Matrix2D.");
    }

    public void preApply(double n00, double n01, double n02,
                         double n10, double n11, double n12) {
        double t0 = m02;
        double t1 = m12;
        n02 += t0 * n00 + t1 * n01;
        n12 += t0 * n10 + t1 * n11;

        m02 = n02;
        m12 = n12;

        t0 = m00;
        t1 = m10;
        m00 = t0 * n00 + t1 * n01;
        m10 = t0 * n10 + t1 * n11;

        t0 = m01;
        t1 = m11;
        m01 = t0 * n00 + t1 * n01;
        m11 = t0 * n10 + t1 * n11;
    }

    public void preApply(double n00, double n01, double n02, double n03,
                         double n10, double n11, double n12, double n13,
                         double n20, double n21, double n22, double n23,
                         double n30, double n31, double n32, double n33) {
        throw new IllegalArgumentException("Cannot use this version of preApply() on a Matrix2D.");
    }

    //////////////////////////////////////////////////////////////

    /**
     * Multiply the x and y coordinates of a Vertex against this matrix.
     */
    public Vector3D mult(Vector3D source) {
        Vector3D result = new Vector3D();
        result.setX(m00 * source.getX() + m01 * source.getY() + m02);
        result.setY(m10 * source.getX() + m11 * source.getY() + m12);
        return result;
    }

    /**
     * Multiply a two element vector against this matrix.
     * If out is null or not length four, a new double array will be returned.
     * The values for vec and out can be the same (though that's less efficient).
     */
    public double[] mult(double[] vec, double[] out) {
        if (out == null || out.length != 2) {
            out = new double[2];
        }

        if (vec == out) {
            double tx = m00*vec[0] + m01*vec[1] + m02;
            double ty = m10*vec[0] + m11*vec[1] + m12;

            out[0] = tx;
            out[1] = ty;
        } else {
            out[0] = m00*vec[0] + m01*vec[1] + m02;
            out[1] = m10*vec[0] + m11*vec[1] + m12;
        }

        return out;
    }

    public double multX(double x, double y) {
        return m00 * x + m01 * y + m02;
    }

    public double multY(double x, double y) {
        return m10 * x + m11 * y + m12;
    }

    /**
     * Transpose this matrix.
     */
    public void transpose() {
    }

    /**
     * Invert this matrix. Implementation stolen from OpenJDK.
     * @return true if successful
     */
    public boolean invert() {
        double determinant = determinant();
        if (Math.abs(determinant) <= Double.MIN_VALUE) {
            return false;
        }

        double t00 = m00;
        double t01 = m01;
        double t02 = m02;
        double t10 = m10;
        double t11 = m11;
        double t12 = m12;

        m00 =  t11 / determinant;
        m10 = -t10 / determinant;
        m01 = -t01 / determinant;
        m11 =  t00 / determinant;
        m02 = (t01 * t12 - t11 * t02) / determinant;
        m12 = (t10 * t02 - t00 * t12) / determinant;

        return true;
    }

    /**
     * @return the determinant of the matrix
     */
    public double determinant() {
        return m00 * m11 - m01 * m10;
    }

    //////////////////////////////////////////////////////////////

    public void print() {
        int big = (int)Math.abs(
            Math.max(max(Math.abs(m00), Math.abs(m01), Math.abs(m02)),
                     max(Math.abs(m10), Math.abs(m11), Math.abs(m12))));

        int digits = 1;
        if (Double.isNaN(big) || Double.isInfinite(big)) {  // avoid infinite loop
            digits = 5;
        } else {
            while ((big /= 10) != 0) digits++;  // cheap log()
        }

        System.out.println(nfs(m00, digits, 4) + " " +
                           nfs(m01, digits, 4) + " " +
                           nfs(m02, digits, 4));

        System.out.println(nfs(m10, digits, 4) + " " +
                           nfs(m11, digits, 4) + " " +
                           nfs(m12, digits, 4));

        System.out.println();
    }

    //////////////////////////////////////////////////////////////

    protected boolean isIdentity() {
        return ((m00 == 1) && (m01 == 0) && (m02 == 0) &&
                (m10 == 0) && (m11 == 1) && (m12 == 0));
    }

    // TODO make this more efficient, or move into Matrix2D
    protected boolean isWarped() {
        return ((m00 != 1) || (m01 != 0) &&
                (m10 != 0) || (m11 != 1));
    }

    //////////////////////////////////////////////////////////////
    private final double max(double a, double b, double c) {
        return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
    }

    static private NumberFormat double_nf;
    static private int double_nf_left, double_nf_right;
    static private boolean double_nf_commas;

    private final String nf(double num, int left, int right) {
        if ((double_nf != null) &&
            (double_nf_left == left) &&
            (double_nf_right == right) &&
            !double_nf_commas) {
            return double_nf.format(num);
        }

        double_nf = NumberFormat.getInstance();
        double_nf.setGroupingUsed(false);
        double_nf_commas = false;

        if (left != 0) double_nf.setMinimumIntegerDigits(left);
        if (right != 0) {
            double_nf.setMinimumFractionDigits(right);
            double_nf.setMaximumFractionDigits(right);
        }
        double_nf_left = left;
        double_nf_right = right;
        return double_nf.format(num);
    }

    private final String nfs(double num, int left, int right) {
        return (num < 0) ? nf(num, left, right) :  (' ' + nf(num, left, right));
    }
}
