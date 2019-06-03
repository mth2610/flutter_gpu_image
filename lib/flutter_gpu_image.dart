import 'dart:async';

import 'package:flutter/services.dart';

enum GPU_FILTERS{
    BokehFilter,
    Glitch,
    VHS,
    Deuteranomaly,
    Protanopia,
    Protanomaly,
    Deuteranopia,
    Tritanomaly,
    Achromatopsia,
    Achromatomaly,
    Tritanopia,
    Sketch,
    Color,
    BilateralBlur,
    BulgeDistortion,
    ColorInvert,
    Contrast,
    //Emboss,
    FalseColor,
    Haze,
    Kuwahara,
    Pixelation,
    Posterize,
    //SepiaTone,
    Sharpen,
    //new com.mth2610.gpu_image.filter.GPUImageSketchFilter2,
    //SmoothToon,
    //Solarize,
    Swirl,
    Toon,
    Vignette,
    ZoomBlur,
    _1977,
    Amaro,
    Brannan,
    Earlybird,
    Hefe,
    Hudson,
    Inkwell,
    Lomo,
    LordKelvin,
    Nashville,
    Rise,
    Sierra,
    Sutro,
    Toaster,
    Valencia,
    Walden,
    Xproll,
}

class FlutterGpuImage {
  static const MethodChannel _channel =
      const MethodChannel('flutter_gpu_image');

  Future<int> init() async {
    int textureSurfaceID = await _channel.invokeMethod('init', {
    });
    return textureSurfaceID;
  }

  Future<String> applyFilter({String inputFilePath, String outputFilePath, int filter}) async {
    String outPutFilePath = await _channel.invokeMethod('applyFilter', {
      'inputFilePath': inputFilePath,
      'outputFilePath': outputFilePath,
      'filter': filter,
    });
    return outPutFilePath;
  }

  Future<String> applyFilterAndSaveToFile({String inputFilePath, String outputFilePath, int filter}) async {
    String outPutFilePath = await _channel.invokeMethod('applyFilterAndSaveToFile', {
      'inputFilePath': inputFilePath,
      'outputFilePath': outputFilePath,
      'filter': filter,
    });
    return outPutFilePath;
  }
}
