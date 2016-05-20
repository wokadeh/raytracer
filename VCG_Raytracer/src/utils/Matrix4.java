package utils;

public class Matrix4 {

	private Matrix mBaseMatrix;

	public Matrix4(){
		mBaseMatrix = Matrix.initializeUnitMatrix(4, 4);
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
		return new Matrix4(MatrixTransformation.transpose(mBaseMatrix));
	}

	public Matrix4 invert(){
		try {
			return new Matrix4( MatrixTransformation.inverse(mBaseMatrix) );
		} catch (NoSquareException e) {
			Log.error(this, "The given matrix cannot be inverted: " + e.getMessage() );
		}
		return this;
	}

	public void setValueAt(int row, int col, double value){
		mBaseMatrix.setValueAt( row, col, value );
	}

	public double getValueAt(int row, int col){
		return mBaseMatrix.getValueAt(row, col);
	}

	public Vec4 multVec4(Vec4 vec){
		return new Vec4(
				vec.x * ( float ) mBaseMatrix.getValueAt(0,0) + vec.y * ( float ) mBaseMatrix.getValueAt(0,1) + vec.z * ( float ) mBaseMatrix.getValueAt(0,2) + vec.w * ( float ) mBaseMatrix.getValueAt(0,3),
				vec.x * ( float ) mBaseMatrix.getValueAt(1,0) + vec.y * ( float ) mBaseMatrix.getValueAt(1,1) + vec.z * ( float ) mBaseMatrix.getValueAt(1,2) + vec.w * ( float ) mBaseMatrix.getValueAt(1,3),
				vec.x * ( float ) mBaseMatrix.getValueAt(2,0) + vec.y * ( float ) mBaseMatrix.getValueAt(2,1) + vec.z * ( float ) mBaseMatrix.getValueAt(2,2) + vec.w * ( float ) mBaseMatrix.getValueAt(2,3),
				vec.x * ( float ) mBaseMatrix.getValueAt(3,0) + vec.y * ( float ) mBaseMatrix.getValueAt(3,1) + vec.z * ( float ) mBaseMatrix.getValueAt(3,2) + vec.w * ( float ) mBaseMatrix.getValueAt(3,3)
		);
	}

	public Vec3 multVec3(Vec3 vec){
		Vec4 out = new Vec4(
				vec.x * ( float ) mBaseMatrix.getValueAt(0,0) + vec.y * ( float ) mBaseMatrix.getValueAt(0,1) + vec.z * ( float ) mBaseMatrix.getValueAt(0,2) + ( float ) mBaseMatrix.getValueAt(0,3),
				vec.x * ( float ) mBaseMatrix.getValueAt(1,0) + vec.y * ( float ) mBaseMatrix.getValueAt(1,1) + vec.z * ( float ) mBaseMatrix.getValueAt(1,2) + ( float ) mBaseMatrix.getValueAt(1,3),
				vec.x * ( float ) mBaseMatrix.getValueAt(2,0) + vec.y * ( float ) mBaseMatrix.getValueAt(2,1) + vec.z * ( float ) mBaseMatrix.getValueAt(2,2) + ( float ) mBaseMatrix.getValueAt(2,3),
				vec.x * ( float ) mBaseMatrix.getValueAt(3,0) + vec.y * ( float ) mBaseMatrix.getValueAt(3,1) + vec.z * ( float ) mBaseMatrix.getValueAt(3,2) + ( float ) mBaseMatrix.getValueAt(3,3)
		);

		return new Vec3(out.x, out.y, out.z);
	}

	@Override
	public String toString(){
		return  "\n" +
				mBaseMatrix.getValueAt(0,0) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(0,1) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(0,2) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(0,3) + "\t\t\n" +
				mBaseMatrix.getValueAt(1,0) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(1,1) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(1,2) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(1,3) + "\t\t\n" +
				mBaseMatrix.getValueAt(2,0) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(2,1) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(2,2) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(2,3) + "\t\t\n" +
				mBaseMatrix.getValueAt(3,0) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(3,1) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(3,2) + "\t\t\t\t\t\t" + mBaseMatrix.getValueAt(3,3) + "\t\t\n" ;
	}

	private static class Matrix {

		private int nrows;
		private int ncols;
		private double[][] data;

		public Matrix(double[][] dat) {
			this.data = dat;
			this.nrows = dat.length;
			this.ncols = dat[0].length;
		}

		public Matrix(int nrow, int ncol) {
			this.nrows = nrow;
			this.ncols = ncol;
			data = new double[nrow][ncol];
		}

		public int getNrows() {
			return nrows;
		}

		public void setNrows(int nrows) {
			this.nrows = nrows;
		}

		public int getNcols() {
			return ncols;
		}

		public void setNcols(int ncols) {
			this.ncols = ncols;
		}

		public double[][] getValues() {
			return data;
		}

		public void setValues(double[][] values) {
			this.data = values;
		}

		public void setValueAt(int row, int col, double value) {
			data[row][col] = value;
		}

		public double getValueAt(int row, int col) {
			return data[row][col];
		}

		public boolean isSquare() {
			return nrows == ncols;
		}

		public int size() {
			if (isSquare())
				return nrows;
			return -1;
		}

