uniform sampler2D sampler;
uniform sampler2D sampler2;
uniform sampler2D mask;
uniform float path;

void main(void)
{
   float blurSize = 0.004;
   if(path!=1.0){
    vec4 sum = vec4(0.0,0.0,0.0,1.0);
    sum += texture2D(sampler, gl_TexCoord[0].st + vec2(-4.0*blurSize, 0)) * 0.05;// * texture2D(mask, gl_TexCoord[0].st + vec2(-4.0*blurSize, 0)).r;
    sum += texture2D(sampler, gl_TexCoord[0].st + vec2(-3.0*blurSize, 0)) * 0.09;// * texture2D(mask, gl_TexCoord[0].st + vec2(-3.0*blurSize, 0)).r;
    sum += texture2D(sampler, gl_TexCoord[0].st + vec2(-2.0*blurSize, 0)) * 0.12;// * texture2D(mask, gl_TexCoord[0].st + vec2(-2.0*blurSize, 0)).r;
    sum += texture2D(sampler, gl_TexCoord[0].st + vec2(-1.0*blurSize, 0)) * 0.15;// * texture2D(mask, gl_TexCoord[0].st + vec2(-1.0*blurSize, 0)).r;
    sum += texture2D(sampler, gl_TexCoord[0].st + vec2(0            , 0)) * 0.16;//* texture2D(mask, gl_TexCoord[0].st + vec2(0            , 0)).r;
    sum += texture2D(sampler, gl_TexCoord[0].st + vec2(+1.0*blurSize, 0)) * 0.15;// * texture2D(mask, gl_TexCoord[0].st + vec2(+1.0*blurSize, 0)).r;
    sum += texture2D(sampler, gl_TexCoord[0].st + vec2(+2.0*blurSize, 0)) * 0.12;// * texture2D(mask, gl_TexCoord[0].st + vec2(+2.0*blurSize, 0)).r;
    sum += texture2D(sampler, gl_TexCoord[0].st + vec2(+3.0*blurSize, 0)) * 0.09;// * texture2D(mask, gl_TexCoord[0].st + vec2(+3.0*blurSize, 0)).r;
    sum += texture2D(sampler, gl_TexCoord[0].st + vec2(+4.0*blurSize, 0)) * 0.05;// * texture2D(mask, gl_TexCoord[0].st + vec2(+4.0*blurSize, 0)).r;
    
   gl_FragData[0] = sum;
   } else {
    vec4 sum = vec4(0.0,0.0,0.0,1.0);
    vec4 texMask = texture2D(mask, gl_TexCoord[0].st);
    sum += texture2D(sampler2, gl_TexCoord[0].st + vec2(0,-4.0*blurSize)) * 0.05;
    sum += texture2D(sampler2, gl_TexCoord[0].st + vec2(0,-3.0*blurSize)) * 0.09;
    sum += texture2D(sampler2, gl_TexCoord[0].st + vec2(0,-2.0*blurSize)) * 0.12;
    sum += texture2D(sampler2, gl_TexCoord[0].st + vec2(0,-1.0*blurSize)) * 0.15;
    sum += texture2D(sampler2, gl_TexCoord[0].st + vec2(0,            0)) * 0.16;
    sum += texture2D(sampler2, gl_TexCoord[0].st + vec2(0,+1.0*blurSize)) * 0.15;
    sum += texture2D(sampler2, gl_TexCoord[0].st + vec2(0,+2.0*blurSize)) * 0.12;
    sum += texture2D(sampler2, gl_TexCoord[0].st + vec2(0,+3.0*blurSize)) * 0.09;
    sum += texture2D(sampler2, gl_TexCoord[0].st + vec2(0,+4.0*blurSize)) * 0.05;
    vec4 resultColor = sum;   // Glow effect
   // vec4 resultColor = sum;  // Blur effect
   // if (texMask==0.0)
   //     resultColor += texMask;
  // resultColor = sum;
   // gl_FragData[0] = texture2D(mask, gl_TexCoord[0].st);
    gl_FragData[0] = sum + texMask;
   }
 }