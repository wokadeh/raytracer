package utils;

public class Matrix4 {

	private Matrix mBaseMatrix;

	public Matrix4(){
		mBaseMatrix = Matrix.identity(4,4);
	}

	private Matrix4(Matrix mat){
		mBaseMatrix = mat;
	}

	public Matrix4 translate( Vec2 vec ){
		Matrix4 out = new Matrix4(mBaseMatrix);

		out.setValueAt( 0, 3, vec.x );
		out.setValueAt( 1, 3, vec.y );

		return out;
	}

	public Matrix4 translate( Vec3 vec ){
		Matrix4 out = new Matrix4(mBaseMatrix);

		out.setValueAt( 0, 3, vec.x );
		out.setValueAt( 1, 3, vec.y );
		out.setValueAt( 2, 3, vec.z );

		return out;
	}

	public Matrix4 translate( Vec4 vec ){
		Matrix4 out = new Matrix4(mBaseMatrix);

		out.setValueAt( 0, 3, vec.x );
		out.setValueAt( 1, 3, vec.y );
		out.setValueAt( 2, 3, vec.z );
		out.setValueAt( 3, 3, vec.z );

		return out;
	}

	public Matrix4 scale( Vec2 vec ){
		Matrix4 out = new Matrix4(mBaseMatrix);

		out.setValueAt( 0, 0, vec.x );
		out.setValueAt( 1, 1, vec.y );

		return out;
	}

	public Matrix4 scale( Vec3 vec ){
		Matrix4 out = new Matrix4(mBaseMatrix);

		out.setValueAt( 0, 0, vec.x );
		out.setValueAt( 1, 1, vec.y );
		out.setValueAt( 2, 2, vec.z );

		return out;
	}

	public Matrix4 scale( Vec4 vec ){
		Matrix4 out = new Matrix4(mBaseMatrix);

		out.setValueAt( 0, 0, vec.x );
		out.setValueAt( 1, 1, vec.y );
		out.setValueAt( 2, 2, vec.z );
		out.setValueAt( 3, 2, vec.z );

		return out;
	}

	public Matrix4 transpose(){
		return new Matrix4( mBaseMatrix.transpose() );
	}

	public Matrix4 invert(){
		return new Matrix4( mBaseMatrix.inverse() );
	}

	public void setValueAt(int row, int col, double value){
		mBaseMatrix.set( row, col, value );
	}

	public double getValueAt(int row, int col){
		return mBaseMatrix.get(row, col);
	}

	public Vec4 multVec4(Vec4 vec){
		return new Vec4(
				vec.x * ( float ) this.getValueAt(0,0) + vec.y * ( float ) this.getValueAt(0,1) + vec.z * ( float ) this.getValueAt(0,2) + vec.w * ( float ) this.getValueAt(0,3),
				vec.x * ( float ) this.getValueAt(1,0) + vec.y * ( float ) this.getValueAt(1,1) + vec.z * ( float ) this.getValueAt(1,2) + vec.w * ( float ) this.getValueAt(1,3),
				vec.x * ( float ) this.getValueAt(2,0) + vec.y * ( float ) this.getValueAt(2,1) + vec.z * ( float ) this.getValueAt(2,2) + vec.w * ( float ) this.getValueAt(2,3),
				vec.x * ( float ) this.getValueAt(3,0) + vec.y * ( float ) this.getValueAt(3,1) + vec.z * ( float ) this.getValueAt(3,2) + vec.w * ( float ) this.getValueAt(3,3)
		);
	}

	public Vec3 multVec3(Vec3 vec){
		Vec4 out = new Vec4(
				vec.x * ( float ) this.getValueAt(0,0) + vec.y * ( float ) this.getValueAt(0,1) + vec.z * ( float ) this.getValueAt(0,2) + ( float ) this.getValueAt(0,3),
				vec.x * ( float ) this.getValueAt(1,0) + vec.y * ( float ) this.getValueAt(1,1) + vec.z * ( float ) this.getValueAt(1,2) + ( float ) this.getValueAt(1,3),
				vec.x * ( float ) this.getValueAt(2,0) + vec.y * ( float ) this.getValueAt(2,1) + vec.z * ( float ) this.getValueAt(2,2) + ( float ) this.getValueAt(2,3),
				vec.x * ( float ) this.getValueAt(3,0) + vec.y * ( float ) this.getValueAt(3,1) + vec.z * ( float ) this.getValueAt(3,2) + ( float ) this.getValueAt(3,3)
		);

		return new Vec3(out.x, out.y, out.z);
	}

