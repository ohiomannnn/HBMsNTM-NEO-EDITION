package com.hbm.render.anim;

import com.hbm.config.ClientConfig;

//"pieces" that make up a bus
public class BusAnimationKeyframe {

    // whether the next frame "snaps" to the intended value or has interpolation
    // it's an enum so stuff like accelerated animations between just
    // two frames could be implemented
    public enum IType {
        /** Teleport */
        CONSTANT,
        /** Linear interpolation */
        LINEAR,
        /** "Sine wave up", quarter of a sine peak that goes from neutral to rising */
        SIN_UP,
        /** "Sine wave down", quarter of a sine peak that goes from rising back to neutral */
        SIN_DOWN,
        /** "Sine wave", first half of a sine peak, accelerating up and then decelerating, makes for smooth movement */
        SIN_FULL,

        // blender magic curves
        BEZIER,

        // blender inertial
        SINE,
        QUAD,
        CUBIC,
        QUART,
        QUINT,
        EXPO,
        CIRC,

        // blendor dynamic
        BOUNCE,
        ELASTIC,
        BACK,
    }

    // Easing
    public enum EType {
        AUTO,
        EASE_IN,
        EASE_OUT,
        EASE_IN_OUT,
    }

    // Handle type
    public enum HType {
        FREE,
        ALIGNED,
        VECTOR,
        AUTO,
        AUTO_CLAMPED,
    }

    public float value;
    public IType interpolationType;
    public EType easingType;
    public int originalDuration;
    public int duration;

    // bezier handles
    public float leftX;
    public float leftY;
    public HType leftType;
    public float rightX;
    public float rightY;
    public HType rightType;

    // elastics
    public float amplitude;
    public float period;

    // back (overshoot)
    public float back;

    // this one can be used for "reset" type keyframes
    public BusAnimationKeyframe() {
        this.value = 0;
        this.originalDuration = this.duration = 1;
        this.interpolationType = IType.LINEAR;
        this.easingType = EType.AUTO;
    }

    public BusAnimationKeyframe(float value, int duration) {
        this();
        this.value = value;
        // todo make ClientConfig GUN_ANIMATION_SPEED
        this.originalDuration = this.duration = (int) (duration / Math.max(0.001D, 1D));
    }

    public BusAnimationKeyframe(float value, int duration, IType interpolation) {
        this(value, duration);
        this.interpolationType = interpolation;
    }

    public BusAnimationKeyframe(float value, int duration, IType interpolation, EType easing) {
        this(value, duration, interpolation);
        this.easingType = easing;
    }

