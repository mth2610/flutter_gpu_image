import 'dart:async';

import 'package:flutter/services.dart';

class FlutterGpuImage {
  static const MethodChannel _channel =
      const MethodChannel('flutter_gpu_image');

  Future<int> init() async {
    int textureSurfaceID = await _channel.invokeMethod('init', {
    });
    return textureSurfaceID;
  }

  Future<String> process({String inputFilePath, String outputFilePath, int filter}) async {
    String outPutFilePath = await _channel.invokeMethod('process', {
      'inputFilePath': inputFilePath,
      'outputFilePath': outputFilePath,
      'filter': filter,
    });
    return outPutFilePath;
  }
}
