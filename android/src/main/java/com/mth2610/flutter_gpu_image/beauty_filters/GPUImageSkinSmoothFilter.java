package com.mth2610.flutter_gpu_image.beauty_filters;

import android.opengl.GLES20;

import com.mth2610.flutter_gpu_image.base_filters.GPUImage3x3TextureSamplingFilter2;

//https://www.shadertoy.com/view/4dl3R4
public class GPUImageSkinSmoothFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String PRIORITYMAP_FRAGMENT_SHADER = "" +
            "precision mediump float;" +
            "varying mediump vec2 textureCoordinate;" +
            "uniform sampler2D inputImageTexture;" +
            //"uniform vec2 singleStepOffset;" +
            "uniform mediump float params;" +
            "const highp vec3 W = vec3(0.299,0.587,0.114);" +
            "vec2 blurCoordinates[20];" +
            "varying vec2 resolution;" +

            "float hardLight(float color)" +
            "{" +
            "   if(color <= 0.5)" +
            "       color = color * color * 2.0;" +
            "   else" +
            "       color = 1.0 - ((1.0 - color)*(1.0 - color) * 2.0);"+
            "   return color;"+
            "}"+

            "void main(){"+
            "   vec2 singleStepOffset = 2.0*resolution;" +
            "   vec3 centralColor = texture2D(inputImageTexture, textureCoordinate).rgb;"+
            "   blurCoordinates[0] = textureCoordinate.xy + singleStepOffset * vec2(0.0, -10.0);"+
            "   blurCoordinates[1] = textureCoordinate.xy + singleStepOffset * vec2(0.0, 10.0);"+
            "   blurCoordinates[2] = textureCoordinate.xy + singleStepOffset * vec2(-10.0, 0.0);"+
            "   blurCoordinates[3] = textureCoordinate.xy + singleStepOffset * vec2(10.0, 0.0);"+
            "   blurCoordinates[4] = textureCoordinate.xy + singleStepOffset * vec2(5.0, -8.0);"+
            "   blurCoordinates[5] = textureCoordinate.xy + singleStepOffset * vec2(5.0, 8.0);"+
            "   blurCoordinates[6] = textureCoordinate.xy + singleStepOffset * vec2(-5.0, 8.0);"+
            "   blurCoordinates[7] = textureCoordinate.xy + singleStepOffset * vec2(-5.0, -8.0);"+
            "   blurCoordinates[8] = textureCoordinate.xy + singleStepOffset * vec2(8.0, -5.0);"+
            "   blurCoordinates[9] = textureCoordinate.xy + singleStepOffset * vec2(8.0, 5.0);"+
            "   blurCoordinates[10] = textureCoordinate.xy + singleStepOffset * vec2(-8.0, 5.0);"+
            "   blurCoordinates[11] = textureCoordinate.xy + singleStepOffset * vec2(-8.0, -5.0);"+
            "   blurCoordinates[12] = textureCoordinate.xy + singleStepOffset * vec2(0.0, -6.0);"+
            "   blurCoordinates[13] = textureCoordinate.xy + singleStepOffset * vec2(0.0, 6.0);"+
            "   blurCoordinates[14] = textureCoordinate.xy + singleStepOffset * vec2(6.0, 0.0);"+
            "   blurCoordinates[15] = textureCoordinate.xy + singleStepOffset * vec2(-6.0, 0.0);"+
            "   blurCoordinates[16] = textureCoordinate.xy + singleStepOffset * vec2(-4.0, -4.0);"+
            "   blurCoordinates[17] = textureCoordinate.xy + singleStepOffset * vec2(-4.0, 4.0);"+
            "   blurCoordinates[18] = textureCoordinate.xy + singleStepOffset * vec2(4.0, -4.0);"+
            "   blurCoordinates[19] = textureCoordinate.xy + singleStepOffset * vec2(4.0, 4.0);"+
            "   float sampleColor = centralColor.g * 20.0;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[0]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[1]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[2]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[3]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[4]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[5]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[6]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[7]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[8]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[9]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[10]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[11]).g;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[12]).g * 2.0;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[13]).g * 2.0;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[14]).g * 2.0;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[15]).g * 2.0;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[16]).g * 2.0;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[17]).g * 2.0;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[18]).g * 2.0;"+
            "   sampleColor += texture2D(inputImageTexture, blurCoordinates[19]).g * 2.0;"+
            "   sampleColor = sampleColor / 48.0;"+
            "   float highPass = centralColor.g - sampleColor + 0.5;"+
            "   for(int i = 0; i < 5;i++)"+
            "   {"+
            "       highPass = hardLight(highPass);"+
            "   }"+

            "   float luminance = dot(centralColor, W);"+
            "   float alpha = pow(luminance, params);"+
            "   vec3 smoothColor = centralColor + (centralColor-vec3(highPass))*alpha*0.1;"+
            "   gl_FragColor = vec4(mix(smoothColor.rgb, max(smoothColor, centralColor), alpha), 1.0);"+
            "}";

    private float[] convolutionKernel;
    private int uniformConvolutionMatrix;
    private int mSingleStepOffsetLocation;
    private int mParamsLocation;
    public GPUImageSkinSmoothFilter() {
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

    private GPUImageSkinSmoothFilter(final float[] convolutionKernel) {
        super(PRIORITYMAP_FRAGMENT_SHADER);
        this.convolutionKernel = convolutionKernel;
    }

    @Override
    public void onInit() {
        super.onInit();
        uniformConvolutionMatrix = GLES20.glGetUniformLocation(getProgram(), "convolutionMatrix");
        //mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
        mParamsLocation = GLES20.glGetUniformLocation(getProgram(), "params");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setConvolutionKernel(convolutionKernel);
        setFloat(mParamsLocation, 1.0f);
    }

    private void setConvolutionKernel(final float[] convolutionKernel) {
        this.convolutionKernel = convolutionKernel;
        setUniformMatrix3f(uniformConvolutionMatrix, this.convolutionKernel);
    }
}
