
varying vec3 P;
varying vec3 N;
varying vec3 T;
varying vec3 B;
uniform sampler2D normalMap;
uniform sampler2D colorMap;
uniform float inv;

void main(void)
{
    vec3 grad = (texture2D(normalMap, gl_TexCoord[0].st).xyz - 0.5) * 2.0 * inv;
    vec3 L = normalize(gl_LightSource[0].position.xyz - P);
    vec3  NN = normalize( N - grad.x * T - grad.y * B);
    vec4 ambient = gl_FrontLightProduct[0].ambient;
    float dotNL = dot(NN, L);
    vec4 diffuse = gl_FrontLightProduct[0].diffuse * max(0.0, dotNL);
    vec3 V = normalize(-P);
    vec3 H = normalize(L + V);
    float powNH = pow(max(dot(NN, H), 0.0), gl_FrontMaterial.shininess);
    if(dotNL <= 0.0) powNH = 0.0;
    vec4 specular = gl_FrontLightProduct[0].specular * powNH;
    vec4 texColor = texture2D(colorMap, gl_TexCoord[0].st);
    gl_FragColor = (ambient + diffuse) * texColor + specular; 
}

