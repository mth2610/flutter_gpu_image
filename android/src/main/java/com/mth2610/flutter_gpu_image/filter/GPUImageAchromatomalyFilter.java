package com.mth2610.flutter_gpu_image.filter;

public class GPUImageAchromatomalyFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +
            "void main()" +
            "{" +
            "   vec2 uv = textureCoordinate;"+
            //Achromatomaly ("Total color blindness")
            "   vec3 c_r = vec3(61.8, 32, 6.2);"+
            "   vec3 c_g = vec3(16.3, 77.5, 6.2);"+
            "   vec3 c_b = vec3(16.3, 32.0, 51.6);"+
            "   c_r /= 100.0;"+
            "   c_g /= 100.0;"+
            "   c_b /= 100.0;"+
            "   vec4 col = texture2D(inputImageTexture, uv);"+
            "   vec3 rgb = vec3( dot(col.rgb,c_r), dot(col.rgb,c_g), dot(col.rgb,c_b) );"+
            "   gl_FragColor = vec4(rgb, col.a);" +
            "}";

    private float[] convolutionKernel;

    public GPUImageAchromatomalyFilter() {
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
