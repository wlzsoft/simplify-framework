package com.meizu.simplify.cache.enums;

public enum TimeEnum {
	
	NANOSECONDS("纳秒", "ns") {
        public long toNanos(long d)   { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    MICROSECONDS("微秒", "ms") {
        public long toMicros(long d)  { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    MILLISECONDS("毫秒", "SSS") {
        public long toMillis(long d)  { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    SECONDS("秒", "ss") {
        public long toSeconds(long d) { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    MINUTES("分钟", "mm") {
        public long toMinutes(long d) { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    HOURS(
			"小时", "HH") {
        public long toHours(long d)   { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    },

    DAYS("天", "dd") {
        public long toDays(long d)    { return d; }
		@Override
		int excessNanos(long d, long m) {
			return 0;
		}
    };
	
	private String text;
	private String value;
	
	public String getText() {
		return text;
	}

	public String getValue() {
		return value;
	}

	private TimeEnum(String text, String value) {
		this.text = text;
		this.value = value;
	}
	
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
