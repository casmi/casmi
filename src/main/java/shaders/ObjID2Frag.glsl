uniform sampler2D sampler;
uniform float mask;
uniform float texOn;
uniform float draw;
//0 : all
//1 : mask
//2 : glow and none

void main(void)
{
    float m = mask;
    if(texOn == 1.0){
        vec4 texColor = texture2D(sampler, gl_TexCoord[0].st);
        gl_FragData[0] = gl_Color * texColor;
        gl_FragData[1] = vec4(texColor.a * m, texColor.a * m, texColor.a * m, texColor.a * m);
        if(draw == 1.0)
            gl_FragData[2] = gl_Color * texColor;
        else
            gl_FragData[2] = vec4(0.0, 0.0, 0.0, 0.0);   
    } else {
        gl_FragData[0] = gl_Color;
        gl_FragData[1] = vec4(m, m, m, m);
        if(draw == 1.0)
            gl_FragData[2] = gl_Color;
        else
            gl_FragData[2] = vec4(0.0, 0.0, 0.0, 0.0);   
    }
}
