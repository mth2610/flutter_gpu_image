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

            "void main()" +
            "{" +
            //"   vec2 iResolution = 1.0/resolution.xy;"+
            "   const vec3 c1 = vec3(0.5, 0.0, 0.1);"+
            "   const vec3 c2 = vec3(0.9, 0.1, 0.0);"+
            "   const vec3 c3 = vec3(0.2, 0.1, 0.7);"+
            "   const vec3 c4 = vec3(1.0, 0.9, 0.1);"+
            "   const vec3 c5 = vec3(0.1);"+
            "   const vec3 c6 = vec3(0.9);"+
            "   vec2 speed = vec2(1.3, 0.1);"+
            "   float shift = 1.77+sin(iTime*2.0)/10.0;"+
            "   float alpha = 1.0;"+
            "   float dist = 6.0+sin(iTime*0.4)/.6;"+

            "   vec2 p = textureCoordinate;"+

            "   p.x -= iTime/1.1;"+
            "   float q = fbm(p - iTime * 0.01+1.0*sin(iTime)/10.0);"+
            "   float qb = fbm(p - iTime * 0.002+0.1*cos(iTime)/5.0);"+
            "   float q2 = fbm(p - iTime * 0.44 - 5.0*cos(iTime)/7.0) - 6.0;"+
            "   float q3 = fbm(p - iTime * 0.9 - 10.0*cos(iTime)/30.0)-4.0;"+
            "   float q4 = fbm(p - iTime * 2.0 - 20.0*sin(iTime)/20.0)+2.0;"+
            "   q = (q + qb - q2 -q3  + q4)/3.8;"+
            "   vec2 r = vec2(fbm(p + q /2.0 + iTime * speed.x - p.x - p.y), fbm(p + q - iTime * speed.y));"+
            "   vec3 c = mix(c1, c2, fbm(p + r)) + mix(c3, c4, r.x) - mix(c5, c6, r.y);"+
            "   vec3 color = vec3(c * cos(shift * textureCoordinate.x));"+
            "   color -= .25;"+
            "   color.r *= 1.02;"+
            "   vec3 hsv = rgb2hsv(color);"+
            "   hsv.y *= hsv.z  * 0.8;"+
            "   hsv.z *= hsv.y * 1.3;"+
            "   color = hsv2rgb(hsv);"+
            "   vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);"+
            "   gl_FragColor = textureColor + vec4(color.x, color.y, color.z, alpha);"+
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
