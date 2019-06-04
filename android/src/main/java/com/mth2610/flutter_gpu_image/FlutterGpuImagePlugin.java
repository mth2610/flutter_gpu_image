package com.mth2610.flutter_gpu_image;
//import com.mth2610.flutter_gpu_image.filter.GPUImageFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageGrayscaleFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageBokehFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageGlitchFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageVHSFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageDeuteranomalyFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageProtanopiaFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageProtanomalyFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageDeuteranopiaFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageTritanomalyFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageAchromatopsiaFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageAchromatomalyFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageTritanopiaFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageSketchFilter;
//import com.mth2610.flutter_gpu_image.filter.GPUImageWaterColorFilter;

import com.mth2610.flutter_gpu_image.filter.*;
import com.mth2610.flutter_gpu_image.instagram_filter.*;

import android.graphics.SurfaceTexture;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import io.flutter.view.TextureRegistry;
import android.view.View;
import android.opengl.GLSurfaceView;

import android.media.ExifInterface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

// https://www.shadertoy.com/view/XdB3zV
// https://www.shadertoy.com/view/MdSGRW
// https://www.shadertoy.com/view/XsXGDX
// https://www.shadertoy.com/view/XdfXRB
// https://www.shadertoy.com/view/4lV3RG

/** FlutterGpuImagePlugin */
public class FlutterGpuImagePlugin implements MethodCallHandler {
    private final Registrar mRegistrar;
    private SurfaceTexture surfaceTexture;
    private GPUImage gpuImage;
    private boolean isInit = false;

    private GPUImageFilter[] FILTERS;
    /** Plugin registration. */
    public FlutterGpuImagePlugin(Registrar registrar) {
        this.mRegistrar = registrar;
        this.FILTERS = new GPUImageFilter[]{
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
            gpuImage = new GPUImage(mRegistrar.context(), surfaceTexture);
            String inputFilePath = call.argument("inputFilePath");
            String outputFilePath = call.argument("outputFilePath");
            int filter = call.argument("filter");
            Bitmap inputBitmap = BitmapFactory.decodeFile(inputFilePath);
            String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";
            gpuImage.setFilter(FILTERS[filter]);
            try {
                //ExifInterface inputExif = new ExifInterface(inputFilePath);
                //gpuImage.setImage(inputBitmap);
                gpuImage.getBitmapWithFilterApplied(inputBitmap);
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
            gpuImage = new GPUImage(mRegistrar.context(), surfaceTexture);
            String inputFilePath = call.argument("inputFilePath");
            String outputFilePath = call.argument("outputFilePath");
            int filter = call.argument("filter");
            Bitmap inputBitmap = BitmapFactory.decodeFile(inputFilePath);
            String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";
            gpuImage.setFilter(FILTERS[filter]);
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
            gpuImage = new GPUImage(mRegistrar.context(), surfaceTexture);
            String inputFilePath = call.argument("inputFilePath");
            String outputFilePath = call.argument("outputFilePath");
            int[] filters = call.argument("filters");
            Bitmap inputBitmap = BitmapFactory.decodeFile(inputFilePath);
            String outputFileName = String.valueOf(System.currentTimeMillis()) + ".png";
            List<GPUImageFilter> gpuImageFilters = new ArrayList<GPUImageFilter>();
            gpuImageFilters.add(FILTERS[1]);
            gpuImageFilters.add(FILTERS[2]);
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
