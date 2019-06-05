package com.mth2610.flutter_gpu_image;

import com.mth2610.flutter_gpu_image.base_filters.GPUImageFilter;
import com.mth2610.flutter_gpu_image.base_filters.GPUImageFilterGroup;

import com.mth2610.flutter_gpu_image.filter.*;
import com.mth2610.flutter_gpu_image.instagram_filters.*;
import com.mth2610.flutter_gpu_image.blind_filters.*;
import com.mth2610.flutter_gpu_image.art_filters.*;
import com.mth2610.flutter_gpu_image.blur_filters.*;

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

//Glitch
//https://www.shadertoy.com/view/Md3cWN

/** FlutterGpuImagePlugin */
public class FlutterGpuImagePlugin implements MethodCallHandler {
    private final Registrar mRegistrar;
    private SurfaceTexture surfaceTexture;
    private GPUImage gpuImage;
    private boolean isInit = false;

    private GPUImageFilter[] ORTHER_FILTERS;
    /** Plugin registration. */
    public FlutterGpuImagePlugin(Registrar registrar) {
        this.mRegistrar = registrar;
        this.ORTHER_FILTERS = new GPUImageFilter[]{

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
        //this.gpuImage = new GPUImage(mRegistrar.context(), surfaceTexture);


        this.isInit = true;
        result.success(entry.id());
    }else if(call.method.equals("applyFilter")) {
        if(isInit!=true){
            result.error("Process failed", "Not initilized", "Not initilized");
        }else{
            GPUImage2 gpuImage = new GPUImage2(mRegistrar.context());
            GLTextureView2 glTextureView = new GLTextureView2(mRegistrar.context(), surfaceTexture);


            String inputFilePath = call.argument("inputFilePath");
            String outputFilePath = call.argument("outputFilePath");
            int filter = call.argument("filter");
            Bitmap inputBitmap = BitmapFactory.decodeFile(inputFilePath);
            String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";

            try {
                //ExifInterface inputExif = new ExifInterface(inputFilePath);
                surfaceTexture.setDefaultBufferSize(inputBitmap.getWidth(), inputBitmap.getHeight());
                gpuImage.setImage(inputBitmap);
                gpuImage.setFilter(ORTHER_FILTERS[filter]);
                gpuImage.setGLTextureView(glTextureView);

                //gpuImage.getBitmapWithFilterApplied();
                //gpuImage.getBitmapWithFilterApplied(inputBitmap);
                result.success("success");
                //gpuImage.saveToPictures(inputBitmap, outputFilePath, outputFileName, null, result, inputExif);
            }catch (Exception e){
                result.error("error", "error", e.toString());
                inputBitmap.recycle();
            }catch (Error e){
                result.error("error", "error", e.toString());
                inputBitmap.recycle();
            }
        }
    }else if(call.method.equals("applyFilterAndSaveToFile")) {
        if(isInit!=true){
            result.error("Process failed", "Not initilized", "Not initilized");
        }else{
            gpuImage = new GPUImage(mRegistrar.context());
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
            gpuImage = new GPUImage(mRegistrar.context());
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
