package com.meizu.simplify.cache.enums;

public enum TimeEnum {

	NANOSECONDS {
        public long toNanos(long d)   { return d; }
    },

    MICROSECONDS {
        public long toMicros(long d)  { return d; }
    },

    MILLISECONDS {
        public long toMillis(long d)  { return d; }
    },

    SECONDS {
        public long toSeconds(long d) { return d; }
    },

    MINUTES {
        public long toMinutes(long d) { return d; }
    },

    HOURS {
        public long toHours(long d)   { return d; }
    },

    DAYS {
        public long toDays(long d)    { return d; }
    };
}
