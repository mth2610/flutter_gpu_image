/*
 * Copyright (C) 2018 CyberAgent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// http://opensource.graphics/how-to-code-a-nice-user-guided-foreground-extraction-algorithm/
// https://dahtah.github.io/imager/foreground_background.html

package com.mth2610.flutter_gpu_image.filter;

import android.opengl.GLES20;

public class GPUImagePriorityMapFilter2 extends GPUImage3x3TextureSamplingFilter2 {

    public static final String PRIORITYMAP_FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +

            "float priorityValue(vec2 p, vec2 r){"+
            "   vec2 p1 = vec2(p.x - r.x, p.y - r.y);"+
            "   vec2 p2 = vec2(p.x, p.y - r.y);"+
            "   vec2 p3 = vec2(p.x + r.x, p.y - r.y);"+
            "   vec2 p4 = vec2(p.x - r.x, p.y);"+
            "   vec2 p5 = p;"+
            "   vec2 p6 = vec2(p.x + r.x, p.y);"+
            "   vec2 p7 = vec2(p.x - r.x, p.y + r.y);"+
            "   vec2 p8 = vec2(p.x, p.y + r.y);"+
            "   vec2 p9 = vec2(p.x + r.x, p.y + r.y);"+
            "   vec3 value1 = texture2D(inputImageTexture, p1).rgb;"+
            "   vec3 value2 = texture2D(inputImageTexture, p2).rgb;"+
            "   vec3 value3 = texture2D(inputImageTexture, p3).rgb;"+
            "   vec3 value4 = texture2D(inputImageTexture, p4).rgb;"+
            "   vec3 value5 = texture2D(inputImageTexture, p5).rgb;"+
            "   vec3 value6 = texture2D(inputImageTexture, p6).rgb;"+
            "   vec3 value7 = texture2D(inputImageTexture, p7).rgb;"+
            "   vec3 value8 = texture2D(inputImageTexture, p8).rgb;"+
            "   vec3 value9 = texture2D(inputImageTexture, p9).rgb;"+
            "   vec3 dx = -value1 - 2.0*value4 - value7 + value3 + 2.0*value6 + value9;"+
            "   vec3 dy = -value1 - 2.0*value2 - value3 + value7 + 2.0*value8 + value9;"+
            "   vec3 gradient = dx + dy;"+
            "   float result = 1.0/(1.0 + gradient.x*gradient.x + gradient.y*gradient.y + gradient.z*gradient.z);"+
            "   return result;"+
            "}"+

            "void main()" +
            "{" +
            "float resultColor = priorityValue(textureCoordinate, resolution);" +
                "gl_FragColor = vec4(vec3(resultColor), 1.0);\n" +
            "}";

    private float[] convolutionKernel;
    private int uniformConvolutionMatrix;

    public GPUImagePriorityMapFilter2() {
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

    private GPUImagePriorityMapFilter2(final float[] convolutionKernel) {
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
