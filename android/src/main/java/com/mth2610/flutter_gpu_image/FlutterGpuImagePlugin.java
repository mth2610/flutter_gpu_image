package com.mth2610.flutter_gpu_image;

import com.mth2610.flutter_gpu_image.base_filters.GPUImageFilter;
import com.mth2610.flutter_gpu_image.base_filters.GPUImageFilterGroup;

import com.mth2610.flutter_gpu_image.filter.*;
import com.mth2610.flutter_gpu_image.glitch_filters.GPUImageVHSFilter;
import com.mth2610.flutter_gpu_image.instagram_filters.*;
import com.mth2610.flutter_gpu_image.blind_filters.*;
import com.mth2610.flutter_gpu_image.art_filters.*;
import com.mth2610.flutter_gpu_image.blur_filters.*;
import com.mth2610.flutter_gpu_image.beauty_filters.*;
import com.mth2610.flutter_gpu_image.natural_filters.*;
import com.mth2610.flutter_gpu_image.glitch_filters.*;

import android.graphics.SurfaceTexture;

import java.util.List;
import java.util.ArrayList;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import io.flutter.view.TextureRegistry;

import android.media.ExifInterface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.util.Map;
import java.util.HashMap;

//Hexagon
//https://www.shadertoy.com/view/XlKyRz

//Line filter
//https://www.shadertoy.com/view/XlXGzf

//Rain drop
//https://www.shadertoy.com/view/MlfBWr

//Super resolution
//https://www.shadertoy.com/view/ltsczl

//Mesh filter
//https://www.shadertoy.com/view/ldV3Wc

//Low poly
//https://www.shadertoy.com/view/llGGz3

//Notebook
//https://www.shadertoy.com/view/XtVGD1

//Handdraw
//https://www.shadertoy.com/view/MsSGD1

//Radial blur
//https://www.shadertoy.com/view/4sfGRn

//ballpoint Sketch
//https://www.shadertoy.com/view/ldtcWS

//Money Sketch
//https://www.shadertoy.com/view/XlsXDN

//Monitor Glicht
//https://www.shadertoy.com/view/ltSSWV

// Sketch book color
//https://www.shadertoy.com/view/ldlcWs

//Fade-in effect
//https://www.shadertoy.com/view/MlcSz2

// Smoke
// https://www.shadertoy.com/view/XtS3Rh

// Smoke
// https://www.shadertoy.com/view/ldBSDd


// Fake TV Screen Malfunction
// https://www.shadertoy.com/view/4scGDr

// Nipple Enchanter
// https://www.shadertoy.com/view/lljSzz
// https://www.shadertoy.com/view/Ml2Szz
// https://www.shadertoy.com/view/Xt2XRR
// https://www.shadertoy.com/view/XdGXDh
// https://www.shadertoy.com/view/MtjSzz


// Cubic
// https://www.shadertoy.com/view/XscSWH

//Spark
//https://www.shadertoy.com/view/3d2XWm
//https://www.shadertoy.com/view/ldVfWV

//https://www.shadertoy.com/view/MlKSWm fire
//https://www.shadertoy.com/view/4sfBWj

//fireflies
//https://www.shadertoy.com/view/4d2fDz
//https://www.shadertoy.com/view/Wtj3D1
//https://www.shadertoy.com/view/MtjBWc

//https://www.shadertoy.com/view/3lfGR7 fire frame

// https://www.shadertoy.com/view/Wd23DG boked

//Sakura
// https://www.shadertoy.com/view/WtSGDz

//Snow flake
//https://www.shadertoy.com/view/Xsd3zf
//https://www.shadertoy.com/view/Mdt3Df

//https://www.shadertoy.com/view/ltXXDN
//https://www.shadertoy.com/view/4scXWB
//https://www.shadertoy.com/view/MlSSWV
//https://www.shadertoy.com/view/XsVSWG

// GLSL
//http://glslsandbox.com/e#53264.0

/** FlutterGpuImagePlugin */
public class FlutterGpuImagePlugin implements MethodCallHandler {
    private final Registrar mRegistrar;
    private SurfaceTexture surfaceTexture;
    private GPUImage2 gpuImage;
    private Bitmap bitmap;
    private GLTextureView2 glTextureView;
    private boolean isInit = false;

