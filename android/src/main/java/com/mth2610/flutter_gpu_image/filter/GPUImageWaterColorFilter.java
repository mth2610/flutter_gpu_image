package com.mth2610.flutter_gpu_image.filter;

//https://www.shadertoy.com/view/4lV3RG
import com.mth2610.flutter_gpu_image.base_filters.GPUImage3x3TextureSamplingFilter2;

public class GPUImageWaterColorFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +
            "void main()" +
            "{" +
            "   vec2 blend_uv = textureCoordinate;"+
            "   vec2 uv = vec2(1.0 - blend_uv.x, blend_uv.y);"+
            "   vec3 intensity = 1.0 - texture2D(inputImageTexture, uv).rgb;"+
            "   float vidSample = dot(vec3(1.0), texture2D(inputImageTexture, uv).rgb);"+
            "   float delta = 0.01;"+
            "   float vidSampleDx = dot(vec3(1.0),"+
            "       texture2D(inputImageTexture, uv + vec2(delta, 0.0)).rgb);"+
            "   float vidSampleDy = dot(vec3(1.0),"+
            "       texture2D(inputImageTexture, uv + vec2(0.0, delta)).rgb);"+
            "   vec2 flow = delta * vec2 (vidSampleDy - vidSample, vidSample - vidSampleDx);"+
            "   intensity = 0.005 * intensity + 0.995 *"+
            "       (1.0 - texture2D(inputImageTexture,"+
            "       blend_uv + vec2(-1.0, 1.0) * flow).rgb);"+
            "   gl_FragColor = vec4(1.0 - intensity,1.0);"+
            "}";

    private float[] convolutionKernel;

    public GPUImageWaterColorFilter() {
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
