package com.mth2610.flutter_gpu_image.filter;

// https://www.shadertoy.com/view/ldXfRj
public class GPUImageSketchFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +
            "const float PI2 = float( 6.28318530717959 );"+
            "const float RANGE = float(16.0);"+
            "const float STEP = float(2.0);"+
            "const float ANGLENUM = float(4.0);"+
            "const float MAGIC_GRAD_THRESH = float(0.01);"+
            "const float MAGIC_SENSITIVITY = float(10.0);"+
            "const float MAGIC_COLOR  = float(0.5);"+

            "vec4 getCol(vec2 pos)"+
            "{"+
            "   return texture2D(inputImageTexture, pos);"+
            "}"+

            "float getVal(vec2 pos)"+
            "{"+
            "   vec4 c=getCol(pos);"+
            "       return dot(c.xyz, vec3(0.2126, 0.7152, 0.0722));"+
            "}"+

            "vec2 getGrad(vec2 pos, float eps)"+
            "{"+
            "   vec2 d=vec2(eps, 0);"+
            "   return vec2("+
            "       getVal(pos+d.xy)-getVal(pos-d.xy),"+
            "       getVal(pos+d.yx)-getVal(pos-d.yx)" +
            "   )/eps/2.0;" +
            "}" +

            "vec2 pR(inout vec2 p, float a) {" +
            "   vec2 result = cos(a)*p + sin(a)*vec2(p.y, -p.x);" +
            "   return result;"+
            "}" +

            "float absCircular(float t)" +
            "{" +
            "   float a = floor(t + 0.5);" +
            "   return mod(abs(a - t), 1.0);" +
            "}" +

            "void main()" +
            "{" +
            "   vec2 pos = textureCoordinate;"+
            "   float weight = 1.0;"+
            "   for (float j = 0.0; j < ANGLENUM; j += 1.0)"+
            "   {"+
            "       vec2 dir = vec2(1, 0);"+
            "       dir = pR(dir, j * PI2 / (2.0 * ANGLENUM));"+
            "       vec2 grad = vec2(-dir.y, dir.x);"+
            "       for (float i = -RANGE; i <= RANGE; i += STEP)"+
            "       {"+
            "           vec2 pos2 = pos + normalize(dir)*i;"+
            // video texture wrap can't be set to anything other than clamp  (-_-)
            "   if (pos2.y < 0.0 || pos2.x < 0.0 || pos2.x > resolution.x || pos2.y > resolution.y)"+
            "       continue;"+
            "   vec2 g = getGrad(pos2, 1.0);"+
            "   if (length(g) < MAGIC_GRAD_THRESH)"+
            "   continue;"+
            "   weight -= pow(abs(dot(normalize(grad), normalize(g))), MAGIC_SENSITIVITY) / floor((2.0 * RANGE + 1.0) / STEP) / ANGLENUM;"+
            "       }"+
            "   }"+
            "   vec4 col = vec4(getVal(pos));"+
            "   vec4 background = mix(col, vec4(1), MAGIC_COLOR);"+
            // because apparently all shaders need one of these. It's like a law or something.
            "   float r = length(pos - resolution.xy*0.5) / resolution.x;"+
            "   float vign = 1.0 - r*r*r;"+
            "   vec4 a = texture2D(inputImageTexture, pos/resolution.xy);"+
            "   gl_FragColor = vign * mix(vec4(0), background, weight) + a.xxxx/25.0;"+
            "}";


    public GPUImageSketchFilter() {
        super(FRAGMENT_SHADER);
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
    }
}
