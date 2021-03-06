/*
 * Copyright (c) 2011, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;

public class MotionBlurState extends LinearConvolveKernel {
    private float radius;
    private float angle;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        if (radius < 0f || radius > 63f) {
            throw new IllegalArgumentException("Radius must be in the range [0,63]");
        }
        this.radius = radius;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getHPad() {
        return (int) Math.ceil(Math.abs(Math.cos(angle)) * radius);
    }

    public int getVPad() {
        return (int) Math.ceil(Math.abs(Math.sin(angle)) * radius);
    }

    @Override
    public LinearConvolveRenderState getRenderState(BaseTransform filtertx) {
        float dx = (float) Math.cos(angle);
        float dy = (float) Math.sin(angle);
        return new GaussianRenderState(radius, dx, dy, filtertx);
    }

    @Override
    public boolean isNop() {
        return (radius == 0f);
    }

    @Override
    public int getKernelSize(int pass) {
        return ((int) Math.ceil(radius)) * 2 + 1;
    }

    @Override
    public final Rectangle getResultBounds(Rectangle srcdimension, int pass) {
        Rectangle ret = new Rectangle(srcdimension);
        ret.grow(getHPad(), getVPad());
        return ret;
    }
}