    public float interpolate(float startTime, float currentTime, BusAnimationKeyframe previous) {
        if(previous == null) previous = new BusAnimationKeyframe();

        float a = value;
        float b = previous.value;
        float t = time(startTime, currentTime, duration);

        float begin = previous.value;
        float change = value - previous.value;
        float time = currentTime - startTime;

        // Constant value optimisation
        if(Math.abs(previous.value - value) < 0.000001) return value;

        if(previous.interpolationType == IType.BEZIER) {
            float v1x = startTime;
            float v1y = previous.value;
            float v2x = previous.rightX;
            float v2y = previous.rightY;

            float v3x = leftX;
            float v3y = leftY;
            float v4x = startTime + duration;
            float v4y = value;

            // correct beziers into non-looping fcurves
            float h1x = v1x - v2x;
            float h1y = v1y - v2y;

            float h2x = v4x - v3x;
            float h2y = v4y - v3y;

            float len = v4x - v1x;
            float len1 = Math.abs(h1x);
            float len2 = Math.abs(h2x);

            if(len1 + len2 != 0) {
                if(len1 > len) {
                    float fac = len / len1;
                    v2x = v1x - fac * h1x;
                    v2y = v1y - fac * h1y;
                }

                if(len2 > len) {
                    float fac = len / len2;
                    v3x = v4x - fac * h2x;
                    v3y = v4y - fac * h2y;
                }
            }

            float curveT = findZero(currentTime, v1x, v2x, v3x, v4x);
            return cubicBezier(v1y, v2y, v3y, v4y, curveT);
        } else if(previous.interpolationType == IType.BACK) {
            return switch (previous.easingType) {
                case EASE_IN -> BLI_easing_back_ease_in(time, begin, change, duration, previous.back);
                case EASE_IN_OUT -> BLI_easing_back_ease_in_out(time, begin, change, duration, previous.back);
                default -> BLI_easing_back_ease_out(time, begin, change, duration, previous.back);
            };
        } else if(previous.interpolationType == IType.BOUNCE) {
            return switch (previous.easingType) {
                case EASE_IN -> BLI_easing_bounce_ease_in(time, begin, change, duration);
                case EASE_IN_OUT -> BLI_easing_bounce_ease_in_out(time, begin, change, duration);
                default -> BLI_easing_bounce_ease_out(time, begin, change, duration);
            };
        } else if(previous.interpolationType == IType.CIRC) {
            return switch (previous.easingType) {
                case EASE_OUT -> BLI_easing_circ_ease_out(time, begin, change, duration);
                case EASE_IN_OUT -> BLI_easing_circ_ease_in_out(time, begin, change, duration);
                default -> BLI_easing_circ_ease_in(time, begin, change, duration);
            };
        } else if(previous.interpolationType == IType.CUBIC) {
            return switch (previous.easingType) {
                case EASE_OUT -> BLI_easing_cubic_ease_out(time, begin, change, duration);
                case EASE_IN_OUT -> BLI_easing_cubic_ease_in_out(time, begin, change, duration);
                default -> BLI_easing_cubic_ease_in(time, begin, change, duration);
            };
        } else if(previous.interpolationType == IType.ELASTIC) {
            return switch (previous.easingType) {
                case EASE_IN ->
                        BLI_easing_elastic_ease_in(time, begin, change, duration, previous.amplitude, previous.period);
                case EASE_IN_OUT ->
                        BLI_easing_elastic_ease_in_out(time, begin, change, duration, previous.amplitude, previous.period);
                default ->
                        BLI_easing_elastic_ease_out(time, begin, change, duration, previous.amplitude, previous.period);
            };
        } else if(previous.interpolationType == IType.EXPO) {
            return switch (previous.easingType) {
                case EASE_OUT -> BLI_easing_expo_ease_out(time, begin, change, duration);
                case EASE_IN_OUT -> BLI_easing_expo_ease_in_out(time, begin, change, duration);
                default -> BLI_easing_expo_ease_in(time, begin, change, duration);
            };
        } else if(previous.interpolationType == IType.QUAD) {
            return switch (previous.easingType) {
                case EASE_OUT -> BLI_easing_quad_ease_out(time, begin, change, duration);
                case EASE_IN_OUT -> BLI_easing_quad_ease_in_out(time, begin, change, duration);
                default -> BLI_easing_quad_ease_in(time, begin, change, duration);
            };
        } else if(previous.interpolationType == IType.QUART) {
            return switch (previous.easingType) {
                case EASE_OUT -> BLI_easing_quart_ease_out(time, begin, change, duration);
                case EASE_IN_OUT -> BLI_easing_quart_ease_in_out(time, begin, change, duration);
                default -> BLI_easing_quart_ease_in(time, begin, change, duration);
            };
        } else if(previous.interpolationType == IType.QUINT) {
            return switch (previous.easingType) {
                case EASE_OUT -> BLI_easing_quint_ease_out(time, begin, change, duration);
                case EASE_IN_OUT -> BLI_easing_quint_ease_in_out(time, begin, change, duration);
                default -> BLI_easing_quint_ease_in(time, begin, change, duration);
            };
        } else if(previous.interpolationType == IType.SINE) {
            return switch (previous.easingType) {
                case EASE_OUT -> BLI_easing_sine_ease_out(time, begin, change, duration);
                case EASE_IN_OUT -> BLI_easing_sine_ease_in_out(time, begin, change, duration);
                default -> BLI_easing_sine_ease_in(time, begin, change, duration);
            };
        }

        return (a - b) * t + b;
    }

    private float sqrt3(float d) {
        if(d > 0.000001) {
            return (float) Math.exp(Math.log(d) / 3.0);
        } else if(d > -0.000001) {
            return 0;
        } else {
            return (float) -Math.exp(Math.log(-d) / 3.0);
        }
    }

    private float time(float start, float end, float duration) {
        if(interpolationType == IType.SIN_UP) return (float) (-Math.sin(((end - start) / duration * Math.PI + Math.PI) / 2) + 1);
        if(interpolationType == IType.SIN_DOWN) return (float) Math.sin((end - start) / duration * Math.PI / 2);
        if(interpolationType == IType.SIN_FULL) return (float) ((-Math.cos((end - start) / duration * Math.PI) + 1) / 2D);
        return (end - start) / duration;
    }

