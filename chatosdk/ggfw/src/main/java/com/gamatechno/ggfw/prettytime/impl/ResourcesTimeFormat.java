/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018. Shendy Aditya Syamsudin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.gamatechno.ggfw.prettytime.impl;


import com.gamatechno.ggfw.prettytime.Duration;
import com.gamatechno.ggfw.prettytime.LocaleAware;
import com.gamatechno.ggfw.prettytime.TimeFormat;
import com.gamatechno.ggfw.prettytime.format.SimpleTimeFormat;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Represents a simple method of formatting a specific {@link } of time
 *
 * @author lb3
 */
public class ResourcesTimeFormat extends SimpleTimeFormat implements TimeFormat, LocaleAware<ResourcesTimeFormat>
{
   private ResourceBundle bundle;
   private final ResourcesTimeUnit unit;
   private TimeFormat override;
   private String overrideResourceBundle; // If used this bundle will override the included bundle

   public ResourcesTimeFormat(ResourcesTimeUnit unit)
   {
      this.unit = unit;
   }

   public ResourcesTimeFormat(ResourcesTimeUnit unit, String overrideResourceBundle)
   {
      this.unit = unit;
      this.overrideResourceBundle = overrideResourceBundle;
   }

   @Override
   public ResourcesTimeFormat setLocale(Locale locale)
   {
      if (overrideResourceBundle != null) {
         try {
            // Attempt to load the bundle that the user passed in, maybe it exists, maybe not
            bundle = ResourceBundle.getBundle(overrideResourceBundle, locale);
         } catch (Exception e) {
            // Throw away if the bundle doesn't contain this local
         }
      }

      // If the bundle doesn't exist then load the default included one
      if (bundle == null) {
         bundle = ResourceBundle.getBundle(unit.getResourceBundleName(), locale);
      }

      if (bundle instanceof TimeFormatProvider)
      {
         TimeFormat format = ((TimeFormatProvider) bundle).getFormatFor(unit);
         if (format != null)
         {
            this.override = format;
         }
      }
      else
      {
         override = null;
      }

      if (override == null)
      {
         setPattern(bundle.getString(unit.getResourceKeyPrefix() + "Pattern"));
         setFuturePrefix(bundle.getString(unit.getResourceKeyPrefix() + "FuturePrefix"));
         setFutureSuffix(bundle.getString(unit.getResourceKeyPrefix() + "FutureSuffix"));
         setPastPrefix(bundle.getString(unit.getResourceKeyPrefix() + "PastPrefix"));
         setPastSuffix(bundle.getString(unit.getResourceKeyPrefix() + "PastSuffix"));

         setSingularName(bundle.getString(unit.getResourceKeyPrefix() + "SingularName"));
         setPluralName(bundle.getString(unit.getResourceKeyPrefix() + "PluralName"));

         try {
            setFuturePluralName(bundle.getString(unit.getResourceKeyPrefix() + "FuturePluralName"));
         }
         catch (Exception e) {}
         try {
            setFutureSingularName((bundle.getString(unit.getResourceKeyPrefix() + "FutureSingularName")));
         }
         catch (Exception e) {}
         try {
            setPastPluralName((bundle.getString(unit.getResourceKeyPrefix() + "PastPluralName")));
         }
         catch (Exception e) {}
         try {
            setPastSingularName((bundle.getString(unit.getResourceKeyPrefix() + "PastSingularName")));
         }
         catch (Exception e) {}

      }

      return this;
   }

   @Override
   public String decorate(Duration duration, String time)
   {
      return override == null ? super.decorate(duration, time) : override.decorate(duration, time);
   }

   @Override
   public String decorateUnrounded(Duration duration, String time)
   {
      return override == null ? super.decorateUnrounded(duration, time) : override.decorateUnrounded(duration, time);
   }

   @Override
   public String format(Duration duration)
   {
      return override == null ? super.format(duration) : override.format(duration);
   }

   @Override
   public String formatUnrounded(Duration duration)
   {
      return override == null ? super.formatUnrounded(duration) : override.formatUnrounded(duration);
   }
}