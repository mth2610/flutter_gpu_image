package com.mth2610.flutter_gpu_image.art_filters;
import com.mth2610.flutter_gpu_image.base_filters.GPUImage3x3TextureSamplingFilter2;

public class GPUImageEdgeGlowFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +
            "const float iTime = float(15.0);" +

            "float lookup(vec2 p, float dx, float dy, float d)"+
            "{"+
            "   vec2 uv = (p.xy + vec2(dx * d, dy * d)) * (1.0/resolution.xy);"+
            "   vec4 c = texture2D(inputImageTexture, vec2(uv.x, 1.0 -uv.y));"+
            "   return 0.2126*c.r + 0.7152*c.g + 0.0722*c.b;"+
            "}"+

            "void main()" +
            "{" +
            "   float d = sin(iTime * 5.0)*0.5 + 1.5;"+
            "   vec2 p = gl_FragCoord.xy;"+
            "   float gx = 0.0;"+
            "   gx += -1.0 * lookup(p, -1.0, -1.0, d);"+
            "   gx += -2.0 * lookup(p, -1.0,  0.0, d);"+
            "   gx += -1.0 * lookup(p, -1.0,  1.0, d);"+
            "   gx +=  1.0 * lookup(p,  1.0, -1.0, d);"+
            "   gx +=  2.0 * lookup(p,  1.0,  0.0, d);"+
            "   gx +=  1.0 * lookup(p,  1.0,  1.0, d);"+
            "   float gy = 0.0;"+
            "   gy += -1.0 * lookup(p, -1.0, -1.0, d);"+
            "   gy += -2.0 * lookup(p,  0.0, -1.0, d);"+
            "   gy += -1.0 * lookup(p,  1.0, -1.0, d);"+
            "   gy +=  1.0 * lookup(p, -1.0,  1.0, d);"+
            "   gy +=  2.0 * lookup(p,  0.0,  1.0, d);"+
            "   gy +=  1.0 * lookup(p,  1.0,  1.0, d);"+
            "   float g = gx*gx + gy*gy;"+
            "   float g2 = g * (sin(iTime) / 2.0 + 0.5);"+
            "   vec4 col = texture2D(inputImageTexture, textureCoordinate);"+
            "   col += vec4(0.0, g, g2, 0.0);"+
            "   gl_FragColor = col;" +
            "}";

    private float[] convolutionKernel;

    public GPUImageEdgeGlowFilter() {
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
