package com.mth2610.flutter_gpu_image.art_filters;
import com.mth2610.flutter_gpu_image.base_filters.GPUImage3x3TextureSamplingFilter2;

import android.opengl.GLES20;

public class GPUImageBokehFilter extends GPUImage3x3TextureSamplingFilter2  {
    public static final String PRIORITYMAP_FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "uniform sampler2D inputImageTexture;" +
            "varying vec2 textureCoordinate;" +
            "varying vec2 resolution;" +
            "const float MATH_PI = float( 3.14159265359 );" +
            "vec2 Rotate(vec2 p, float a )" +
            "{" +
            "    vec2 result = cos( a ) * p + sin( a ) * vec2( p.y, -p.x );" +
            "    return result;" +
            "}" +

            "float Circle( vec2 p, float r )" +
            "{" +
            "   return ( length( p / r ) - 1.0 ) * r;" +
            "}" +

            " float Rand( vec2 c )" +
            "{" +
            "    return fract( sin( dot( c.xy, vec2( 12.9898, 78.233 ) ) ) * 43758.5453 );" +
            "}" +

            "float saturate( float x )" +
            "{" +
            "    return clamp( x, 0.0, 1.0 );" +
            "}" +

            "vec3 BokehLayer(vec3 color, vec2 p, vec3 c )"+
            "{"+
            "   float wrap = 450.0;"+
            "   if ( mod( floor( p.y / wrap + 0.5 ), 2.0 ) == 0.0 )"+
            "   {"+
            "       p.x += wrap * 0.5;"+
            "   }"+
            "   vec2 p2 = mod( p + 0.5 * wrap, wrap ) - 0.5 * wrap;"+
            "   vec2 cell = floor( p / wrap + 0.5 );"+
            "   float cellR = Rand( cell );"+
            "   c *= fract( cellR * 3.33 + 3.33 );"+
            "   float radius = mix( 30.0, 70.0, fract( cellR * 7.77 + 7.77 ) );"+
            "   p2.x *= mix( 0.9, 1.1, fract( cellR * 11.13 + 11.13 ) );"+
            "   p2.y *= mix( 0.9, 1.1, fract( cellR * 17.17 + 17.17 ) );"+
            "   float sdf = Circle( p2, radius );"+
            "   float circle = 1.0 - smoothstep( 0.0, 1.0, sdf * 0.04 );"+
            "   float glow\t = exp( -sdf * 0.025 ) * 0.3 * ( 1.0 - circle );"+
            "   vec3 result = c * ( circle + glow );"+
            "   return result;"+
            "}"+
            "void main()" +
            "{" +
            "   vec2 uv = textureCoordinate;"+
            "   vec2 iResolution = resolution;"+
            "   vec2 p = ( 2.0 * gl_FragCoord.xy - iResolution.xy ) / iResolution.x * 1000.0;"+
            "   vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "   vec3 color = mix( vec3( 0.3, 0.1, 0.3 ), vec3( 0.1, 0.4, 0.5 ), dot( uv, vec2( 0.2, 0.7 ) ) );"+
            "   float time = 30.0;"+
            "   Rotate( p, 0.2 + time * 0.03 );"+
            "   color += BokehLayer( color, p + vec2( -50.0 * time +  0.0, 0.0  ), 3.0 * vec3( 0.4, 0.1, 0.2 ) );"+
            "   Rotate( p, 0.3 - time * 0.05 );"+
            "   color += BokehLayer( color, p + vec2( -70.0 * time + 33.0, -33.0 ), 3.5 * vec3( 0.6, 0.4, 0.2 ) );"+
            "   Rotate( p, 0.5 + time * 0.07 );"+
            "   color += BokehLayer( color, p + vec2( -60.0 * time + 55.0, 55.0 ), 3.0 * vec3( 0.4, 0.3, 0.2 ) );"+
            "   Rotate( p, 0.9 - time * 0.03 );"+
            "   color += BokehLayer( color, p + vec2( -25.0 * time + 77.0, 77.0 ), 3.0 * vec3( 0.4, 0.2, 0.1 ) );"+
            "   Rotate( p, 0.0 + time * 0.05 );"+
            "   color += BokehLayer( color, p + vec2( -15.0 * time + 99.0, 99.0 ), 3.0 * vec3( 0.2, 0.0, 0.4 ) );"+
            "   gl_FragColor = vec4(mix(textureColor.rgb, color.rgb, 0.2), 1.0);" +
            "}";

    private float[] convolutionKernel;
    private int uniformConvolutionMatrix;

    public GPUImageBokehFilter() {
//        this(new float[]{
//                0.5f, 1.0f, 0.5f,
//                1.0f, -6.0f, 1.0f,
//                0.5f, 1.0f, 0.5f
//        });

        this(new float[]{
                0.0f, 1.0f, 0.0f,
                1.0f, -4.0f, 1.0f,
                0.0f, 1.0f, 0.0f
        });
    }

    private GPUImageBokehFilter(final float[] convolutionKernel) {
        super(PRIORITYMAP_FRAGMENT_SHADER);
        this.convolutionKernel = convolutionKernel;
    }

    @Override
    public void onInit() {
        super.onInit();
        uniformConvolutionMatrix = GLES20.glGetUniformLocation(getProgram(), "convolutionMatrix");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setConvolutionKernel(convolutionKernel);
    }

    private void setConvolutionKernel(final float[] convolutionKernel) {
        this.convolutionKernel = convolutionKernel;
        setUniformMatrix3f(uniformConvolutionMatrix, this.convolutionKernel);
    }
}
