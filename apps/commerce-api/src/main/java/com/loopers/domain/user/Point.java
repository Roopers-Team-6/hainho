package com.loopers.domain.user;

public record Point(long balance) {
    public static final Point ZERO = new Point(0L);
}