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

package com.mth2610.flutter_gpu_image.base_filters;


import android.opengl.GLES20;

public class GPUImage3x3TextureSamplingFilter2 extends GPUImageFilter {
    public static final String THREE_X_THREE_TEXTURE_SAMPLING_VERTEX_SHADER = "" +
            "attribute vec4 position;" +
            "attribute vec4 inputTextureCoordinate;" +
            "uniform highp float texelWidth;" +
            "uniform highp float texelHeight;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +
            "void main()" +
            "{" +
            "    gl_Position = position;" +
            "    resolution = vec2(texelWidth, texelHeight);" +
            "    textureCoordinate = inputTextureCoordinate.xy;"+
            "}";

    private int uniformTexelWidthLocation;
    private int uniformTexelHeightLocation;

    private boolean hasOverriddenImageSizeFactor = false;
    private float texelWidth;
    private float texelHeight;
    private float lineSize = 1.0f;

    public GPUImage3x3TextureSamplingFilter2() {
        this(NO_FILTER_VERTEX_SHADER);
    }

    public GPUImage3x3TextureSamplingFilter2(final String fragmentShader) {
        super(THREE_X_THREE_TEXTURE_SAMPLING_VERTEX_SHADER, fragmentShader);
    }

    @Override
    public void onInit() {
        super.onInit();
        uniformTexelWidthLocation = GLES20.glGetUniformLocation(getProgram(), "texelWidth");
        uniformTexelHeightLocation = GLES20.glGetUniformLocation(getProgram(), "texelHeight");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        if (texelWidth != 0) {
            updateTexelValues();
        }
    }

    @Override
    public void onOutputSizeChanged(final int width, final int height) {
        super.onOutputSizeChanged(width, height);
        if (!hasOverriddenImageSizeFactor) {
            setLineSize(lineSize);
        }
    }

    public void setTexelWidth(final float texelWidth) {
        hasOverriddenImageSizeFactor = true;
        this.texelWidth = texelWidth;
        setFloat(uniformTexelWidthLocation, texelWidth);
    }

    public void setTexelHeight(final float texelHeight) {
        hasOverriddenImageSizeFactor = true;
        this.texelHeight = texelHeight;
        setFloat(uniformTexelHeightLocation, texelHeight);
    }

    public void setLineSize(final float size) {
        lineSize = size;
        texelWidth = size / getOutputWidth();
        texelHeight = size / getOutputHeight();
        updateTexelValues();
    }

    private void updateTexelValues() {
        setFloat(uniformTexelWidthLocation, texelWidth);
        setFloat(uniformTexelHeightLocation, texelHeight);
    }
}
