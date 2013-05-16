uniform sampler2D normalMap;
 
varying vec3 viewVec;
varying vec3 lightVec;
varying vec3 nn;
varying vec3 tt;

void main (void)
{
    /*
     * 法線マップ座標系(接平面)での光源計算
     */
    /* 法線マップ */
    vec4 normal = texture2DProj(normalMap, gl_TexCoord[0]);
    vec3 fnormal = vec3(normal) * 2.0 - 1.0;
    
    /* 光源ベクトル */
    vec3 flight = normalize(lightVec);
    
    /* 法線と光源との内積 */
    float diffuse = dot(flight, fnormal);

    /* アンビエント */
    gl_FragColor = gl_FrontLightProduct[0].ambient;

    if (diffuse > 0.0)
    {
        /* ハーフベクトル */
        vec3 fview = normalize(viewVec);
        vec3 halfVec = normalize(flight - fview);
        float specular = pow(max(dot(fnormal, halfVec), 0.0), 
                                    gl_FrontMaterial.shininess);

        gl_FragColor += gl_FrontLightProduct[0].diffuse * diffuse
                     +  gl_FrontLightProduct[0].specular * specular;
      //gl_FragColor = normal;
      //  gl_FragColor = vec4(1.0, 0.2, 0.2, 1.0);  
    } else {
        gl_FragColor = vec4(tt.r, tt.g, tt.b, 1.0);  
        }
}