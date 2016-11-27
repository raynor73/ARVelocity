attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinate;

void main() {
	v_TextureCoordinate = a_TextureCoordinates;
	gl_Position = a_Position;
}
