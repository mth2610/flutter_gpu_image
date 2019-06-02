import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_gpu_image/flutter_gpu_image.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutter_gpu_image');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await FlutterGpuImage.platformVersion, '42');
  });
}