    private GPUImageFilter[] ORTHER_FILTERS;
    /** Plugin registration. */
    public FlutterGpuImagePlugin(Registrar registrar) {
        this.mRegistrar = registrar;
        this.ORTHER_FILTERS = new GPUImageFilter[]{
                new GPUImageOldTVScreenFilter(),
                new GPUImageColorBlendFilter(),
                new GPUImageRainbowFilter(),
                new GPUImageFireFliesFilter(),
                new GPUImageEdgeGlowFilter(),
                new GPUImageNippleEnchanterFilter(),
                new GPUImageBeautyFilter(),
                new GPUImageRadicalBlurFilter(),
                new GPUImageFireFilter(),
                new GPUImageSmoke1Filter(),

                new GPUImageBokehFilter(),
                new GPUImageGlitchFilter(),
                new GPUImageVHSFilter(),
                new GPUImageDeuteranomalyFilter(),
                new GPUImageProtanopiaFilter(),
                new GPUImageProtanomalyFilter(),
                new GPUImageDeuteranopiaFilter(),
                new GPUImageTritanomalyFilter(),
                new GPUImageAchromatopsiaFilter(),
                new GPUImageAchromatomalyFilter(),
                new GPUImageTritanopiaFilter(),

                new GPUImageSketchFilter2(),
                new GPUImageSketchFilter(),
                new GPUImageWaterColorFilter(),
                new GPUImageBilateralBlurFilter(),
                new GPUImageBulgeDistortionFilter(),
                new GPUImageColorInvertFilter(),
                new GPUImageContrastFilter(),
                //new GPUImageEmbossFilter(),
                new GPUImageFalseColorFilter(),
                new GPUImageHazeFilter(),
                new GPUImageKuwaharaFilter(),
                new GPUImagePixelationFilter(),
                new GPUImagePosterizeFilter(),
                //new GPUImageSepiaToneFilter(),
                new GPUImageSharpenFilter(),
                //new com.mth2610.gpu_image.filter.GPUImageSketchFilter2(),
                //new GPUImageSmoothToonFilter(),
                //new GPUImageSolarizeFilter(),
                new GPUImageSwirlFilter(),
                new GPUImageToonFilter(),
                new GPUImageVignetteFilter(),
                new GPUImageZoomBlurFilter(),
                new GPUImageBoxBlurFilter(),
                new GPUImageGaussianBlurFilter(),

                new IF1977Filter(registrar.context()),
                new IFAmaroFilter(registrar.context()),
                new IFBrannanFilter(registrar.context()),
                new IFEarlybirdFilter(registrar.context()),
                new IFHefeFilter(registrar.context()),
                new IFHudsonFilter(registrar.context()),
                new IFInkwellFilter(registrar.context()),
                new IFLomoFilter(registrar.context()),
                new IFLordKelvinFilter(registrar.context()),
                new IFNashvilleFilter(registrar.context()),
                new IFRiseFilter(registrar.context()),
                new IFSierraFilter(registrar.context()),
                new IFSutroFilter(registrar.context()),
                new IFToasterFilter(registrar.context()),
                new IFValenciaFilter(registrar.context()),
                new IFWaldenFilter(registrar.context()),
                new IFXprollFilter(registrar.context()),

                new GPUImageLowPolyFilter(),
                new GPUImageSnowFilter(),
                new GPUImageSkinSmoothFilter(),
                new GPUImageSkinWhitenFilter(),
                new GPUImageColorSketchFilter(),
                new GPUImageAntiqueFilter(),


        };
    }
    
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_gpu_image");
    channel.setMethodCallHandler(new FlutterGpuImagePlugin(registrar));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("init")) {
        TextureRegistry.SurfaceTextureEntry entry = mRegistrar.textures().createSurfaceTexture();
        this.surfaceTexture = entry.surfaceTexture();
        this.gpuImage = new GPUImage2(mRegistrar.context());
        this.glTextureView = new GLTextureView2(surfaceTexture);
        glTextureView.init();
        this.isInit = true;
        result.success(entry.id());
    } else if(call.method.equals("setInputImage")){
        gpuImage.deleteImage();
        String inputFilePath = call.argument("inputFilePath");
        int rotate = 0;
        try {
            ExifInterface originalExif = new ExifInterface(inputFilePath);
            int orientation = originalExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            if(orientation == ExifInterface.ORIENTATION_ROTATE_90){
                rotate = 90;
            }else if(orientation == ExifInterface.ORIENTATION_ROTATE_180){
                rotate = 180;
            }else if(orientation == ExifInterface.ORIENTATION_ROTATE_270){
                rotate = 270;
            } else {
                rotate = 0;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeFile(inputFilePath);
        surfaceTexture.setDefaultBufferSize(bitmap.getWidth(), bitmap.getHeight());
        gpuImage.setImage(bitmap);
        glTextureView.onTextureSizeChange(bitmap.getWidth(), bitmap.getHeight());
        gpuImage.setGLTextureView(glTextureView);

        Map<String, Object> output = new HashMap<>();
        output.put("width", bitmap.getWidth());
        output.put("height", bitmap.getHeight());
        output.put("rotation", rotate);
        result.success(output);

    } else if(call.method.equals("applyFilter")) {
        if(isInit!=true||bitmap==null){
            result.error("Process failed", "Not initilized", "Not initilized");
        }else{
            int filter = call.argument("filter");
            try {
                gpuImage.setFilter(ORTHER_FILTERS[filter]);
                result.success("success");
            }catch (Exception e){
                result.error("error", "error", e.toString());
            }catch (Error e){
                result.error("error", "error", e.toString());
            }
        }
    } else if(call.method.equals("applyFilters")) {
        if(isInit!=true||bitmap==null){
            result.error("Process failed", "Not initilized", "Not initilized");
        }else{
            try {
                ArrayList filters = call.argument("filters");
                List<GPUImageFilter> gpuImageFilters = new ArrayList<GPUImageFilter>();
                for(int i=0; i <filters.size(); i++){
                    gpuImageFilters.add(ORTHER_FILTERS[(int) filters.get(i)]);
                }
                GPUImageFilter gPUImageGroupFilter = new GPUImageFilterGroup(gpuImageFilters);
                gpuImage.setFilter(gPUImageGroupFilter);
                result.success("success");
            }catch (Exception e){
                result.error("error", "error", e.toString());
            }catch (Error e){
                result.error("error", "error", e.toString());
            }
        }
    } else if(call.method.equals("applyFilterAndSaveToFile")) {
        if(isInit!=true){
            result.error("Process failed", "Not initilized", "Not initilized");
        }else{
            gpuImage = new GPUImage2(mRegistrar.context());
            String inputFilePath = call.argument("inputFilePath");
            String outputFilePath = call.argument("outputFilePath");
            int filter = call.argument("filter");
            Bitmap inputBitmap = BitmapFactory.decodeFile(inputFilePath);
            String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";
            gpuImage.setFilter(ORTHER_FILTERS[filter]);
            try {
                ExifInterface inputExif = new ExifInterface(inputFilePath);
                gpuImage.saveToPictures(inputBitmap, outputFilePath, outputFileName, null, result, inputExif);
            }catch (Exception e){
                result.error("error", "error", e.toString());
                inputBitmap.recycle();
            }catch (Error e){
                result.error("error", "error", e.toString());
                inputBitmap.recycle();
            }
            Log.i("test4", "test4");
        }
    }else if(call.method.equals("applyMultiFiltersAndSaveToFile")) {
        if(isInit!=true){
            result.error("Process failed", "Not initilized", "Not initilized");
        }else{
            gpuImage = new GPUImage2(mRegistrar.context());
            String inputFilePath = call.argument("inputFilePath");
            String outputFilePath = call.argument("outputFilePath");
            ArrayList filters = call.argument("filters");
            Bitmap inputBitmap = BitmapFactory.decodeFile(inputFilePath);
            String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";
            List<GPUImageFilter> gpuImageFilters = new ArrayList<GPUImageFilter>();

            for(int i=0; i <filters.size(); i++){
                gpuImageFilters.add(ORTHER_FILTERS[(int) filters.get(i)]);
            }

            GPUImageFilter gPUImageGroupFilter = new GPUImageFilterGroup(gpuImageFilters);
            gpuImage.setFilter(gPUImageGroupFilter);
            try {
                ExifInterface inputExif = new ExifInterface(inputFilePath);
                gpuImage.saveToPictures(inputBitmap, outputFilePath, outputFileName, null, result, inputExif);
            }catch (Exception e){
                result.error("error", "error", e.toString());
                inputBitmap.recycle();
            }catch (Error e){
                result.error("error", "error", e.toString());
                inputBitmap.recycle();
            }
            Log.i("test4", "test4");
        }
    }
    else {
      result.notImplemented();
    }
  }
}
