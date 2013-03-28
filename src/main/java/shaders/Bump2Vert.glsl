// vertex shader of bump mapping

/* 接線ベクトル */
attribute vec3 tangent;

varying vec3 viewVec;
varying vec3 lightVec;
varying vec3 nn;
varying vec3 tt;

void main(void)
{
    /* 位置を視点座標に変換 */
    vec3 pos = vec3(gl_ModelViewMatrix * gl_Vertex);
    vec3 lgt = gl_LightSource[0].position.xyz - pos;

    /* 法線と接線を正規化 */
    vec3 n = normalize(gl_NormalMatrix * gl_Normal);
    vec3 t = normalize(gl_NormalMatrix * tangent);
    nn = n;
    tt = t;
    
    /* 従法線 */
    vec3 b = cross(n, t);
    
    /* 法線マップ座標系(接空間)における視線ベクトル */
    vec3 viewV;
    viewV.x = dot(t, pos);
    viewV.y = dot(b, pos);
    viewV.z = dot(n, pos);
    viewVec = normalize(viewV);

    /* 法線マップ座標系(接空間)における光線ベクトル */
    vec3 lightV;
    lightV.x = dot(t, lgt);
    lightV.y = dot(b, lgt);
    lightV.z = dot(n, lgt);
    lightVec = normalize(lightV);

    /* 法線マップのテクスチャ座標 */
    gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;

    gl_Position = ftransform();
}