	@Override
	public String toString(){
		return  "\n" +
				this.getValueAt(0,0) + "\t\t\t\t\t\t" + this.getValueAt(0,1) + "\t\t\t\t\t\t" + this.getValueAt(0,2) + "\t\t\t\t\t\t" + this.getValueAt(0,3) + "\t\t\n" +
				this.getValueAt(1,0) + "\t\t\t\t\t\t" + this.getValueAt(1,1) + "\t\t\t\t\t\t" + this.getValueAt(1,2) + "\t\t\t\t\t\t" + this.getValueAt(1,3) + "\t\t\n" +
				this.getValueAt(2,0) + "\t\t\t\t\t\t" + this.getValueAt(2,1) + "\t\t\t\t\t\t" + this.getValueAt(2,2) + "\t\t\t\t\t\t" + this.getValueAt(2,3) + "\t\t\n" +
				this.getValueAt(3,0) + "\t\t\t\t\t\t" + this.getValueAt(3,1) + "\t\t\t\t\t\t" + this.getValueAt(3,2) + "\t\t\t\t\t\t" + this.getValueAt(3,3) + "\t\t\n" ;
	}

	public static class Matrix implements Cloneable, java.io.Serializable {

		private double[][] A;
		private int m, n;

		public Matrix (int m, int n) {
			this.m = m;
			this.n = n;
			A = new double[m][n];
		}

