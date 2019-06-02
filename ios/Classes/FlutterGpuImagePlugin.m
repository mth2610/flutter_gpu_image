#import "FlutterGpuImagePlugin.h"
#import <flutter_gpu_image/flutter_gpu_image-Swift.h>

@implementation FlutterGpuImagePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterGpuImagePlugin registerWithRegistrar:registrar];
}
@end