    // Blender bezier solvers, but rewritten (pain)
    private float solveCubic(float c0, float c1, float c2, float c3) {
        if(c3 > 0.000001 || c3 < -0.000001) {
            float a = c2 / c3;
            float b = c1 / c3;
            float c = c0 / c3;
            a = a / 3;

            float p = b / 3 - a * a;
            float q = (2 * a * a * a - a * b + c) / 2;
            float d = q * q + p * p * p;

            if(d > 0.000001) {
                float t = (float) Math.sqrt(d);
                return sqrt3(-q + t) + sqrt3(-q - t) - a;
            } else if(d > -0.000001) {
                float t = sqrt3(-q);
                float result = 2 * t - a;
                if(result < 0.000001 || result > 1.000001) {
                    result = -t - a;
                }
                return result;
            }

            float phi = (float) Math.acos(-q / Math.sqrt(-(p * p * p)));
            float t = (float) Math.sqrt(-p);
            p = (float) Math.cos(phi / 3);
            q = (float) Math.sqrt(3 - 3 * p * p);
            float result = 2 * t * p - a;
            if(result < 0.000001 || result > 1.000001) {
                result = -t * (p + q) - a;
            }
            if(result < 0.000001 || result > 1.000001) {
                result = -t * (p - q) - a;
            }
            return result;
        }

        float a = c2;
        float b = c1;
        float c = c0;

        if(a > 0.000001) {
            float p = b * b - 4 * a * c;

            if(p > 0.000001) {
                p = (float) Math.sqrt(p);
                float result = (-b - p) / (2 * a);
                if(result < 0.000001 || result > 1.000001) {
                    result = (-b + p) / (2 * a);
                }
                return result;
            } else if(p > -0.000001) {
                return -b / (2 * a);
            }
        }

        if(b > 0.000001) {
            return -c / b;
        }

        return 0;
    }

    private float findZero(float t, float x1, float x2, float x3, float x4) {
        float c0 = x1 - t;
        float c1 = 3.0f * (x2 - x1);
        float c2 = 3.0f * (x1 - 2.0f * x2 + x3);
        float c3 = x4 - x1 + 3.0f * (x2 - x3);

        return solveCubic(c0, c1, c2, c3);
    }

    private float cubicBezier(float y1, float y2, float y3, float y4, float t) {
        float c0 = y1;
        float c1 = 3.0f * (y2 - y1);
        float c2 = 3.0f * (y1 - 2.0f * y2 + y3);
        float c3 = y4 - y1 + 3.0f * (y2 - y3);

        return c0 + t * c1 + t * t * c2 + t * t * t * c3;
    }

    /**
     * EASING FUNCTIONS, taken directly from Blender `easing.c`
     */

    float BLI_easing_back_ease_in(float time, float begin, float change, float duration, float overshoot) {
        time /= duration;
        return change * time * time * ((overshoot + 1) * time - overshoot) + begin;
    }

    float BLI_easing_back_ease_out(float time, float begin, float change, float duration, float overshoot) {
        time = time / duration - 1;
        return change * (time * time * ((overshoot + 1) * time + overshoot) + 1) + begin;
    }

    float BLI_easing_back_ease_in_out(float time, float begin, float change, float duration, float overshoot) {
        overshoot *= 1.525f;
        if((time /= duration / 2) < 1.0f) {
            return change / 2 * (time * time * ((overshoot + 1) * time - overshoot)) + begin;
        }
        time -= 2.0f;
        return change / 2 * (time * time * ((overshoot + 1) * time + overshoot) + 2) + begin;
    }

    float BLI_easing_bounce_ease_out(float time, float begin, float change, float duration) {
        time /= duration;
        if(time < (1 / 2.75f)) {
            return change * (7.5625f * time * time) + begin;
        }
        if(time < (2 / 2.75f)) {
            time -= (1.5f / 2.75f);
            return change * ((7.5625f * time) * time + 0.75f) + begin;
        }
        if(time < (2.5f / 2.75f)) {
            time -= (2.25f / 2.75f);
            return change * ((7.5625f * time) * time + 0.9375f) + begin;
        }
        time -= (2.625f / 2.75f);
        return change * ((7.5625f * time) * time + 0.984375f) + begin;
    }

    float BLI_easing_bounce_ease_in(float time, float begin, float change, float duration) {
        return change - BLI_easing_bounce_ease_out(duration - time, 0, change, duration) + begin;
    }

