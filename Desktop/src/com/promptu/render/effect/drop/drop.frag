#version 330

out vec4 finalCol;

in vec4 Color;
in vec4 Position;
in vec2 TexCoords;

uniform vec4 u_bounds;
uniform sampler2D u_texture;
uniform float sigma;

bool closeEnough(vec3 col, vec3 target, float thres) {
    if(col.r < target.r-thres || col.r > target.r+thres) return false;
    if(col.g < target.g-thres || col.g > target.g+thres) return false;
    if(col.b < target.b-thres || col.b > target.b+thres) return false;
    return true;
}

bool isNear(sampler2D tex, vec2 pos, int range, vec4 searchFor) {
    for(int x = -range; x <= range; x++) {
        for(int y = -range; y <= range; y++) {
            vec4 c = texture(tex, pos+(vec2(x, y)));
            if(closeEnough(c.rgb, vec3(1.0), 0.1))
                return true;
        }
    }
    return false;
}

vec4 erf(vec4 x) {
  vec4 s = sign(x), a = abs(x);
  x = 1.0 + (0.278393 + (0.230389 + 0.078108 * (a * a)) * a) * a;
  x *= x;
  return s - s / (x * x);
}

// Return the mask for the shadow of a box from lower to upper
float boxShadow(vec2 lower, vec2 upper, vec2 point, float sigma) {
  vec4 query = vec4(point - lower, upper - point);
  vec4 integral = 0.5 + 0.5 * erf(query * (sqrt(0.5) / sigma));
  return (integral.z - integral.x) * (integral.w - integral.y);
}




void main() {
    finalCol = vec4(1.0);
}
