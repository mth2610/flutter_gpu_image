package com.mth2610.flutter_gpu_image.glitch_filters;
import com.mth2610.flutter_gpu_image.base_filters.GPUImage3x3TextureSamplingFilter2;

//https://www.shadertoy.com/view/Xt2XRR
public class GPUImageColorBlendFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "const int numberLoop = 7;" +
            "const float constX = float(0.01/1.5);" +
            "const float t = float(29.5);" +

            "void main()" +
            "{" +
            "   vec2 p = textureCoordinate;"+
            "   vec3 col;"+
            "   col.rgb = vec3(texture2D(inputImageTexture, p).r, texture2D(inputImageTexture, p - 0.025 + cos(t + p.y + p.x)*0.1).g, texture2D(inputImageTexture, p + 0.04  + sin(t + p.y + p.x)*0.1).b);"+
            "   col.rgb *= 1.75;"+
            "   float a = t*0.25;"+
            "   col.gb -= texture2D(inputImageTexture, (p + length(p) ) * mat2(cos(a), -sin(a),"+
            "       sin(a), cos(a))).gb*0.15 + length(p - 0.5);"+
            "       col.rgb -= length(p - 0.5) * 0.75;"+
            "   gl_FragColor = vec4(col, 1.0);" +
            "}";

    private float[] convolutionKernel;

    public GPUImageColorBlendFilter() {
        super(FRAGMENT_SHADER);
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
    }
}
