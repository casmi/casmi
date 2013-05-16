varying vec3 P;
varying vec3 N;
uniform float time;
uniform sampler2D sampler;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453+ time);
}


void main(void)
{
    vec3 L = normalize(gl_LightSource[0].position.xyz - P);
    vec3 NN = normalize(N);
    vec4 ambient = gl_FrontLightProduct[0].ambient;
    float dotNL = dot(NN, L);
    vec4 diffuse = gl_FrontLightProduct[0].diffuse * max(0.0, dotNL);
    vec3 V = normalize(-P);
    vec3 H = normalize(L + V);
    float powNH = pow(max(dot(NN, H), 0.0), gl_FrontMaterial.shininess);
    if(dotNL <= 0.0) powNH = 0.0;
    vec4 specular = gl_FrontLightProduct[0].specular * powNH;
    vec4 texColor = texture2D(sampler, gl_TexCoord[0].st);
    gl_FragColor = (ambient + diffuse) * texColor + specular;
}