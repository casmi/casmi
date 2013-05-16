varying vec3 P;
varying vec3 N;

void main(void)
{
    P = vec3(gl_ModelViewMatrix * gl_Vertex);
    N = normalize(gl_NormalMatrix * gl_Normal);
    gl_Position = ftransform();
}
