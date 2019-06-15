package com.mth2610.flutter_gpu_image.natural_filters;

import android.opengl.GLES20;

import com.mth2610.flutter_gpu_image.base_filters.GPUImage3x3TextureSamplingFilter2;

//https://www.shadertoy.com/view/ldBSDd
public class GPUImageFireFliesFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String PRIORITYMAP_FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +
            "const float iTime = float(15.0);" +
            "vec2 rand(vec2 r){ return fract(4e4*sin(2e3*r));}"+
            "float rand(float r){ return fract(4e4*sin(2e3*r));}"+

            "void main()" +
            "{" +
            "   vec2 iResolution = 1.0/resolution.xy;"+
            "   vec4 fireFlies = vec4(0.0, 0.0, 0.0, 0.0);"+
            "   for (float i = 0.0; i < 60.0; i ++){"+
            "       fireFlies += rand(i)/3e3"+
            "          / length( fract( rand(i+vec2(0.1, 0.2)) + (rand(i+vec2(0.3, 0.5))-0.5) * iTime/20.0 )"+
            "           - textureCoordinate);"+
            "   }"+
            "   fireFlies *= vec4(1.0,0.6,0.4, 1.0);"+
            "   fireFlies.xyz = mix(fireFlies.xyz, vec3(1.0), abs(fireFlies.x));"+
            "   vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);"+
            "   gl_FragColor = textureColor + fireFlies;"+
            "}";

    private float[] convolutionKernel;
    private int uniformConvolutionMatrix;

    public GPUImageFireFliesFilter() {
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

    private GPUImageFireFliesFilter(final float[] convolutionKernel) {
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
