import java.util.*;

public class CubicSpline{
  private float[] x;
  private float[] f;
  private int N;
  private float[] h;
  private float[] a;
  private float[] b;
  private float[] c;
  private float[] d;
  private float[] S;
  
  public CubicSpline(float[] ex, float[] fx){
    this.x = ex;
    this.f = fx;
    this.N = x.length-1;
    this.h = new float[N+1];
    this.a = new float[N+1];
    this.b = new float[N+1];
    this.c = new float[N+1];
    this.d = new float[N+1];
    this.S = new float[N+1];
    a[0] = f[0];
    for (int i=0;i<(N+1);i++){
      a[i] = f[i];
    }
    for (int i=0;i<N;i++){
      h[i] = x[i+1]-x[i];
    }
    
    float[] alpha = new float[N];
    
    for (int i=1;i<N;i++){
      alpha[i] = (3.0f/(h[i]))*(a[i+1] - a[i]) - (3.0f/(h[i-1]))*(a[i] - a[i-1]);
    }
    
    float[] l = new float[N+1];
    l[0] = 1.0f;
    
    float[] u = new float[N+1];
    u[0] = 0.0f;
    
    float[] z = new float[N+1];
    z[0] = 0.0f;
    
    for (int i=1;i<N;i++){
      l[i] = 2.0f*(x[i+1] - x[i-1]) - h[i-1]*u[i-1];
      u[i] = h[i]/l[i];
      z[i] = (alpha[i] - h[i-1]*z[i-1])/l[i];
    }
    
    l[N] = 1.0f;
    z[N] = 0.0f;
    c[N] = 0.0f;
    
    for (int j=N-1;j>=0;j--){
      c[j] = z[j] - u[j]*c[j+1];
      b[j] = (a[j+1] - a[j])/h[j] - h[j]*(c[j+1] + 2*c[j])/3.0f;
      d[j] = (c[j+1] - c[j])/(3.0f*h[j]);
    }
    
    float X = 0.0f;
    for (int i=0;i<(N);i++){
      if (i<N){
        X = ( (x[i] + x[i+1]) / 2.0f ) - x[i];
      }
      S[i] = a[i] + b[i]*X + c[i]*X*X + d[i]*X*X*X;
      //System.out.println(((x[i] + x[i+1]) / 2.0f)+ " "+ S[i]);
    }
    S[N] = a[N];
    
    /*System.out.println("Ex: \n");
    for (int i=0;i<N;i++){
      if (i<N){
        System.out.print(((x[i] + x[i+1]) / 2.0f ) + " ");
      }
      else{
        System.out.print(x[i] + " ");
      }
    }
    System.out.println("S(x): \n");
    for (int i=0;i<N;i++){
      System.out.print(S[i] + " ");
    }*/
  }
  
  public float[] getA(){
    return a;
  }
  public float[] getB(){
    return b;
  }
  public float[] getC(){
    return c;
  }
  public float[] getD(){
    return d;
  }
  
  public float[] getCCValues(){
    return S;
  }
  
  public float getY(float ex){
    for (int i=0;i<(N+1);i++){
      if (ex >= x[i] && ex <= x[i+1]){
        return a[i] + b[i]*ex + c[i]*ex*ex + d[i]*ex*ex*ex;
      }
    }
    return 0.0f;
  }

  /*public float getX(float y){
    float D0,D1,D,C;
    float root1,root2,root3;
    float[] roots = new float[3];
    float root=0.0f;
    float u1 = 1.0f;
    Complex u2 = new Complex(-1.0/2.0,0.5*Math.pow(3,0.5));
    Complex u3 = new Complex(-1.0/2.0,-0.5*Math.pow(3,0.5));
    for (int i=0; i<(N+1);i++){
      D = 18.0f*a[i]*b[i]*c[i]*d[i]-4.0f*(b[i]*b[i]*b[i])*d[i]-4.0f*a[i]*(c[i]*c[i]*c[i])-27.0f*(a[i]*a[i])*(d[i]*d[i]);
      D0 = b[i]*b[i]-3*a[i]*c[i];
      D1 = 2.0f*b[i]*b[i]*b[i] -9.0f*a[i]*b[i]*c[i] + 27.0f*a[i]*a[i]*d[i];
      if (Math.pow(-27.0f*a[i]*a[i]*D,0.50f) >= 0.0){
        C = (float)Math.pow((D1+(Math.pow(-27.0f*a[i]*a[i]*D,0.50f)))/2.0f,1.0f/3.0f);
      }
      for (int k=0;k<3;k++){
        if (k==0){
          root1 = -(1.0f/3.0f)*(b[i]+u1*C+D0/(u1*C));
        }
        if (k==1){
          root
    }
    return root;
  }*/
  public String toString(){
	String graph = "";
	for (int i=0; i<(N+1);i++){
		graph+=("t= "+x[i]+",  r= "+getY(x[i])+"\n");
	}
	graph += "\n";
	return graph;
  }    
  
  public float getLeft(){
    return x[0];
  }
  public float getRight(){
    return x[N];
  }
  public static float e(float x){
    return (((float)(Math.exp(x)) - 1.0f)/((float)(Math.E)-1.0f));
  }
  
  /*public static void main(String[] args){
    int N = 2*64+1;
    float n = N+0.0f;
    float dx = 1.0f/(n-1);
    float[] x = new float[N];
    float[] f = new float[N];
    for (int i=0;i<(N);i++){
      x[i] = dx*i+0.0f;
      f[i] = e(dx*i);
    }
    CubicSpline spline = new CubicSpline(x,f);
  }*/
  
}
    
