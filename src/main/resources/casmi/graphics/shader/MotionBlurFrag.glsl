uniform sampler2D sampler;
uniform sampler2D sampler2;
uniform float path;

void main(void)
{
   vec4 nowTexColor = texture2D(sampler, gl_TexCoord[0].st);
   vec4 oldTexColor = texture2D(sampler2, gl_TexCoord[0].st);
   if(path==0.0)
    gl_FragColor = oldTexColor;
   else
   gl_FragColor = nowTexColor + oldTexColor * 0.8;
 }