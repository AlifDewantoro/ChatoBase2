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

package com.gamatechno.ggfw.string.prettytime.i18n;

import com.gamatechno.ggfw.string.prettytime.Duration;
import com.gamatechno.ggfw.string.prettytime.TimeFormat;
import com.gamatechno.ggfw.string.prettytime.TimeUnit;
import com.gamatechno.ggfw.string.prettytime.impl.TimeFormatProvider;
import com.gamatechno.ggfw.string.prettytime.units.Century;
import com.gamatechno.ggfw.string.prettytime.units.Day;
import com.gamatechno.ggfw.string.prettytime.units.Decade;
import com.gamatechno.ggfw.string.prettytime.units.Hour;
import com.gamatechno.ggfw.string.prettytime.units.JustNow;
import com.gamatechno.ggfw.string.prettytime.units.Millennium;
import com.gamatechno.ggfw.string.prettytime.units.Millisecond;
import com.gamatechno.ggfw.string.prettytime.units.Minute;
import com.gamatechno.ggfw.string.prettytime.units.Month;
import com.gamatechno.ggfw.string.prettytime.units.Second;
import com.gamatechno.ggfw.string.prettytime.units.Week;
import com.gamatechno.ggfw.string.prettytime.units.Year;

import java.util.ListResourceBundle;

/**
 * Created with IntelliJ IDEA. User: Tumin Alexander Date: 2012-12-13 Time: 03:33
 */
public class Resources_ru extends ListResourceBundle implements TimeFormatProvider
{
   private static final Object[][] OBJECTS = new Object[0][0];

   private static final int tolerance = 50;

   // see http://translate.sourceforge.net/wiki/l10n/pluralforms
   private static final int russianPluralForms = 3;

   private static class TimeFormatAided implements TimeFormat
   {
      private final String[] pluarls;

      public TimeFormatAided(String... plurals)
      {
         if (plurals.length != russianPluralForms) {
            throw new IllegalArgumentException("Wrong plural forms number for russian language!");
         }
         this.pluarls = plurals;
      }

      @Override
      public String format(Duration duration)
      {
         long quantity = duration.getQuantityRounded(tolerance);
         StringBuilder result = new StringBuilder();
         result.append(quantity);
         return result.toString();
      }

      @Override
      public String formatUnrounded(Duration duration)
      {
         long quantity = duration.getQuantity();
         StringBuilder result = new StringBuilder();
         result.append(quantity);
         return result.toString();
      }

      @Override
      public String decorate(Duration duration, String time)
      {
         return performDecoration(
                  duration.isInPast(),
                  duration.isInFuture(),
                  duration.getQuantityRounded(tolerance),
                  time);
      }

      @Override
      public String decorateUnrounded(Duration duration, String time)
      {
         return performDecoration(
                  duration.isInPast(),
                  duration.isInFuture(),
                  duration.getQuantity(),
                  time);
      }

      private String performDecoration(boolean past, boolean future, long n, String time)
      {
         // a bit cryptic, yet well-tested
         // consider http://translate.sourceforge.net/wiki/l10n/pluralforms
         int pluralIdx = (n % 10 == 1 && n % 100 != 11 ? 0 : n % 10 >= 2 && n % 10 <= 4
                  && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2);
         if (pluralIdx > russianPluralForms) {
            // impossible happening
            throw new IllegalStateException("Wrong plural index was calculated somehow for russian language");
         }

         StringBuilder result = new StringBuilder();

         if (future) {
            result.append("через ");
         }

         result.append(time);
         result.append(' ');
         result.append(pluarls[pluralIdx]);

         if (past) {
            result.append(" назад");
         }

         return result.toString();
      }
   }

   @Override
   public Object[][] getContents()
   {
      return OBJECTS;
   }

   @Override
   public TimeFormat getFormatFor(TimeUnit t)
   {
      if (t instanceof JustNow) {
         return new TimeFormat() {
            @Override
            public String format(Duration duration)
            {
               return performFormat(duration);
            }

            @Override
            public String formatUnrounded(Duration duration)
            {
               return performFormat(duration);
            }

            private String performFormat(Duration duration)
            {
               if (duration.isInFuture()) {
                  return "сейчас";
               }
               if (duration.isInPast()) {
                  return "только что";
               }
               return null;
            }

            @Override
            public String decorate(Duration duration, String time)
            {
               return time;
            }

            @Override
            public String decorateUnrounded(Duration duration, String time)
            {
               return time;
            }
         };
      }
      else if (t instanceof Century) {
         return new TimeFormatAided("век", "века", "веков");
      }
      else if (t instanceof Day) {
         return new TimeFormatAided("день", "дня", "дней");
      }
      else if (t instanceof Decade) {
         return new TimeFormatAided("десятилетие", "десятилетия", "десятилетий");
      }
      else if (t instanceof Hour) {
         return new TimeFormatAided("час", "часа", "часов");
      }
      else if (t instanceof Millennium) {
         return new TimeFormatAided("тысячелетие", "тысячелетия", "тысячелетий");
      }
      else if (t instanceof Millisecond) {
         return new TimeFormatAided("миллисекунду", "миллисекунды", "миллисекунд");
      }
      else if (t instanceof Minute) {
         return new TimeFormatAided("минуту", "минуты", "минут");
      }
      else if (t instanceof Month) {
         return new TimeFormatAided("месяц", "месяца", "месяцев");
      }
      else if (t instanceof Second) {
         return new TimeFormatAided("секунду", "секунды", "секунд");
      }
      else if (t instanceof Week) {
         return new TimeFormatAided("неделю", "недели", "недель");
      }
      else if (t instanceof Year) {
         return new TimeFormatAided("год", "года", "лет");
      }
      return null; // error
   }
}
