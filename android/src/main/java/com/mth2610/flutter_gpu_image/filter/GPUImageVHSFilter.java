package com.mth2610.flutter_gpu_image.filter;

public class GPUImageVHSFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +
            "void main()" +
            "{" +
            "   vec2 uv = textureCoordinate;"+
            "   vec4 vhsColor;"+
            "   float iTime = 1.0;"+
            "   vec2 uvOffset = texture2D(inputImageTexture, vec2(iTime*5.0)).rg;"+
            "   uvOffset.x *= 0.02;"+
            "   uvOffset.y *= 0.052;"+
            "   vhsColor.r = texture2D(inputImageTexture, uv + uvOffset + vec2(-0.02*texture2D(inputImageTexture, vec2(uv.x,uv.y/200.0 + iTime*5.0)).r, (tan(sin(iTime)) * 0.6 ) * 0.05) ).r;"+
            "   vhsColor.g = vec4(texture2D(inputImageTexture, uv + uvOffset)).g;"+
            "   vhsColor.b = texture2D(inputImageTexture, uv / uvOffset + vec2(-0.01*texture2D(inputImageTexture, vec2(uv.x/2.0,uv.y + iTime*5.0)).r, -0.2) ).b;"+
            "   vhsColor.rgb = mix(vhsColor.rgb, vec3(dot(vhsColor.rgb, vec3(.43))), 0.5);"+
            "   vhsColor.a = 1.0;"+
            "   gl_FragColor = vhsColor;" +
            "}";

    private float[] convolutionKernel;

    public GPUImageVHSFilter() {
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
