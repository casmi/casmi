varying vec3 P;
varying vec3 N;
uniform float time;

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
    gl_FragColor = ambient + diffuse + specular;
    //gl_FragColor = vec4(rand(vec2(1.0,1.0)), rand(vec2(0.0,1.0)), rand(vec2(1.0,0.0)), 1.0);
}