package com.mth2610.flutter_gpu_image.art_filters;

//https://www.shadertoy.com/view/llGGz3
import android.opengl.GLES20;

import com.mth2610.flutter_gpu_image.base_filters.GPUImage3x3TextureSamplingFilter2;

public class GPUImageLowPolyFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String PRIORITYMAP_FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +

            "vec2 GetCoord( float offset, vec2 iResolution)"+
            "{"+
            "   float y = floor(offset / iResolution.x);"+
            "   return vec2( offset - y * iResolution.x, y ) + 0.5;"+
            "}"+

            "float sdTriangle( in vec2 p0, in vec2 p1, in vec2 p2, in vec2 p )"+
            "{"+
            "   vec2 e0 = p1 - p0;"+
            "   vec2 e1 = p2 - p1;"+
            "   vec2 e2 = p0 - p2;"+
            "   vec2 v0 = p - p0;"+
            "   vec2 v1 = p - p1;"+
            "   vec2 v2 = p - p2;"+
            "   vec2 pq0 = v0 - e0*clamp( dot(v0,e0)/dot(e0,e0), 0.0, 1.0 );"+
            "   vec2 pq1 = v1 - e1*clamp( dot(v1,e1)/dot(e1,e1), 0.0, 1.0 );"+
            "   vec2 pq2 = v2 - e2*clamp( dot(v2,e2)/dot(e2,e2), 0.0, 1.0 );"+

            // Correction for flipped triangle, we don't need it if consistent order is kept
            //float s = -sign(e0.x*e1.y - e0.y*e1.x); e0 *= s; e1 *= s; e2 *= s;
            "   vec2 d = min( min( vec2( dot( pq0, pq0 ), v0.x*e0.y-v0.y*e0.x ),"+
            "   vec2( dot( pq1, pq1 ), v1.x*e1.y-v1.y*e1.x )),"+
            "   vec2( dot( pq2, pq2 ), v2.x*e2.y-v2.y*e2.x ));"+
            "   return -sqrt(d.x)*sign(d.y);"+
            "}"+



            "void main()" +
            "{" +
            "   vec2 uv = textureCoordinate;"+
            "   vec2 iResolution = gl_FragCoord.xy/uv.xy;"+
            "   vec4 tri = texture2D(inputImageTexture, uv);"+
            "   vec2 p0 = GetCoord(tri.x, iResolution);"+
            "   vec2 p1 = GetCoord(tri.y, iResolution);"+
            "   vec2 p2 = GetCoord(tri.z, iResolution);"+
            "   vec2 p3 = (p0 + p1 + p2) * (1.0/3.0);"+
            "   float dist = sdTriangle(p0, p1, p2, gl_FragCoord);"+
            "   p3 = GetCoord(tri.w);"+
            "   dist = min(dist, sdTriangle(p0, p2, p3, gl_FragCoord));"+
            "   vec3 c0 = texture2D(inputImageTexture, p0/iResolution.xy).xyz;"+
            "   vec3 c1 = texture2D(inputImageTexture, p1/iResolution.xy).xyz;"+
            "   vec3 c2 = texture2D(inputImageTexture, p2/iResolution.xy).xyz;"+
            "   vec3 c3 = texture2D(inputImageTexture, p3/iResolution.xy).xyz;"+
            "   vec3 video = pow( (pow(c0, vec3(2.2)) + pow(c1, vec3(2.2)) + pow(c2, vec3(2.2)) + pow(c3, vec3(2.2))) / 4.0,"+
            "       vec3(1.0 / 2.2));"+
            "   vec3 color = video.xyz * dist;"+
            "   gl_FragColor = vec4(color, 1.0);"+
            "}";

    private float[] convolutionKernel;
    private int uniformConvolutionMatrix;

    public GPUImageLowPolyFilter() {
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

    private GPUImageLowPolyFilter(final float[] convolutionKernel) {
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
