package com.mth2610.flutter_gpu_image.blind_filters;
import com.mth2610.flutter_gpu_image.base_filters.GPUImage3x3TextureSamplingFilter2;
import android.opengl.GLES20;

public class GPUImageProtanomalyFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String PRIORITYMAP_FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +
            "void main()" +
            "{" +
            "   vec2 uv = textureCoordinate;"+
            //Protanomaly ("red/green", 1% of males, 0.01% of females)
            "   vec3 c_r = vec3(81.667, 18.333, 0);"+
            "   vec3 c_g = vec3(33.333, 66.667, 0);"+
            "   vec3 c_b = vec3(0, 12.5, 87.5);"+
            "   c_r /= 100.0;"+
            "   c_g /= 100.0;"+
            "   c_b /= 100.0;"+
            "   vec4 col = texture2D(inputImageTexture, uv);"+
            "   vec3 rgb = vec3( dot(col.rgb,c_r), dot(col.rgb,c_g), dot(col.rgb,c_b) );"+
            "   gl_FragColor = vec4(rgb, col.a);" +
            "}";

    private float[] convolutionKernel;
    private int uniformConvolutionMatrix;

    public GPUImageProtanomalyFilter() {
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

    private GPUImageProtanomalyFilter(final float[] convolutionKernel) {
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