    float BLI_easing_bounce_ease_in_out(float time, float begin, float change, float duration) {
        if(time < duration / 2) {
            return BLI_easing_bounce_ease_in(time * 2, 0, change, duration) * 0.5f + begin;
        }
        return BLI_easing_bounce_ease_out(time * 2 - duration, 0, change, duration) * 0.5f + change * 0.5f + begin;
    }

    float BLI_easing_circ_ease_in(float time, float begin, float change, float duration) {
        time /= duration;
        return (float) (-change * (Math.sqrt(1 - time * time) - 1) + begin);
    }

    float BLI_easing_circ_ease_out(float time, float begin, float change, float duration) {
        time = time / duration - 1;
        return (float) (change * Math.sqrt(1 - time * time) + begin);
    }

    float BLI_easing_circ_ease_in_out(float time, float begin, float change, float duration) {
        if((time /= duration / 2) < 1.0f) {
            return (float) (-change / 2 * (Math.sqrt(1 - time * time) - 1) + begin);
        }
        time -= 2.0f;
        return (float) (change / 2 * (Math.sqrt(1 - time * time) + 1) + begin);
    }

    float BLI_easing_cubic_ease_in(float time, float begin, float change, float duration) {
        time /= duration;
        return change * time * time * time + begin;
    }

    float BLI_easing_cubic_ease_out(float time, float begin, float change, float duration) {
        time = time / duration - 1;
        return change * (time * time * time + 1) + begin;
    }

    float BLI_easing_cubic_ease_in_out(float time, float begin, float change, float duration) {
        if((time /= duration / 2) < 1.0f) {
            return change / 2 * time * time * time + begin;
        }
        time -= 2.0f;
        return change / 2 * (time * time * time + 2) + begin;
    }

    float elastic_blend(float time, float change, float duration, float amplitude, float s, float f) {
        if(change != 0) {
            /*
             * Looks like a magic number,
             * but this is a part of the sine curve we need to blend from
             */
            float t = Math.abs(s);
            if(amplitude != 0) {
                f *= amplitude / Math.abs(change);
            } else {
                f = 0.0f;
            }

            if(Math.abs(time * duration) < t) {
                float l = Math.abs(time * duration) / t;
                f = (f * l) + (1.0f - l);
            }
        }

        return f;
    }

    float BLI_easing_elastic_ease_in(float time, float begin, float change, float duration, float amplitude, float period) {
        float s;
        float f = 1.0f;

        if(time == 0.0f) {
            return begin;
        }

        if((time /= duration) == 1.0f) {
            return begin + change;
        }
        time -= 1.0f;
        if(period == 0) {
            period = duration * 0.3f;
        }
        if(amplitude == 0 || amplitude < Math.abs(change)) {
            s = period / 4;
            f = elastic_blend(time, change, duration, amplitude, s, f);
            amplitude = change;
        } else {
            s = (float) (period / (2 * (float) Math.PI) * Math.asin(change / amplitude));
        }

        return (float) ((-f * (amplitude * Math.pow(2, 10 * time) * Math.sin((time * duration - s) * (2 * (float) Math.PI) / period))) + begin);
    }

    float BLI_easing_elastic_ease_out(float time, float begin, float change, float duration, float amplitude, float period) {
        float s;
        float f = 1.0f;

        if(time == 0.0f) {
            return begin;
        }
        if((time /= duration) == 1.0f) {
            return begin + change;
        }
        time = -time;
        if(period == 0) {
            period = duration * 0.3f;
        }
        if(amplitude == 0 || amplitude < Math.abs(change)) {
            s = period / 4;
            f = elastic_blend(time, change, duration, amplitude, s, f);
            amplitude = change;
        } else {
            s = (float) (period / (2 * (float) Math.PI) * Math.asin(change / amplitude));
        }

        return (float) ((f * (amplitude * Math.pow(2, 10 * time) * Math.sin((time * duration - s) * (2 * (float) Math.PI) / period))) + change + begin);
    }

