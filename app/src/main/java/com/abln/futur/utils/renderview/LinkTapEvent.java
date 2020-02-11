/**
 * Copyright 2016 Bartosz Schiller
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.abln.futur.utils.renderview;

import android.graphics.RectF;

import com.shockwave.pdfium.PdfDocument;

public class LinkTapEvent {
    private float originalX;
    private float originalY;
    private float documentX;
    private float documentY;
    private RectF mappedLinkRect;
    private PdfDocument link;

    public LinkTapEvent(float originalX, float originalY, float documentX, float documentY, RectF mappedLinkRect, PdfDocument link) {
        this.originalX = originalX;
        this.originalY = originalY;
        this.documentX = documentX;
        this.documentY = documentY;
        this.mappedLinkRect = mappedLinkRect;
        this.link = link;
    }

    public float getOriginalX() {
        return originalX;
    }

    public float getOriginalY() {
        return originalY;
    }

    public float getDocumentX() {
        return documentX;
    }

    public float getDocumentY() {
        return documentY;
    }

    public RectF getMappedLinkRect() {
        return mappedLinkRect;
    }

    public PdfDocument getLink() {
        return link;
    }
}
