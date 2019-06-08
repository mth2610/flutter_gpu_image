package com.mth2610.flutter_gpu_image.blur_filters;

import android.opengl.GLES20;

import com.mth2610.flutter_gpu_image.base_filters.GPUImage3x3TextureSamplingFilter2;

//https://www.shadertoy.com/view/ldBSDd
public class GPUImageRadicalBlurFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String PRIORITYMAP_FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +
            "const float iTime = float(0.0);" +

            "vec3 deform( in vec2 p )"+
            "{"+
            "   vec2 q = sin( vec2(1.1,1.2)*iTime + p );"+
            "   float a = atan( q.y, q.x );"+
            "   float r = sqrt( dot(q,q) );"+
            "   vec2 uv = p*sqrt(1.0+r*r);"+
            "   uv += sin( vec2(0.0,0.6) + vec2(1.0,1.1)*iTime);"+
            "   return texture2D(inputImageTexture, uv*0.3).yxx;"+
            "}"+

            "void main()" +
            "{" +
            "   vec2 p = -1.0 + 2.0*textureCoordinate;"+
            //"   vec2 p = textureCoordinate;"+
            "   vec3  col = vec3(0.0);"+
            "   vec2  d = (vec2(0.0,0.0)-p)/8.0;"+
            "   float w = 1.0;"+
            "   vec2  s = p;"+
            "   for( int i=0; i<8; i++ )"+
            "   {"+
            "       vec3 res = deform( s );"+
            "       col += w*smoothstep( 0.0, 1.0, res );"+
            "       w *= 0.99;"+
            "       s += d;"+
            "   }"+
            "   col = col * 3.5 / 8.0;;"+
            "   gl_FragColor = vec4( col, 1.0 );"+
            "}";

    private float[] convolutionKernel;
    private int uniformConvolutionMatrix;

    public GPUImageRadicalBlurFilter() {
//        this(new float[]{
//                0.5f, 1.0f, 0.5f,
//                1.0f, -6.0f, 1.0f,
//                0.5f, 1.0f, 0.5f
//        });

        this(new float[]{
                0.0f, 1.0f, 0.0f,
                1.0f, -4.0f, 1.0f,
                0.0f, 1.0f, 0.0f
        });
    }

    private GPUImageRadicalBlurFilter(final float[] convolutionKernel) {
        super(PRIORITYMAP_FRAGMENT_SHADER);
        this.convolutionKernel = convolutionKernel;
    }

    @Override
    public void onInit() {
        super.onInit();
        uniformConvolutionMatrix = GLES20.glGetUniformLocation(getProgram(), "convolutionMatrix");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setConvolutionKernel(convolutionKernel);
    }

    private void setConvolutionKernel(final float[] convolutionKernel) {
        this.convolutionKernel = convolutionKernel;
        setUniformMatrix3f(uniformConvolutionMatrix, this.convolutionKernel);
    }
}
