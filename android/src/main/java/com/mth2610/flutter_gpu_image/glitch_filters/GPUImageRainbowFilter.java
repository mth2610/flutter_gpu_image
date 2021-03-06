package com.mth2610.flutter_gpu_image.glitch_filters;
import com.mth2610.flutter_gpu_image.base_filters.GPUImage3x3TextureSamplingFilter2;

//https://www.shadertoy.com/view/Ml2Szz

public class GPUImageRainbowFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "const int numberLoop = 7;" +
            "const float constX = float(0.01/1.5);" +
            "const float constY = float(0.05/5.0);" +

            "void main()" +
            "{" +
            "   vec2 p = textureCoordinate;"+
            "   float t = 0.0;"+
            "   float r = 0.0;"+
            "   float pass = 0.0;"+
            "   for (int i = 0; i < numberLoop; i++) {"+
            "       r += smoothstep(0.3, 0.4, texture2D(inputImageTexture, vec2(p.x - pass*constX, p.y)).r);"+
            "       pass += 1.0;"+
            "   }"+
            "   r /= pass;"+
            "   float g = 0.0;"+
            "   pass = 0.0;"+

            "   for (int i = 0; i < numberLoop; i++) {"+
            "       g += smoothstep(0.3, 0.4, texture2D(inputImageTexture, vec2(p.x - pass*constX, p.y + pass*constX)).g);"+
            "       pass += 1.0;"+
            "   }"+

            "   g /= pass;"+
            "   float b = 0.0;"+
            "   pass = 0.0;"+
            "   for (int i = 0; i < numberLoop; i++) {"+
            "       b += smoothstep(0.3, 0.4, texture2D(inputImageTexture, vec2(p.x, p.y + pass*constX)).b);"+
            "       pass += 1.0;"+
            "   }"+
            "   b /= pass;"+
            "   gl_FragColor = vec4(vec3(r, g, b), 1.0);" +
            "}";

    private float[] convolutionKernel;

    public GPUImageRainbowFilter() {
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