		public Matrix (int m, int n, double s) {
			this.m = m;
			this.n = n;
			A = new double[m][n];
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					A[i][j] = s;
				}
			}
		}

		public Matrix (double[][] A) {
			m = A.length;
			n = A[0].length;
			for (int i = 0; i < m; i++) {
				if (A[i].length != n) {
					throw new IllegalArgumentException("All rows must have the same length.");
				}
			}
			this.A = A;
		}

		public Matrix (double[][] A, int m, int n) {
			this.A = A;
			this.m = m;
			this.n = n;
		}

		public Matrix (double vals[], int m) {
			this.m = m;
			n = (m != 0 ? vals.length/m : 0);
			if (m*n != vals.length) {
				throw new IllegalArgumentException("Array length must be a multiple of m.");
			}
			A = new double[m][n];
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					A[i][j] = vals[i+j*m];
				}
			}
		}

		public static Matrix constructWithCopy(double[][] A) {
			int m = A.length;
			int n = A[0].length;
			Matrix X = new Matrix(m,n);
			double[][] C = X.getArray();
			for (int i = 0; i < m; i++) {
				if (A[i].length != n) {
					throw new IllegalArgumentException
							("All rows must have the same length.");
				}
				for (int j = 0; j < n; j++) {
					C[i][j] = A[i][j];
				}
			}
			return X;
		}

		public Matrix copy () {
			Matrix X = new Matrix(m,n);
			double[][] C = X.getArray();
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					C[i][j] = A[i][j];
				}
			}
			return X;
		}

		public Object clone () {
			return this.copy();
		}

		public double[][] getArray () {
			return A;
		}

		public double[][] getArrayCopy () {
			double[][] C = new double[m][n];
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					C[i][j] = A[i][j];
				}
			}
			return C;
		}

		public double[] getColumnPackedCopy () {
			double[] vals = new double[m*n];
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					vals[i+j*m] = A[i][j];
				}
			}
			return vals;
		}

		public double[] getRowPackedCopy () {
			double[] vals = new double[m*n];
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					vals[i*n+j] = A[i][j];
				}
			}
			return vals;
		}

		public int getRowDimension () {
			return m;
		}

		public int getColumnDimension () {
			return n;
		}

		public double get (int i, int j) {
			return A[i][j];
		}

		public Matrix getMatrix (int i0, int i1, int j0, int j1) {
			Matrix X = new Matrix(i1-i0+1,j1-j0+1);
			double[][] B = X.getArray();
			try {
				for (int i = i0; i <= i1; i++) {
					for (int j = j0; j <= j1; j++) {
						B[i-i0][j-j0] = A[i][j];
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException("Submatrix indices");
			}
			return X;
		}

		public Matrix getMatrix (int[] r, int[] c) {
			Matrix X = new Matrix(r.length,c.length);
			double[][] B = X.getArray();
			try {
				for (int i = 0; i < r.length; i++) {
					for (int j = 0; j < c.length; j++) {
						B[i][j] = A[r[i]][c[j]];
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException("Submatrix indices");
			}
			return X;
		}

		public Matrix getMatrix (int i0, int i1, int[] c) {
			Matrix X = new Matrix(i1-i0+1,c.length);
			double[][] B = X.getArray();
			try {
				for (int i = i0; i <= i1; i++) {
					for (int j = 0; j < c.length; j++) {
						B[i-i0][j] = A[i][c[j]];
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException("Submatrix indices");
			}
			return X;
		}

		public Matrix getMatrix (int[] r, int j0, int j1) {
			Matrix X = new Matrix(r.length,j1-j0+1);
			double[][] B = X.getArray();
			try {
				for (int i = 0; i < r.length; i++) {
					for (int j = j0; j <= j1; j++) {
						B[i][j-j0] = A[r[i]][j];
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException("Submatrix indices");
			}
			return X;
		}

		public void set (int i, int j, double s) {
			A[i][j] = s;
		}

		public void setMatrix (int i0, int i1, int j0, int j1, Matrix X) {
			try {
				for (int i = i0; i <= i1; i++) {
					for (int j = j0; j <= j1; j++) {
						A[i][j] = X.get(i-i0,j-j0);
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException("Submatrix indices");
			}
		}

		public void setMatrix (int[] r, int[] c, Matrix X) {
			try {
				for (int i = 0; i < r.length; i++) {
					for (int j = 0; j < c.length; j++) {
						A[r[i]][c[j]] = X.get(i,j);
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException("Submatrix indices");
			}
		}

		public void setMatrix (int[] r, int j0, int j1, Matrix X) {
			try {
				for (int i = 0; i < r.length; i++) {
					for (int j = j0; j <= j1; j++) {
						A[r[i]][j] = X.get(i,j-j0);
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException("Submatrix indices");
			}
		}

		public void setMatrix (int i0, int i1, int[] c, Matrix X) {
			try {
				for (int i = i0; i <= i1; i++) {
					for (int j = 0; j < c.length; j++) {
						A[i][c[j]] = X.get(i-i0,j);
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException("Submatrix indices");
			}
		}

		public Matrix transpose () {
			Matrix X = new Matrix(n,m);
			double[][] C = X.getArray();
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					C[j][i] = A[i][j];
				}
			}
			return X;
		}

		public double trace () {
			double t = 0;
			for (int i = 0; i < Math.min(m,n); i++) {
				t += A[i][i];
			}
			return t;
		}

		public static Matrix random (int m, int n) {
			Matrix A = new Matrix(m,n);
			double[][] X = A.getArray();
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					X[i][j] = Math.random();
				}
			}
			return A;
		}

		public static Matrix identity (int m, int n) {
			Matrix A = new Matrix(m,n);
			double[][] X = A.getArray();
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					X[i][j] = (i == j ? 1.0 : 0.0);
				}
			}
			return A;
		}

		private void checkMatrixDimensions (Matrix B) {
			if (B.m != m || B.n != n) {
				throw new IllegalArgumentException("Matrix dimensions must agree.");
			}
		}

		public Matrix solve (Matrix B) {
			return (m == n ? (new LUDecomposition(this)).solve(B) :
					(new QRDecomposition(this)).solve(B));
		}

		public Matrix solveTranspose (Matrix B) {
			return transpose().solve(B.transpose());
		}

		public Matrix inverse () {
			return solve(identity(m,m));
		}

		public double det () {
			return new LUDecomposition(this).det();
		}

		private static final long serialVersionUID = 1;
	}

	private static class Maths {

		public static double hypot(double a, double b) {
			double r;
			if (Math.abs(a) > Math.abs(b)) {
				r = b/a;
				r = Math.abs(a)*Math.sqrt(1+r*r);
			} else if (b != 0) {
				r = a/b;
				r = Math.abs(b)*Math.sqrt(1+r*r);
			} else {
				r = 0.0;
			}
			return r;
		}
	}
	public static class LUDecomposition implements java.io.Serializable {

		private double[][] LU;
		private int m, n, pivsign;
		private int[] piv;

		public LUDecomposition (Matrix A) {

			// Use a "left-looking", dot-product, Crout/Doolittle algorithm.

			LU = A.getArrayCopy();
			m = A.getRowDimension();
			n = A.getColumnDimension();
			piv = new int[m];
			for (int i = 0; i < m; i++) {
				piv[i] = i;
			}
			pivsign = 1;
			double[] LUrowi;
			double[] LUcolj = new double[m];

			// Outer loop.

			for (int j = 0; j < n; j++) {

				// Make a copy of the j-th column to localize references.

				for (int i = 0; i < m; i++) {
					LUcolj[i] = LU[i][j];
				}

				// Apply previous transformations.

				for (int i = 0; i < m; i++) {
					LUrowi = LU[i];

					// Most of the time is spent in the following dot product.

					int kmax = Math.min(i,j);
					double s = 0.0;
					for (int k = 0; k < kmax; k++) {
						s += LUrowi[k]*LUcolj[k];
					}

					LUrowi[j] = LUcolj[i] -= s;
				}

				// Find pivot and exchange if necessary.

				int p = j;
				for (int i = j+1; i < m; i++) {
					if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p])) {
						p = i;
					}
				}
				if (p != j) {
					for (int k = 0; k < n; k++) {
						double t = LU[p][k]; LU[p][k] = LU[j][k]; LU[j][k] = t;
					}
					int k = piv[p]; piv[p] = piv[j]; piv[j] = k;
					pivsign = -pivsign;
				}

				// Compute multipliers.

				if (j < m & LU[j][j] != 0.0) {
					for (int i = j+1; i < m; i++) {
						LU[i][j] /= LU[j][j];
					}
				}
			}
		}

		public boolean isNonsingular () {
			for (int j = 0; j < n; j++) {
				if (LU[j][j] == 0)
					return false;
			}
			return true;
		}

		/** Return lower triangular factor
		 @return     L
		 */

		public Matrix getL () {
			Matrix X = new Matrix(m,n);
			double[][] L = X.getArray();
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					if (i > j) {
						L[i][j] = LU[i][j];
					} else if (i == j) {
						L[i][j] = 1.0;
					} else {
						L[i][j] = 0.0;
					}
				}
			}
			return X;
		}

		/** Return upper triangular factor
		 @return     U
		 */

		public Matrix getU () {
			Matrix X = new Matrix(n,n);
			double[][] U = X.getArray();
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (i <= j) {
						U[i][j] = LU[i][j];
					} else {
						U[i][j] = 0.0;
					}
				}
			}
			return X;
		}

		/** Return pivot permutation vector
		 @return     piv
		 */

		public int[] getPivot () {
			int[] p = new int[m];
			for (int i = 0; i < m; i++) {
				p[i] = piv[i];
			}
			return p;
		}

		/** Return pivot permutation vector as a one-dimensional double array
		 @return     (double) piv
		 */

		public double[] getDoublePivot () {
			double[] vals = new double[m];
			for (int i = 0; i < m; i++) {
				vals[i] = (double) piv[i];
			}
			return vals;
		}

		/** Determinant
		 @return     det(A)
		 @exception  IllegalArgumentException  Matrix must be square
		 */

		public double det () {
			if (m != n) {
				throw new IllegalArgumentException("Matrix must be square.");
			}
			double d = (double) pivsign;
			for (int j = 0; j < n; j++) {
				d *= LU[j][j];
			}
			return d;
		}

		/** Solve A*X = B
		 @param  B   A Matrix with as many rows as A and any number of columns.
		 @return     X so that L*U*X = B(piv,:)
		 @exception  IllegalArgumentException Matrix row dimensions must agree.
		 @exception  RuntimeException  Matrix is singular.
		 */

		public Matrix solve (Matrix B) {
			if (B.getRowDimension() != m) {
				throw new IllegalArgumentException("Matrix row dimensions must agree.");
			}
			if (!this.isNonsingular()) {
				throw new RuntimeException("Matrix is singular.");
			}

			// Copy right hand side with pivoting
			int nx = B.getColumnDimension();
			Matrix Xmat = B.getMatrix(piv,0,nx-1);
			double[][] X = Xmat.getArray();

			// Solve L*Y = B(piv,:)
			for (int k = 0; k < n; k++) {
				for (int i = k+1; i < n; i++) {
					for (int j = 0; j < nx; j++) {
						X[i][j] -= X[k][j]*LU[i][k];
					}
				}
			}
			// Solve U*X = Y;
			for (int k = n-1; k >= 0; k--) {
				for (int j = 0; j < nx; j++) {
					X[k][j] /= LU[k][k];
				}
				for (int i = 0; i < k; i++) {
					for (int j = 0; j < nx; j++) {
						X[i][j] -= X[k][j]*LU[i][k];
					}
				}
			}
			return Xmat;
		}
		private static final long serialVersionUID = 1;
	}

	public static class QRDecomposition implements java.io.Serializable {

/* ------------------------
   Class variables
 * ------------------------ */

		/** Array for internal storage of decomposition.
		 @serial internal array storage.
		 */
		private double[][] QR;

		/** Row and column dimensions.
		 @serial column dimension.
		 @serial row dimension.
		 */
		private int m, n;

		/** Array for internal storage of diagonal of R.
		 @serial diagonal of R.
		 */
		private double[] Rdiag;

/* ------------------------
   Constructor
 * ------------------------ */

		/** QR Decomposition, computed by Householder reflections.
		 Structure to access R and the Householder vectors and compute Q.
		 @param A    Rectangular matrix
		 */

		public QRDecomposition (Matrix A) {
			// Initialize.
			QR = A.getArrayCopy();
			m = A.getRowDimension();
			n = A.getColumnDimension();
			Rdiag = new double[n];

			// Main loop.
			for (int k = 0; k < n; k++) {
				// Compute 2-norm of k-th column without under/overflow.
				double nrm = 0;
				for (int i = k; i < m; i++) {
					nrm = Maths.hypot(nrm,QR[i][k]);
				}

				if (nrm != 0.0) {
					// Form k-th Householder vector.
					if (QR[k][k] < 0) {
						nrm = -nrm;
					}
					for (int i = k; i < m; i++) {
						QR[i][k] /= nrm;
					}
					QR[k][k] += 1.0;

					// Apply transformation to remaining columns.
					for (int j = k+1; j < n; j++) {
						double s = 0.0;
						for (int i = k; i < m; i++) {
							s += QR[i][k]*QR[i][j];
						}
						s = -s/QR[k][k];
						for (int i = k; i < m; i++) {
							QR[i][j] += s*QR[i][k];
						}
					}
				}
				Rdiag[k] = -nrm;
			}
		}

		public boolean isFullRank () {
			for (int j = 0; j < n; j++) {
				if (Rdiag[j] == 0)
					return false;
			}
			return true;
		}

		/** Return the Householder vectors
		 @return     Lower trapezoidal matrix whose columns define the reflections
		 */

		public Matrix getH () {
			Matrix X = new Matrix(m,n);
			double[][] H = X.getArray();
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					if (i >= j) {
						H[i][j] = QR[i][j];
					} else {
						H[i][j] = 0.0;
					}
				}
			}
			return X;
		}

		/** Return the upper triangular factor
		 @return     R
		 */

		public Matrix getR () {
			Matrix X = new Matrix(n,n);
			double[][] R = X.getArray();
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (i < j) {
						R[i][j] = QR[i][j];
					} else if (i == j) {
						R[i][j] = Rdiag[i];
					} else {
						R[i][j] = 0.0;
					}
				}
			}
			return X;
		}

		/** Generate and return the (economy-sized) orthogonal factor
		 @return     Q
		 */

		public Matrix getQ () {
			Matrix X = new Matrix(m,n);
			double[][] Q = X.getArray();
			for (int k = n-1; k >= 0; k--) {
				for (int i = 0; i < m; i++) {
					Q[i][k] = 0.0;
				}
				Q[k][k] = 1.0;
				for (int j = k; j < n; j++) {
					if (QR[k][k] != 0) {
						double s = 0.0;
						for (int i = k; i < m; i++) {
							s += QR[i][k]*Q[i][j];
						}
						s = -s/QR[k][k];
						for (int i = k; i < m; i++) {
							Q[i][j] += s*QR[i][k];
						}
					}
				}
			}
			return X;
		}

		/** Least squares solution of A*X = B
		 @param B    A Matrix with as many rows as A and any number of columns.
		 @return     X that minimizes the two norm of Q*R*X-B.
		 @exception  IllegalArgumentException  Matrix row dimensions must agree.
		 @exception  RuntimeException  Matrix is rank deficient.
		 */

		public Matrix solve (Matrix B) {
			if (B.getRowDimension() != m) {
				throw new IllegalArgumentException("Matrix row dimensions must agree.");
			}
			if (!this.isFullRank()) {
				throw new RuntimeException("Matrix is rank deficient.");
			}

			// Copy right hand side
			int nx = B.getColumnDimension();
			double[][] X = B.getArrayCopy();

			// Compute Y = transpose(Q)*B
			for (int k = 0; k < n; k++) {
				for (int j = 0; j < nx; j++) {
					double s = 0.0;
					for (int i = k; i < m; i++) {
						s += QR[i][k]*X[i][j];
					}
					s = -s/QR[k][k];
					for (int i = k; i < m; i++) {
						X[i][j] += s*QR[i][k];
					}
				}
			}
			// Solve R*X = Y;
			for (int k = n-1; k >= 0; k--) {
				for (int j = 0; j < nx; j++) {
					X[k][j] /= Rdiag[k];
				}
				for (int i = 0; i < k; i++) {
					for (int j = 0; j < nx; j++) {
						X[i][j] -= X[k][j]*QR[i][k];
					}
				}
			}
			return (new Matrix(X,n,nx).getMatrix(0,n-1,0,nx-1));
		}
		private static final long serialVersionUID = 1;
	}