    float BLI_easing_elastic_ease_in_out(float time, float begin, float change, float duration, float amplitude, float period) {
        float s;
        float f = 1.0f;

        if(time == 0.0f) {
            return begin;
        }
        if((time /= duration / 2) == 2.0f) {
            return begin + change;
        }
        time -= 1.0f;
        if(period == 0) {
            period = duration * (0.3f * 1.5f);
        }
        if(amplitude == 0 || amplitude < Math.abs(change)) {
            s = period / 4;
            f = elastic_blend(time, change, duration, amplitude, s, f);
            amplitude = change;
        } else {
            s = (float) (period / (2 * (float) Math.PI) * Math.asin(change / amplitude));
        }

        if(time < 0.0f) {
            f *= -0.5f;
            return (float) ((f * (amplitude * Math.pow(2, 10 * time) * Math.sin((time * duration - s) * (2 * (float) Math.PI) / period))) + begin);
        }

        time = -time;
        f *= 0.5f;
        return (float) ((f * (amplitude * Math.pow(2, 10 * time) * Math.sin((time * duration - s) * (2 * (float) Math.PI) / period))) + change + begin);
    }

    static final float pow_min = 0.0009765625f; /* = 2^(-10) */
    static final float pow_scale = 1.0f / (1.0f - 0.0009765625f);

    float BLI_easing_expo_ease_in(float time, float begin, float change, float duration) {
        if(time == 0.0) {
            return begin;
        }
        return (float) (change * (Math.pow(2, 10 * (time / duration - 1)) - pow_min) * pow_scale + begin);
    }

    float BLI_easing_expo_ease_out(float time, float begin, float change, float duration) {
        if(time == 0.0) {
            return begin;
        }
        return (float) (change * (1 - (Math.pow(2, -10 * time / duration) - pow_min) * pow_scale) + begin);
    }

    float BLI_easing_expo_ease_in_out(float time, float begin, float change, float duration) {
        float duration_half = duration / 2.0f;
        float change_half = change / 2.0f;
        if(time <= duration_half) {
            return BLI_easing_expo_ease_in(time, begin, change_half, duration_half);
        }
        return BLI_easing_expo_ease_out(time - duration_half, begin + change_half, change_half, duration_half);
    }

    float BLI_easing_linear_ease(float time, float begin, float change, float duration) {
        return change * time / duration + begin;
    }

    float BLI_easing_quad_ease_in(float time, float begin, float change, float duration) {
        time /= duration;
        return change * time * time + begin;
    }

    float BLI_easing_quad_ease_out(float time, float begin, float change, float duration) {
        time /= duration;
        return -change * time * (time - 2) + begin;
    }

    float BLI_easing_quad_ease_in_out(float time, float begin, float change, float duration) {
        if((time /= duration / 2) < 1.0f) {
            return change / 2 * time * time + begin;
        }
        time -= 1.0f;
        return -change / 2 * (time * (time - 2) - 1) + begin;
    }

    float BLI_easing_quart_ease_in(float time, float begin, float change, float duration) {
        time /= duration;
        return change * time * time * time * time + begin;
    }

    float BLI_easing_quart_ease_out(float time, float begin, float change, float duration) {
        time = time / duration - 1;
        return -change * (time * time * time * time - 1) + begin;
    }

    float BLI_easing_quart_ease_in_out(float time, float begin, float change, float duration) {
        if((time /= duration / 2) < 1.0f) {
            return change / 2 * time * time * time * time + begin;
        }
        time -= 2.0f;
        return -change / 2 * (time * time * time * time - 2) + begin;
    }

    float BLI_easing_quint_ease_in(float time, float begin, float change, float duration) {
        time /= duration;
        return change * time * time * time * time * time + begin;
    }

    float BLI_easing_quint_ease_out(float time, float begin, float change, float duration) {
        time = time / duration - 1;
        return change * (time * time * time * time * time + 1) + begin;
    }

    float BLI_easing_quint_ease_in_out(float time, float begin, float change, float duration) {
        if((time /= duration / 2) < 1.0f) {
            return change / 2 * time * time * time * time * time + begin;
        }
        time -= 2.0f;
        return change / 2 * (time * time * time * time * time + 2) + begin;
    }

    float BLI_easing_sine_ease_in(float time, float begin, float change, float duration) {
        return (float) (-change * Math.cos(time / duration * (float) Math.PI / 2) + change + begin);
    }

    float BLI_easing_sine_ease_out(float time, float begin, float change, float duration) {
        return (float) (change * Math.sin(time / duration * (float) Math.PI / 2) + begin);
    }

    float BLI_easing_sine_ease_in_out(float time, float begin, float change, float duration) {
        return (float) (-change / 2 * (Math.cos(Math.PI * time / duration) - 1) + begin);
    }

}