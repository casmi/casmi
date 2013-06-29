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
 * 3D Matrix class.
 *
 * @author Y. Ban
 */
public class Matrix3D implements Matrix {

    public double m00, m01, m02, m03;
    public double m10, m11, m12, m13;
    public double m20, m21, m22, m23;
    public double m30, m31, m32, m33;

    // locally allocated version to avoid creating new memory
    protected Matrix3D inverseCopy;

    public Matrix3D() {
        reset();
    }

    public Matrix3D(double m00, double m01, double m02,
                    double m10, double m11, double m12) {
        set(m00, m01, m02, 0,
            m10, m11, m12, 0,
            0,   0,   1,   0,
            0,   0,   0,   1);
    }

    public Matrix3D(double m00, double m01, double m02, double m03,
                    double m10, double m11, double m12, double m13,
                    double m20, double m21, double m22, double m23,
                    double m30, double m31, double m32, double m33) {
        set(m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33);
    }

    public Matrix3D(Matrix matrix) {
        set(matrix);
    }

    public Matrix3D(double rotateX, double rotateY, double rotateZ) {
        set(1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);
        apply(getRotateZMatrix(rotateX));
        apply(getRotateYMatrix(rotateY));
        apply(getRotateXMatrix(rotateZ));
    }

    @Override
    public void reset() {
        set(1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);
    }

    public static final Matrix3D getRotateYMatrix(double x) {
        double radian = x / 180.0 * Math.PI;
        Matrix3D matrix = new Matrix3D(
            Math.cos(radian), 0, Math.sin(radian), 0,
            0, 1, 0, 0,
            -Math.sin(radian), 0, Math.cos(radian), 0,
            0, 0, 0, 1);
        return matrix;
    }

    public static final Matrix3D getRotateXMatrix(double x) {
        double radian = x / 180.0 * Math.PI;
        Matrix3D matrix = new Matrix3D(
            1, 0, 0, 0,
            0, Math.cos(radian), -Math.sin(radian), 0,
            0, Math.sin(radian), Math.cos(radian), 0,
            0, 0, 0, 1);
        return matrix;
    }

    public static final Matrix3D getRotateZMatrix(double x) {
        double radian = x / 180.0 * Math.PI;
        Matrix3D matrix = new Matrix3D(
            Math.cos(radian), -Math.sin(radian), 0, 0,
            Math.sin(radian), Math.cos(radian), 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);
        return matrix;
    }

    /**
     * Returns a copy of this Matrix.
     */
    @Override
    public Matrix3D get() {
        Matrix3D outgoing = new Matrix3D();
        outgoing.set(this);
        return outgoing;
    }

    /**
     * Copies the matrix contents into a 16 entry double array.
     * If target is null (or not the correct size), a new array will be created.
     */
    @Override
    public double[] get(double[] target) {
        if ((target == null) || (target.length != 16)) {
            target = new double[16];
        }
        target[0] = m00;
        target[1] = m01;
        target[2] = m02;
        target[3] = m03;

        target[4] = m10;
        target[5] = m11;
        target[6] = m12;
        target[7] = m13;

        target[8] = m20;
        target[9] = m21;
        target[10] = m22;
        target[11] = m23;

        target[12] = m30;
        target[13] = m31;
        target[14] = m32;
        target[15] = m33;

        return target;
    }

    @Override
    public void set(Matrix matrix) {
        if (matrix instanceof Matrix3D) {
            Matrix3D src = (Matrix3D) matrix;
            set(src.m00, src.m01, src.m02, src.m03,
                src.m10, src.m11, src.m12, src.m13,
                src.m20, src.m21, src.m22, src.m23,
                src.m30, src.m31, src.m32, src.m33);
        } else {
            Matrix2D src = (Matrix2D) matrix;
            set(src.m00, src.m01, 0, src.m02,
                src.m10, src.m11, 0, src.m12,
                0, 0, 1, 0,
                0, 0, 0, 1);
        }
    }

    @Override
    public void set(double[] source) {
        if (source.length == 6) {
            set(source[0], source[1], source[2],
                source[3], source[4], source[5]);

        } else if (source.length == 16) {
            m00 = source[0];
            m01 = source[1];
            m02 = source[2];
            m03 = source[3];

            m10 = source[4];
            m11 = source[5];
            m12 = source[6];
            m13 = source[7];

            m20 = source[8];
            m21 = source[9];
            m22 = source[10];
            m23 = source[11];

            m30 = source[12];
            m31 = source[13];
            m32 = source[14];
            m33 = source[15];
        }
    }

