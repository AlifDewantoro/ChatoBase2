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
 * Created by Azimkhan Yerzhan on 5/8/2017
 */
public class Resources_kk extends ListResourceBundle implements TimeFormatProvider {

    private static final Object[][] OBJECTS = new Object[0][0];
    @Override
    protected Object[][] getContents() {
        return OBJECTS;
    }

    private static class KkTimeFormat implements TimeFormat
    {
        private final int tolerance = 50;
        private final String[] forms;

        public KkTimeFormat(String... plurals)
        {
            if (plurals.length != 2) {
                throw new IllegalArgumentException("Future and past forms must be provided for kazakh language!");
            }
            this.forms = plurals;
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
            StringBuilder builder = new StringBuilder();
            int formIndex = past ? 0 : 1;

            builder.append(time);
            builder.append(' ');
            builder.append(forms[formIndex]);
            builder.append(' ');

            if (past) {
                builder.append("бұрын");
            }

            if (future) {
                builder.append("кейін");
            }
            return builder.toString();
        }
    }

    @Override
    public TimeFormat getFormatFor(TimeUnit t) {
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
                        return "дәл қазір";
                    }
                    if (duration.isInPast()) {
                        return "жана ғана";
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
            return new KkTimeFormat("ғасыр","ғасырдан");
        }
        else if (t instanceof Day) {
            return new KkTimeFormat("күн", "күннен");
        }
        else if (t instanceof Decade) {
            return new KkTimeFormat("онжылдық", "онжылдықтан");
        }
        else if (t instanceof Hour) {
            return new KkTimeFormat("сағат", "сағаттан");
        }
        else if (t instanceof Millennium) {
            return new KkTimeFormat("мыңжылдық", "мыңжылдықтан");
        }
        else if (t instanceof Millisecond) {
            return new KkTimeFormat("миллисекунд", "миллисекундтан");
        }
        else if (t instanceof Minute) {
            return new KkTimeFormat("минут", "минуттан");
        }
        else if (t instanceof Month) {
            return new KkTimeFormat("ай", "айдан");
        }
        else if (t instanceof Second) {
            return new KkTimeFormat("секунд", "секундтан");
        }
        else if (t instanceof Week) {
            return new KkTimeFormat("апта", "аптадан");
        }
        else if (t instanceof Year) {
            return new KkTimeFormat("жыл", "жылдан");
        }
        return null;
    }
}