		public static Matrix initializeUnitMatrix(int rows, int cols) {
			Matrix mat = new Matrix(rows, cols);
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					if(i == j){
						mat.setValueAt(i, j, 1);
					}
					else {
						mat.setValueAt(i, j, 0);
					}
				}
			}
			return mat;
		}

		public Matrix multiplyByConstant(double constant) {
			Matrix mat = new Matrix(nrows, ncols);
			for (int i = 0; i < nrows; i++) {
				for (int j = 0; j < ncols; j++) {
					mat.setValueAt(i, j, data[i][j] * constant);
				}
			}
			return mat;
		}

		public Matrix insertColumnWithValue1() {
			Matrix X_ = new Matrix(this.getNrows(), this.getNcols()+1);
			for (int i=0;i<X_.getNrows();i++) {
				for (int j=0;j<X_.getNcols();j++) {
					if (j==0)
						X_.setValueAt(i, j, 1.0);
					else
						X_.setValueAt(i, j, this.getValueAt(i, j-1));

				}
			}
			return X_;
		}
	}

	private static class MatrixTransformation {

		private MatrixTransformation() {}

		public static Matrix transpose(Matrix matrix) {
			Matrix transposedMatrix = new Matrix(matrix.getNcols(), matrix.getNrows());
			for (int i = 0; i < matrix.getNrows(); i++) {
				for (int j = 0; j < matrix.getNcols(); j++) {
					transposedMatrix.setValueAt(j, i, matrix.getValueAt(i, j));
				}
			}
			return transposedMatrix;
		}
		public static Matrix inverse(Matrix matrix) throws NoSquareException {
			return (transpose(cofactor(matrix)).multiplyByConstant(1.0 / determinant(matrix)));
		}

		public static double determinant(Matrix matrix) throws NoSquareException {
			if (!matrix.isSquare())
				throw new NoSquareException("matrix need to be square.");
			if (matrix.size() == 1) {
				return matrix.getValueAt(0, 0);
			}

			if (matrix.size() == 2) {
				return (matrix.getValueAt(0, 0) * matrix.getValueAt(1, 1)) - (matrix.getValueAt(0, 1) * matrix.getValueAt(1, 0));
			}
			double sum = 0.0;
			for (int i = 0; i < matrix.getNcols(); i++) {
				sum += changeSign(i) * matrix.getValueAt(0, i) * determinant(createSubMatrix(matrix, 0, i));
			}
			return sum;
		}

		private static int changeSign(int i) {
			if (i % 2 == 0)
				return 1;
			return -1;
		}

		public static Matrix createSubMatrix(Matrix matrix, int excluding_row, int excluding_col) {
			Matrix mat = new Matrix(matrix.getNrows() - 1, matrix.getNcols() - 1);
			int r = -1;
			for (int i = 0; i < matrix.getNrows(); i++) {
				if (i == excluding_row)
					continue;
				r++;
				int c = -1;
				for (int j = 0; j < matrix.getNcols(); j++) {
					if (j == excluding_col)
						continue;
					mat.setValueAt(r, ++c, matrix.getValueAt(i, j));
				}
			}
			return mat;
		}

		public static Matrix cofactor(Matrix matrix) throws NoSquareException {
			Matrix mat = new Matrix(matrix.getNrows(), matrix.getNcols());
			for (int i = 0; i < matrix.getNrows(); i++) {
				for (int j = 0; j < matrix.getNcols(); j++) {
					mat.setValueAt(i, j, changeSign(i) * changeSign(j) * determinant(createSubMatrix(matrix, i, j)));
				}
			}

			return mat;
		}

		public static Matrix add(Matrix matrix1, Matrix matrix2) throws IllegalDimensionException {
			if (matrix1.getNcols() != matrix2.getNcols() || matrix1.getNrows() != matrix2.getNrows())
				throw new IllegalDimensionException("Two matrices should be the same dimension.");
			Matrix sumMatrix = new Matrix(matrix1.getNrows(), matrix1.getNcols());
			for (int i = 0; i < matrix1.getNrows(); i++) {
				for (int j = 0; j < matrix1.getNcols(); j++)
					sumMatrix.setValueAt(i, j, matrix1.getValueAt(i, j) + matrix2.getValueAt(i, j));

			}
			return sumMatrix;
		}

		public static Matrix subtract(Matrix matrix1, Matrix matrix2) throws IllegalDimensionException {
			return add(matrix1, matrix2.multiplyByConstant(-1));
		}

		public static Matrix multiply(Matrix matrix1, Matrix matrix2) {
			Matrix multipliedMatrix = new Matrix(matrix1.getNrows(), matrix2.getNcols());

			for (int i = 0; i < multipliedMatrix.getNrows(); i++) {
				for (int j = 0; j < multipliedMatrix.getNcols(); j++) {
					double sum = 0.0;
					for (int k = 0; k < matrix1.getNcols(); k++) {
						sum += matrix1.getValueAt(i, k) * matrix2.getValueAt(k, j);
					}
					multipliedMatrix.setValueAt(i, j, sum);
				}
			}
			return multipliedMatrix;
		}
	}

	public static class IllegalDimensionException extends Exception {
		public IllegalDimensionException() {
			super();
			Log.error(this, "Dimension does not fit!");
		}

		public IllegalDimensionException(String message) {
			super(message);
			Log.error(this, "Dimension does not fit: " + message);
		}
	}

	private static class NoSquareException extends Exception {

		public NoSquareException() {
			super();
			Log.error(this, "The input is not square!");
		}

		public NoSquareException(String message) {
			super(message);
			Log.error(this, "The input is not square: " + message);
		}

	}
}