    @Override
    public void set(double m00, double m01, double m02,
                    double m10, double m11, double m12) {
        set(m00, m01, 0, m02,
            m10, m11, 0, m12,
            0, 0, 1, 0,
            0, 0, 0, 1);
    }

    @Override
    public void set(double m00, double m01, double m02, double m03,
                    double m10, double m11, double m12, double m13,
                    double m20, double m21, double m22, double m23,
                    double m30, double m31, double m32, double m33) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
        this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
        this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
        this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
    }

    @Override
    public void translate(double tx, double ty) {
        translate(tx, ty, 0);
    }

    //  public void invTranslate(double tx, double ty) {
    //        invTranslate(tx, ty, 0);
    //  }

    @Override
    public void translate(double tx, double ty, double tz) {
        m03 += tx * m00 + ty * m01 + tz * m02;
        m13 += tx * m10 + ty * m11 + tz * m12;
        m23 += tx * m20 + ty * m21 + tz * m22;
        m33 += tx * m30 + ty * m31 + tz * m32;
    }

    @Override
    public void rotate(double angle) {
        rotateZ(angle);
    }

    @Override
    public void rotateX(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        apply(1, 0, 0, 0,  0, c, -s, 0,  0, s, c, 0,  0, 0, 0, 1);
    }

    @Override
    public void rotateY(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        apply(c, 0, s, 0,  0, 1, 0, 0,  -s, 0, c, 0,  0, 0, 0, 1);
    }

    @Override
    public void rotateZ(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        apply(c, -s, 0, 0,  s, c, 0, 0,  0, 0, 1, 0,  0, 0, 0, 1);
    }

    @Override
    public void rotate(double angle, double v0, double v1, double v2) {
        // TODO should make sure this vector is normalized

        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double t = 1.0f - c;

        apply((t * v0 * v0) + c, (t * v0 * v1) - (s * v2), (t * v0 * v2) + (s * v1), 0,
              (t * v0 * v1) + (s * v2), (t * v1 * v1) + c, (t * v1 * v2) - (s * v0), 0,
              (t * v0 * v2) - (s * v1), (t * v1 * v2) + (s * v0), (t * v2 * v2) + c, 0,
              0, 0, 0, 1);
    }

    @Override
    public void scale(double s) {
        //apply(s, 0, 0, 0,  0, s, 0, 0,  0, 0, s, 0,  0, 0, 0, 1);
        scale(s, s, s);
    }

    @Override
    public void scale(double sx, double sy) {
        //apply(sx, 0, 0, 0,  0, sy, 0, 0,  0, 0, 1, 0,  0, 0, 0, 1);
        scale(sx, sy, 1);
    }

    @Override
    public void scale(double x, double y, double z) {
        //apply(x, 0, 0, 0,  0, y, 0, 0,  0, 0, z, 0,  0, 0, 0, 1);
        m00 *= x;  m01 *= y;  m02 *= z;
        m10 *= x;  m11 *= y;  m12 *= z;
        m20 *= x;  m21 *= y;  m22 *= z;
        m30 *= x;  m31 *= y;  m32 *= z;
    }

    @Override
    public void shearX(double angle) {
        double t = Math.tan(angle);
        apply(1, t, 0, 0,
              0, 1, 0, 0,
              0, 0, 1, 0,
              0, 0, 0, 1);
    }

    @Override
    public void shearY(double angle) {
        double t = Math.tan(angle);
        apply(1, 0, 0, 0,
              t, 1, 0, 0,
              0, 0, 1, 0,
              0, 0, 0, 1);
    }

    @Override
    public void apply(Matrix source) {
        if (source instanceof Matrix2D) {
            apply((Matrix2D) source);
        } else if (source instanceof Matrix3D) {
            apply((Matrix3D) source);
        }
    }

    @Override
    public void apply(Matrix2D source) {
        apply(source.m00, source.m01, 0, source.m02,
              source.m10, source.m11, 0, source.m12,
              0, 0, 1, 0,
              0, 0, 0, 1);
    }

    @Override
    public void apply(Matrix3D source) {
        apply(source.m00, source.m01, source.m02, source.m03,
              source.m10, source.m11, source.m12, source.m13,
              source.m20, source.m21, source.m22, source.m23,
              source.m30, source.m31, source.m32, source.m33);
    }

    @Override
    public void apply(double n00, double n01, double n02,
                      double n10, double n11, double n12) {
        apply(n00, n01, 0, n02,
              n10, n11, 0, n12,
              0, 0, 1, 0,
              0, 0, 0, 1);
    }

    @Override
    public void apply(double n00, double n01, double n02, double n03,
                      double n10, double n11, double n12, double n13,
                      double n20, double n21, double n22, double n23,
                      double n30, double n31, double n32, double n33) {

        double r00 = m00 * n00 + m01 * n10 + m02 * n20 + m03 * n30;
        double r01 = m00 * n01 + m01 * n11 + m02 * n21 + m03 * n31;
        double r02 = m00 * n02 + m01 * n12 + m02 * n22 + m03 * n32;
        double r03 = m00 * n03 + m01 * n13 + m02 * n23 + m03 * n33;

        double r10 = m10 * n00 + m11 * n10 + m12 * n20 + m13 * n30;
        double r11 = m10 * n01 + m11 * n11 + m12 * n21 + m13 * n31;
        double r12 = m10 * n02 + m11 * n12 + m12 * n22 + m13 * n32;
        double r13 = m10 * n03 + m11 * n13 + m12 * n23 + m13 * n33;

        double r20 = m20 * n00 + m21 * n10 + m22 * n20 + m23 * n30;
        double r21 = m20 * n01 + m21 * n11 + m22 * n21 + m23 * n31;
        double r22 = m20 * n02 + m21 * n12 + m22 * n22 + m23 * n32;
        double r23 = m20 * n03 + m21 * n13 + m22 * n23 + m23 * n33;

        double r30 = m30 * n00 + m31 * n10 + m32 * n20 + m33 * n30;
        double r31 = m30 * n01 + m31 * n11 + m32 * n21 + m33 * n31;
        double r32 = m30 * n02 + m31 * n12 + m32 * n22 + m33 * n32;
        double r33 = m30 * n03 + m31 * n13 + m32 * n23 + m33 * n33;

        m00 = r00; m01 = r01; m02 = r02; m03 = r03;
        m10 = r10; m11 = r11; m12 = r12; m13 = r13;
        m20 = r20; m21 = r21; m22 = r22; m23 = r23;
        m30 = r30; m31 = r31; m32 = r32; m33 = r33;
    }

    @Override
    public void preApply(Matrix2D left) {
        preApply(left.m00, left.m01, 0, left.m02,
                 left.m10, left.m11, 0, left.m12,
                 0, 0, 1, 0,
                 0, 0, 0, 1);
    }

    /**
     * Apply another matrix to the left of this one.
     */
    @Override
    public void preApply(Matrix3D left) {
        preApply(left.m00, left.m01, left.m02, left.m03,
                 left.m10, left.m11, left.m12, left.m13,
                 left.m20, left.m21, left.m22, left.m23,
                 left.m30, left.m31, left.m32, left.m33);
    }

    @Override
    public void preApply(double n00, double n01, double n02,
                         double n10, double n11, double n12) {
        preApply(n00, n01, 0, n02,
                 n10, n11, 0, n12,
                 0, 0, 1, 0,
                 0, 0, 0, 1);
    }

    @Override
    public void preApply(double n00, double n01, double n02, double n03,
                         double n10, double n11, double n12, double n13,
                         double n20, double n21, double n22, double n23,
                         double n30, double n31, double n32, double n33) {

        double r00 = n00 * m00 + n01 * m10 + n02 * m20 + n03 * m30;
        double r01 = n00 * m01 + n01 * m11 + n02 * m21 + n03 * m31;
        double r02 = n00 * m02 + n01 * m12 + n02 * m22 + n03 * m32;
        double r03 = n00 * m03 + n01 * m13 + n02 * m23 + n03 * m33;

        double r10 = n10 * m00 + n11 * m10 + n12 * m20 + n13 * m30;
        double r11 = n10 * m01 + n11 * m11 + n12 * m21 + n13 * m31;
        double r12 = n10 * m02 + n11 * m12 + n12 * m22 + n13 * m32;
        double r13 = n10 * m03 + n11 * m13 + n12 * m23 + n13 * m33;

        double r20 = n20 * m00 + n21 * m10 + n22 * m20 + n23 * m30;
        double r21 = n20 * m01 + n21 * m11 + n22 * m21 + n23 * m31;
        double r22 = n20 * m02 + n21 * m12 + n22 * m22 + n23 * m32;
        double r23 = n20 * m03 + n21 * m13 + n22 * m23 + n23 * m33;

        double r30 = n30 * m00 + n31 * m10 + n32 * m20 + n33 * m30;
        double r31 = n30 * m01 + n31 * m11 + n32 * m21 + n33 * m31;
        double r32 = n30 * m02 + n31 * m12 + n32 * m22 + n33 * m32;
        double r33 = n30 * m03 + n31 * m13 + n32 * m23 + n33 * m33;

        m00 = r00; m01 = r01; m02 = r02; m03 = r03;
        m10 = r10; m11 = r11; m12 = r12; m13 = r13;
        m20 = r20; m21 = r21; m22 = r22; m23 = r23;
        m30 = r30; m31 = r31; m32 = r32; m33 = r33;
    }

    //////////////////////////////////////////////////////////////

    @Override
    public Vector3D mult(Vector3D source) {

        Vector3D result = new Vector3D();
        result.setX(m00 * source.getX() + m01 * source.getY() + m02 * source.getZ() + m03);
        result.setY(m10 * source.getX() + m11 * source.getY() + m12 * source.getZ() + m13);
        result.setZ(m20 * source.getX() + m21 * source.getY() + m22 * source.getZ() + m23);

        return result;
    }

    /**
     * Multiply a three or four element vector against this matrix. If out is
     * null or not length 3 or 4, a new double array (length 3) will be returned.
     */
    @Override
    public double[] mult(double[] source, double[] target) {
        if (target == null || target.length < 3) {
            target = new double[3];
        }
        if (source == target) {
            throw new RuntimeException("The source and target vectors used in Matrix3D.mult() cannot be identical.");
        }
        if (target.length == 3) {
            target[0] = m00 * source[0] + m01 * source[1] + m02 * source[2] + m03;
            target[1] = m10 * source[0] + m11 * source[1] + m12 * source[2] + m13;
            target[2] = m20 * source[0] + m21 * source[1] + m22 * source[2] + m23;
            //double w = m30*source[0] + m31*source[1] + m32*source[2] + m33;
            //if (w != 0 && w != 1) {
            //  target[0] /= w; target[1] /= w; target[2] /= w;
            //}
        } else if (target.length > 3) {
            target[0] = m00 * source[0] + m01 * source[1] + m02 * source[2] + m03 * source[3];
            target[1] = m10 * source[0] + m11 * source[1] + m12 * source[2] + m13 * source[3];
            target[2] = m20 * source[0] + m21 * source[1] + m22 * source[2] + m23 * source[3];
            target[3] = m30 * source[0] + m31 * source[1] + m32 * source[2] + m33 * source[3];
        }
        return target;
    }

    public double multX(double x, double y) {
        return m00 * x + m01 * y + m03;
    }

    public double multY(double x, double y) {
        return m10 * x + m11 * y + m13;
    }

    public double multX(double x, double y, double z) {
        return m00 * x + m01 * y + m02 * z + m03;
    }

    public double multY(double x, double y, double z) {
        return m10 * x + m11 * y + m12 * z + m13;
    }

    public double multZ(double x, double y, double z) {
        return m20 * x + m21 * y + m22 * z + m23;
    }

    public double multW(double x, double y, double z) {
        return m30 * x + m31 * y + m32 * z + m33;
    }

    public double multX(double x, double y, double z, double w) {
        return m00 * x + m01 * y + m02 * z + m03 * w;
    }

    public double multY(double x, double y, double z, double w) {
        return m10 * x + m11 * y + m12 * z + m13 * w;
    }

    public double multZ(double x, double y, double z, double w) {
        return m20 * x + m21 * y + m22 * z + m23 * w;
    }

    public double multW(double x, double y, double z, double w) {
        return m30 * x + m31 * y + m32 * z + m33 * w;
    }

    /**
     * Transpose this matrix.
     */
    @Override
    public void transpose() {
        double temp;
        temp = m01; m01 = m10; m10 = temp;
        temp = m02; m02 = m20; m20 = temp;
        temp = m03; m03 = m30; m30 = temp;
        temp = m12; m12 = m21; m21 = temp;
        temp = m13; m13 = m31; m31 = temp;
        temp = m23; m23 = m32; m32 = temp;
    }

    /**
     * Invert this matrix.
     * @return true if successful
     */
    @Override
    public boolean invert() {
        double determinant = determinant();
        if (determinant == 0) {
            return false;
        }

        // first row
        double t00 =  determinant3x3(m11, m12, m13, m21, m22, m23, m31, m32, m33);
        double t01 = -determinant3x3(m10, m12, m13, m20, m22, m23, m30, m32, m33);
        double t02 =  determinant3x3(m10, m11, m13, m20, m21, m23, m30, m31, m33);
        double t03 = -determinant3x3(m10, m11, m12, m20, m21, m22, m30, m31, m32);

        // second row
        double t10 = -determinant3x3(m01, m02, m03, m21, m22, m23, m31, m32, m33);
        double t11 =  determinant3x3(m00, m02, m03, m20, m22, m23, m30, m32, m33);
        double t12 = -determinant3x3(m00, m01, m03, m20, m21, m23, m30, m31, m33);
        double t13 =  determinant3x3(m00, m01, m02, m20, m21, m22, m30, m31, m32);

        // third row
        double t20 =  determinant3x3(m01, m02, m03, m11, m12, m13, m31, m32, m33);
        double t21 = -determinant3x3(m00, m02, m03, m10, m12, m13, m30, m32, m33);
        double t22 =  determinant3x3(m00, m01, m03, m10, m11, m13, m30, m31, m33);
        double t23 = -determinant3x3(m00, m01, m02, m10, m11, m12, m30, m31, m32);

        // fourth row
        double t30 = -determinant3x3(m01, m02, m03, m11, m12, m13, m21, m22, m23);
        double t31 =  determinant3x3(m00, m02, m03, m10, m12, m13, m20, m22, m23);
        double t32 = -determinant3x3(m00, m01, m03, m10, m11, m13, m20, m21, m23);
        double t33 =  determinant3x3(m00, m01, m02, m10, m11, m12, m20, m21, m22);

        // transpose and divide by the determinant
        m00 = t00 / determinant;
        m01 = t10 / determinant;
        m02 = t20 / determinant;
        m03 = t30 / determinant;

        m10 = t01 / determinant;
        m11 = t11 / determinant;
        m12 = t21 / determinant;
        m13 = t31 / determinant;

        m20 = t02 / determinant;
        m21 = t12 / determinant;
        m22 = t22 / determinant;
        m23 = t32 / determinant;

        m30 = t03 / determinant;
        m31 = t13 / determinant;
        m32 = t23 / determinant;
        m33 = t33 / determinant;

        return true;
    }

    /**
     * Calculate the determinant of a 3x3 matrix.
     * @return result
     */
    private double determinant3x3(double t00, double t01, double t02,
                                  double t10, double t11, double t12,
                                  double t20, double t21, double t22) {
        return (t00 * (t11 * t22 - t12 * t21) +
                t01 * (t12 * t20 - t10 * t22) +
                t02 * (t10 * t21 - t11 * t20));
    }

    /**
     * @return the determinant of the matrix
     */
    @Override
    public double determinant() {
        double f = m00
            * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32)
                - m13 * m22 * m31
                - m11 * m23 * m32
                - m12 * m21 * m33);
        f -= m01
            * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32)
                - m13 * m22 * m30
                - m10 * m23 * m32
                - m12 * m20 * m33);
        f += m02
            * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31)
                - m13 * m21 * m30
                - m10 * m23 * m31
                - m11 * m20 * m33);
        f -= m03
            * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31)
                - m12 * m21 * m30
                - m10 * m22 * m31
                - m11 * m20 * m32);
        return f;
    }

    //////////////////////////////////////////////////////////////

    // REVERSE VERSIONS OF MATRIX OPERATIONS

    // These functions should not be used, as they will be removed in the future.

    public void invTranslate(double tx, double ty, double tz) {
        preApply(1, 0, 0, -tx,
                 0, 1, 0, -ty,
                 0, 0, 1, -tz,
                 0, 0, 0, 1);
    }

    public void invRotateX(double angle) {
        double c = Math.cos(-angle);
        double s = Math.sin(-angle);
        preApply(1, 0, 0, 0,  0, c, -s, 0,  0, s, c, 0,  0, 0, 0, 1);
    }

    public void invRotateY(double angle) {
        double c = Math.cos(-angle);
        double s = Math.sin(-angle);
        preApply(c, 0, s, 0,  0, 1, 0, 0,  -s, 0, c, 0,  0, 0, 0, 1);
    }

    public void invRotateZ(double angle) {
        double c = Math.cos(-angle);
        double s = Math.sin(-angle);
        preApply(c, -s, 0, 0,  s, c, 0, 0,  0, 0, 1, 0,  0, 0, 0, 1);
    }

    public void invRotate(double angle, double v0, double v1, double v2) {
        //TODO should make sure this vector is normalized

        double c = Math.cos(-angle);
        double s = Math.sin(-angle);
        double t = 1.0f - c;

        preApply((t*v0*v0) + c, (t*v0*v1) - (s*v2), (t*v0*v2) + (s*v1), 0,
                 (t*v0*v1) + (s*v2), (t*v1*v1) + c, (t*v1*v2) - (s*v0), 0,
                 (t*v0*v2) - (s*v1), (t*v1*v2) + (s*v0), (t*v2*v2) + c, 0,
                 0, 0, 0, 1);
    }

    public void invScale(double x, double y, double z) {
        preApply(1/x, 0, 0, 0,  0, 1/y, 0, 0,  0, 0, 1/z, 0,  0, 0, 0, 1);
    }

    public boolean invApply(double n00, double n01, double n02, double n03,
                            double n10, double n11, double n12, double n13,
                            double n20, double n21, double n22, double n23,
                            double n30, double n31, double n32, double n33) {
        if (inverseCopy == null) {
            inverseCopy = new Matrix3D();
        }
        inverseCopy.set(n00, n01, n02, n03,
                        n10, n11, n12, n13,
                        n20, n21, n22, n23,
                        n30, n31, n32, n33);
        if (!inverseCopy.invert()) {
            return false;
        }
        preApply(inverseCopy);
        return true;
    }

    //////////////////////////////////////////////////////////////

    public void print() {
        /*
        System.out.println(m00 + " " + m01 + " " + m02 + " " + m03 + "\n" +
                           m10 + " " + m11 + " " + m12 + " " + m13 + "\n" +
                           m20 + " " + m21 + " " + m22 + " " + m23 + "\n" +
                           m30 + " " + m31 + " " + m32 + " " + m33 + "\n");
         */
        int big = (int) Math.abs(Math.max(Math.max(Math.max(Math.max(Math.abs(m00), Math.abs(m01)),
            Math.max(Math.abs(m02), Math.abs(m03))),
            Math.max(Math.max(Math.abs(m10), Math.abs(m11)),
                Math.max(Math.abs(m12), Math.abs(m13)))),
                Math.max(Math.max(Math.max(Math.abs(m20), Math.abs(m21)),
                    Math.max(Math.abs(m22), Math.abs(m23))),
                    Math.max(Math.max(Math.abs(m30), Math.abs(m31)),
                        Math.max(Math.abs(m32), Math.abs(m33))))));

        int digits = 1;
        if (Double.isNaN(big) || Double.isInfinite(big)) {  // avoid infinite loop
            digits = 5;
        } else {
            while ((big /= 10) != 0) digits++;  // cheap log()
        }

        System.out.println(nfs(m00, digits, 4) + " " +
                           nfs(m01, digits, 4) + " " +
                           nfs(m02, digits, 4) + " " +
                           nfs(m03, digits, 4));

        System.out.println(nfs(m10, digits, 4) + " " +
                           nfs(m11, digits, 4) + " " +
                           nfs(m12, digits, 4) + " " +
                           nfs(m13, digits, 4));

        System.out.println(nfs(m20, digits, 4) + " " +
                           nfs(m21, digits, 4) + " " +
                           nfs(m22, digits, 4) + " " +
                           nfs(m23, digits, 4));

        System.out.println(nfs(m30, digits, 4) + " " +
                           nfs(m31, digits, 4) + " " +
                           nfs(m32, digits, 4) + " " +
                           nfs(m33, digits, 4));

        System.out.println();
    }

    //////////////////////////////////////////////////////////////
    static private NumberFormat float_nf;
    static private int float_nf_left, float_nf_right;
    static private boolean float_nf_commas;

    private final String nf(double num, int left, int right) {
        if ((float_nf != null) &&
            (float_nf_left == left) &&
            (float_nf_right == right) &&
            !float_nf_commas) {
            return float_nf.format(num);
        }

        float_nf = NumberFormat.getInstance();
        float_nf.setGroupingUsed(false);
        float_nf_commas = false;

        if (left != 0) float_nf.setMinimumIntegerDigits(left);
        if (right != 0) {
            float_nf.setMinimumFractionDigits(right);
            float_nf.setMaximumFractionDigits(right);
        }
        float_nf_left = left;
        float_nf_right = right;
        return float_nf.format(num);
    }

    private final String nfs(double num, int left, int right) {
        return (num < 0) ? nf(num, left, right) :  (' ' + nf(num, left, right));
    }
}