//	private static class Matrix {
//
//		private int nrows;
//		private int ncols;
//		private double[][] data;
//
//		public Matrix(double[][] dat) {
//			this.data = dat;
//			this.nrows = dat.length;
//			this.ncols = dat[0].length;
//		}
//
//		public Matrix(int nrow, int ncol) {
//			this.nrows = nrow;
//			this.ncols = ncol;
//			data = new double[nrow][ncol];
//		}
//
//		public int getNrows() {
//			return nrows;
//		}
//
//		public void setNrows(int nrows) {
//			this.nrows = nrows;
//		}
//
//		public int getNcols() {
//			return ncols;
//		}
//
//		public void setNcols(int ncols) {
//			this.ncols = ncols;
//		}
//
//		public double[][] getValues() {
//			return data;
//		}
//
//		public void setValues(double[][] values) {
//			this.data = values;
//		}
//
//		public void setValueAt(int row, int col, double value) {
//			data[row][col] = value;
//		}
//
//		public double getValueAt(int row, int col) {
//			return data[row][col];
//		}
//
//		public boolean isSquare() {
//			return nrows == ncols;
//		}
//
//		public int size() {
//			if (isSquare())
//				return nrows;
//			return -1;
//		}
//
//		public static Matrix initializeUnitMatrix(int rows, int cols) {
//			Matrix mat = new Matrix(rows, cols);
//			for (int i = 0; i < rows; i++) {
//				for (int j = 0; j < cols; j++) {
//					if(i == j){
//						mat.setValueAt(i, j, 1);
//					}
//					else {
//						mat.setValueAt(i, j, 0);
//					}
//				}
//			}
//			return mat;
//		}
//
//		public Matrix multiplyByConstant(double constant) {
//			Matrix mat = new Matrix(nrows, ncols);
//			for (int i = 0; i < nrows; i++) {
//				for (int j = 0; j < ncols; j++) {
//					mat.setValueAt(i, j, data[i][j] * constant);
//				}
//			}
//			return mat;
//		}
//
//		public Matrix insertColumnWithValue1() {
//			Matrix X_ = new Matrix(this.getNrows(), this.getNcols()+1);
//			for (int i=0;i<X_.getNrows();i++) {
//				for (int j=0;j<X_.getNcols();j++) {
//					if (j==0)
//						X_.setValueAt(i, j, 1.0);
//					else
//						X_.setValueAt(i, j, this.getValueAt(i, j-1));
//
//				}
//			}
//			return X_;
//		}
//	}
//
//	private static class MatrixTransformation {
//
//		private MatrixTransformation() {}
//
//		public static Matrix transpose(Matrix matrix) {
//			Matrix transposedMatrix = new Matrix(matrix.getNcols(), matrix.getNrows());
//			for (int i = 0; i < matrix.getNrows(); i++) {
//				for (int j = 0; j < matrix.getNcols(); j++) {
//					transposedMatrix.setValueAt(j, i, matrix.getValueAt(i, j));
//				}
//			}
//			return transposedMatrix;
//		}
//		public static Matrix inverse(Matrix matrix) throws NoSquareException {
//			return transpose(cofactor(matrix));//(transpose(cofactor(matrix)).multiplyByConstant(1.0 / determinant(matrix)));
//		}
//
//		public static double determinant(Matrix matrix) throws NoSquareException {
//			if (!matrix.isSquare())
//				throw new NoSquareException("Matrix must be square.");
//			if (matrix.size() == 1) {
//				return matrix.getValueAt(0, 0);
//			}
//
//			if (matrix.size() == 2) {
//				return (matrix.getValueAt(0, 0) * matrix.getValueAt(1, 1)) - (matrix.getValueAt(0, 1) * matrix.getValueAt(1, 0));
//			}
//			double sum = 0.0;
//			for (int i = 0; i < matrix.getNcols(); i++) {
//				sum += changeSign(i) * matrix.getValueAt(0, i) * determinant(createSubMatrix(matrix, 0, i));
//			}
//			return sum;
//		}
//
//		private static int changeSign(int i) {
//			if (i % 2 == 0)
//				return 1;
//			return -1;
//		}
//
//		public static Matrix createSubMatrix(Matrix matrix, int excluding_row, int excluding_col) {
//			Matrix mat = new Matrix(matrix.getNrows() - 1, matrix.getNcols() - 1);
//			int r = -1;
//			for (int i = 0; i < matrix.getNrows(); i++) {
//				if (i == excluding_row)
//					continue;
//				r++;
//				int c = -1;
//				for (int j = 0; j < matrix.getNcols(); j++) {
//					if (j == excluding_col)
//						continue;
//					mat.setValueAt(r, ++c, matrix.getValueAt(i, j));
//				}
//			}
//			return mat;
//		}
//
//		public static Matrix cofactor(Matrix matrix) throws NoSquareException {
//			Matrix mat = new Matrix(matrix.getNrows(), matrix.getNcols());
//			for (int i = 0; i < matrix.getNrows(); i++) {
//				for (int j = 0; j < matrix.getNcols(); j++) {
//					mat.setValueAt(i, j, changeSign(i) * changeSign(j) * determinant(matrix));
//				}
//			}
//
//			return mat;
//		}
//
//		public static Matrix add(Matrix matrix1, Matrix matrix2) throws IllegalDimensionException {
//			if (matrix1.getNcols() != matrix2.getNcols() || matrix1.getNrows() != matrix2.getNrows())
//				throw new IllegalDimensionException("Two matrices should be the same dimension.");
//			Matrix sumMatrix = new Matrix(matrix1.getNrows(), matrix1.getNcols());
//			for (int i = 0; i < matrix1.getNrows(); i++) {
//				for (int j = 0; j < matrix1.getNcols(); j++)
//					sumMatrix.setValueAt(i, j, matrix1.getValueAt(i, j) + matrix2.getValueAt(i, j));
//
//			}
//			return sumMatrix;
//		}
//
//		public static Matrix subtract(Matrix matrix1, Matrix matrix2) throws IllegalDimensionException {
//			return add(matrix1, matrix2.multiplyByConstant(-1));
//		}
//
//		public static Matrix multiply(Matrix matrix1, Matrix matrix2) {
//			Matrix multipliedMatrix = new Matrix(matrix1.getNrows(), matrix2.getNcols());
//
//			for (int i = 0; i < multipliedMatrix.getNrows(); i++) {
//				for (int j = 0; j < multipliedMatrix.getNcols(); j++) {
//					double sum = 0.0;
//					for (int k = 0; k < matrix1.getNcols(); k++) {
//						sum += matrix1.getValueAt(i, k) * matrix2.getValueAt(k, j);
//					}
//					multipliedMatrix.setValueAt(i, j, sum);
//				}
//			}
//			return multipliedMatrix;
//		}
//	}
//
//	public static class IllegalDimensionException extends Exception {
//		public IllegalDimensionException() {
//			super();
//			Log.error(this, "Dimension does not fit!");
//		}
//
//		public IllegalDimensionException(String message) {
//			super(message);
//			Log.error(this, "Dimension does not fit: " + message);
//		}
//	}
//
//	private static class NoSquareException extends Exception {
//
//		public NoSquareException() {
//			super();
//			Log.error(this, "The input is not square!");
//		}
//
//		public NoSquareException(String message) {
//			super(message);
//			Log.error(this, "The input is not square: " + message);
//		}
//
//	}
}
