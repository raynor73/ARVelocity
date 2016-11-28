attribute vec4 a_Position;
attribute vec4 a_TextureCoordinate;

uniform mat4 u_TextureCoordinateMatrix;

varying vec2 v_TextureCoordinate;

void main() {
	v_TextureCoordinate = (u_TextureCoordinateMatrix * a_TextureCoordinate).xy;
	gl_Position = a_Position;
}
