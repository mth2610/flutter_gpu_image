package com.mth2610.flutter_gpu_image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.egl.EGL10.EGL_DEFAULT_DISPLAY;
import static javax.microedition.khronos.egl.EGL10.EGL_HEIGHT;
import static javax.microedition.khronos.egl.EGL10.EGL_NONE;
import static javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT;
import static javax.microedition.khronos.egl.EGL10.EGL_WIDTH;

import static javax.microedition.khronos.egl.EGL10.EGL_ALPHA_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_BLUE_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_DEFAULT_DISPLAY;
import static javax.microedition.khronos.egl.EGL10.EGL_DEPTH_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_GREEN_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_HEIGHT;
import static javax.microedition.khronos.egl.EGL10.EGL_NONE;
import static javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT;
import static javax.microedition.khronos.egl.EGL10.EGL_RED_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_STENCIL_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_WIDTH;
/**
 * Created on 15-10-18.
 */
public class GLTextureView2  {
    private final static String TAG = "GLTexture2";
    private final static boolean LIST_CONFIGS = false;

    private GLSurfaceView.Renderer renderer; // borrow this interface
    private int width;
    private int height;

    private EGL10 egl10;
    private EGLDisplay eglDisplay;
    private EGLConfig[] eglConfigs;
    private EGLConfig eglConfig;
    private EGLContext eglContext;
    private EGLSurface eglSurface;
    private SurfaceTexture surfaceTexture;
    private GL10 gl10;
    private String mThreadOwner;

    public GLTextureView2(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
    }

    public void init(){

        int[] version = new int[2];
        // No error checking performed, minimum required code to elucidate logic
        egl10 = (EGL10) EGLContext.getEGL();
        eglDisplay = egl10.eglGetDisplay(EGL_DEFAULT_DISPLAY);
        egl10.eglInitialize(eglDisplay, version);
        eglConfig = chooseConfig(); // Choosing a config is a little more
        // complicated

        int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
        int[] attrib_list = {
                EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL10.EGL_NONE
        };
        eglContext = egl10.eglCreateContext(eglDisplay, eglConfig, EGL_NO_CONTEXT, attrib_list);
        eglSurface = egl10.eglCreateWindowSurface(eglDisplay, eglConfig, surfaceTexture, null);

        egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
        gl10 = (GL10) eglContext.getGL();

        // Record thread owner of OpenGL context
        mThreadOwner = Thread.currentThread().getName();
    }

    public void onTextureSizeChange(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setRenderer(final GLSurfaceView.Renderer renderer) {
        this.renderer = renderer;

        // Does this thread own the OpenGL context?
        if (!Thread.currentThread().getName().equals(mThreadOwner)) {
            Log.e(TAG, "setRenderer: This thread does not own the OpenGL context.");
            return;
        }

        // Call the renderer initialization routines
        this.renderer.onSurfaceCreated(gl10, eglConfig);
        this.renderer.onSurfaceChanged(gl10, width, height);
    }

    public void requestRender(){
        // Do we have a renderer?
        if (renderer == null) {
            Log.e(TAG, "getBitmap: Renderer was not set.");
        }

        // Does this thread own the OpenGL context?
        if (!Thread.currentThread().getName().equals(mThreadOwner)) {
            Log.e(TAG, "getBitmap: This thread does not own the OpenGL context.");
        }
        // Call the renderer draw routine (it seems that some filters do not
        // work if this is only called once)
        renderer.onDrawFrame(gl10);
        if (!egl10.eglSwapBuffers(eglDisplay, eglSurface)) {
            Log.e(TAG, "cannot swap buffers!");
        }
    }


    public void destroy() {
        renderer.onDrawFrame(gl10);
        renderer.onDrawFrame(gl10);
        egl10.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);

        egl10.eglDestroySurface(eglDisplay, eglSurface);
        egl10.eglDestroyContext(eglDisplay, eglContext);
        egl10.eglTerminate(eglDisplay);
    }

    private EGLConfig chooseConfig() {
        int[] attribList = new int[]{
                EGL_DEPTH_SIZE, 0,
                EGL_STENCIL_SIZE, 0,
                EGL_RED_SIZE, 8,
                EGL_GREEN_SIZE, 8,
                EGL_BLUE_SIZE, 8,
                EGL_ALPHA_SIZE, 8,
                EGL10.EGL_RENDERABLE_TYPE, 4,
                EGL_NONE
        };

        // No error checking performed, minimum required code to elucidate logic
        // Expand on this logic to be more selective in choosing a configuration
        int[] numConfig = new int[1];
        egl10.eglChooseConfig(eglDisplay, attribList, null, 0, numConfig);
        int configSize = numConfig[0];
        eglConfigs = new EGLConfig[configSize];
        egl10.eglChooseConfig(eglDisplay, attribList, eglConfigs, configSize, numConfig);

        if (LIST_CONFIGS) {
            listConfig();
        }

        return eglConfigs[0]; // Best match is probably the first configuration
    }

    private void listConfig() {
        Log.i(TAG, "Config List {");

        for (EGLConfig config : eglConfigs) {
            int d, s, r, g, b, a;

            // Expand on this logic to dump other attributes
            d = getConfigAttrib(config, EGL_DEPTH_SIZE);
            s = getConfigAttrib(config, EGL_STENCIL_SIZE);
            r = getConfigAttrib(config, EGL_RED_SIZE);
            g = getConfigAttrib(config, EGL_GREEN_SIZE);
            b = getConfigAttrib(config, EGL_BLUE_SIZE);
            a = getConfigAttrib(config, EGL_ALPHA_SIZE);
            Log.i(TAG, "    <d,s,r,g,b,a> = <" + d + "," + s + "," +
                    r + "," + g + "," + b + "," + a + ">");
        }

        Log.i(TAG, "}");
    }

    private int getConfigAttrib(final EGLConfig config, final int attribute) {
        int[] value = new int[1];
        return egl10.eglGetConfigAttrib(eglDisplay, config,
                attribute, value) ? value[0] : 0;
    }
}