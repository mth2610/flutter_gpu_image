package com.mth2610.flutter_gpu_image.natural_filters;

import android.opengl.GLES20;

import com.mth2610.flutter_gpu_image.base_filters.GPUImage3x3TextureSamplingFilter2;

//https://www.shadertoy.com/view/ldBSDd
public class GPUImageSmoke1Filter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String PRIORITYMAP_FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +
            "const float OCTAVES = float( 8.0 );" +

            "float rand(vec2 co){"+
            "   return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);"+
            "}"+

            "float rand2(vec2 co){"+
            "   return fract(cos(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);"+
            "}"+

            // Rough Value noise implementation
            "float valueNoiseSimple(vec2 vl) {"+
            "   float minStep = 1.0 ;"+
            "   vec2 grid = floor(vl);"+
            "   vec2 gridPnt1 = grid;"+
            "   vec2 gridPnt2 = vec2(grid.x, grid.y + minStep);"+
            "   vec2 gridPnt3 = vec2(grid.x + minStep, grid.y);"+
            "   vec2 gridPnt4 = vec2(gridPnt3.x, gridPnt2.y);"+
            "   float s = rand2(grid);"+
            "   float t = rand2(gridPnt3);"+
            "   float u = rand2(gridPnt2);"+
            "   float v = rand2(gridPnt4);"+
            "   float x1 = smoothstep(0., 1., fract(vl.x));"+
            "   float interpX1 = mix(s, t, x1);"+
            "   float interpX2 = mix(u, v, x1);"+
            "   float y = smoothstep(0., 1., fract(vl.y));"+
            "   float interpY = mix(interpX1, interpX2, y);"+
            "   return interpY;"+
            "}"+

            "float fractalNoise(vec2 vl) {"+
            "   float persistance = 2.0;"+
            "   float amplitude = 0.5;"+
            "   float rez = 0.0;"+
            "   vec2 p = vl;"+
            "   for (float i = 0.0; i < OCTAVES; i++) {"+
            "       rez += amplitude * valueNoiseSimple(p);"+
            "       amplitude /= persistance;"+
            "       p *= persistance;"+
            "   }"+
            "   return rez;"+
            "}"+

            "float complexFBM(vec2 p) {"+
            "   float iTime=15.0;"+
            //"   float sound = getLowFreqs();"+
            "   float slow = iTime / 2.5;"+
            "   float fast = iTime / .5;"+
            "   vec2 offset1 = vec2(slow  , 0.0);"+
            "   vec2 offset2 = vec2(sin(fast )* 0.1, 0.0);"+
            "   return fractalNoise( p + offset1 + fractalNoise("+
            "       p + fractalNoise("+
            "       p + 2. * fractalNoise(p - offset2)"+
            "           )"+
            "       )"+
            "   );"+
            " }"+

            "void main()" +
            "{" +
            "   vec2 uv = textureCoordinate;"+
            "   vec4 blueColor = vec3(0.529411765, 0.807843137, 0.980392157);"+
            "   vec4 orangeColor2 = vec3(1.0, 1.0, 1.0, 0.0);"+
           //"   vec3 orangeColor2 = vec3(0.509803922, 0.203921569, 0.015686275);"+
            "   vec4 rez = mix(orangeColor2, blueColor, complexFBM(uv));"+
            "   vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);"+
            //"   gl_FragColor = textureColor + vec4(rez, rez.r*0.2);"+
            "   gl_FragColor = mix(textureColor, rez), 1.0);"+
            "}";

    private float[] convolutionKernel;
    private int uniformConvolutionMatrix;

    public GPUImageSmoke1Filter() {
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

    private GPUImageSmoke1Filter(final float[] convolutionKernel) {
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
