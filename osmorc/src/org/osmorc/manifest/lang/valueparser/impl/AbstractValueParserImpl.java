/*
 * Copyright (c) 2007-2009, Osmorc Development Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright notice, this list
 *       of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this
 *       list of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *     * Neither the name of 'Osmorc Development Team' nor the names of its contributors may be
 *       used to endorse or promote products derived from this software without specific
 *       prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.osmorc.manifest.lang.valueparser.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osmorc.manifest.lang.psi.ManifestHeaderValue;
import org.osmorc.manifest.lang.valueparser.ValueParser;

/**
 * Author: Robert F. Beeger (robert@beeger.net)
 */
public abstract class AbstractValueParserImpl<T> implements ValueParser<T>
{
  public T parseValue(@NotNull ManifestHeaderValue headerValue, @Nullable AnnotationHolder annotationHolder)
  {
    String valueText = headerValue.getValueText();
    return parseValue(headerValue, valueText, 0, annotationHolder);
  }

  public T parseValue(
      @NotNull ManifestHeaderValue headerValue, int start, int end, @Nullable AnnotationHolder annotationHolder)
  {
    String valueText = headerValue.getValueText();
    valueText = valueText.substring(start, end);
    return parseValue(headerValue, valueText, start, annotationHolder);
  }

  public T parseValue(@NotNull String text, int start, int end)
  {
    String valueText = text.substring(start, end);
    return parseValue(null, valueText, start, null);
  }

  protected abstract T parseValue(@Nullable ManifestHeaderValue headerValue, String text, int start,
                                  @Nullable AnnotationHolder annotationHolder);
}