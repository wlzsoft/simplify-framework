package com.meizu.simplify.cache.enums;

public enum TimeEnum {

	NANOSECONDS {
        public long toNanos(long d)   { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    MICROSECONDS {
        public long toMicros(long d)  { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    MILLISECONDS {
        public long toMillis(long d)  { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    SECONDS {
        public long toSeconds(long d) { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    MINUTES {
        public long toMinutes(long d) { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    HOURS {
        public long toHours(long d)   { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    DAYS {
        public long toDays(long d)    { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    };
	
    public long toNanos(long duration) {
        throw new AbstractMethodError();
    }

    public long toMicros(long duration) {
        throw new AbstractMethodError();
    }

    public long toMillis(long duration) {
        throw new AbstractMethodError();
    }

    public long toSeconds(long duration) {
        throw new AbstractMethodError();
    }

    public long toMinutes(long duration) {
        throw new AbstractMethodError();
    }

    public long toHours(long duration) {
        throw new AbstractMethodError();
    }

    public long toDays(long duration) {
        throw new AbstractMethodError();
    }
    
    abstract int excessNanos(long d, long m);
}
